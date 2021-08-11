/*
 * Initium <https://www.github.com/zorroware/initium>
 * Copyright (C) 2021 Zorroware
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.zorroware.initium.listeners;

import io.github.zorroware.initium.Initium;
import io.github.zorroware.initium.command.AbstractCommand;
import io.github.zorroware.initium.config.Config;
import io.github.zorroware.initium.util.EmbedUtil;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import lombok.Getter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedTransferQueue;

/**
 * A message listener that handles processing and executing commands.
 */
public class CommandListener implements EventListener {
    // Logger
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandListener.class);

    // References
    private static final Config CONFIG = Initium.getConfig();
    private static final Map<String, AbstractCommand> COMMAND_MAP = Initium.getCommandMap();
    private static final Map<String, String> ALIAS_MAP = Initium.getAliasMap();

    // Queueing
    private static final @Getter ExecutorService threadPool = Executors.newCachedThreadPool();
    private static final @Getter Map<Long, Queue<MessageReceivedEvent>> userQueueMap = new Long2ObjectOpenHashMap<>();

    @Override
    public void onEvent(@Nonnull GenericEvent event) {
        if (!(event instanceof MessageReceivedEvent)) return;
        MessageReceivedEvent messageReceivedEvent = (MessageReceivedEvent) event;

        // These checks go first because they're most likely to fail
        if (!messageReceivedEvent.getMessage().getContentRaw().startsWith(CONFIG.getPrefix())) return;
        if (messageReceivedEvent.getChannelType().equals(ChannelType.PRIVATE)) return;
        if (messageReceivedEvent.getAuthor().isBot()) return;

        // Initialize a queue
        Queue<MessageReceivedEvent> queue =
                userQueueMap.computeIfAbsent(messageReceivedEvent.getAuthor().getIdLong(), k -> new LinkedTransferQueue<>());
        // Submit the first assignment to the queue
        queue.offer(messageReceivedEvent);

        // Only start the queue loop if this is the first entry
        if (queue.size() == 1) {
            // Submit to thread pool for command processing
            threadPool.execute(() -> {
                do {
                    // Use peek instead of poll/remove because the spot in the queue should only be freed when done processing
                    MessageReceivedEvent queuedMessageReceivedEvent = queue.peek();
                    assert queuedMessageReceivedEvent != null;

                    // Command parsing
                    String[] formatted = queuedMessageReceivedEvent.getMessage().getContentRaw().substring(CONFIG.getPrefix().length()).split(" ");
                    String name = formatted[0];
                    String[] args = Arrays.copyOfRange(formatted, 1, formatted.length);

                    // Matching to a command
                    AbstractCommand command;
                    if (COMMAND_MAP.containsKey(name)) {
                        command = COMMAND_MAP.get(name);
                    } else if (ALIAS_MAP.containsKey(name)) {
                        command = COMMAND_MAP.get(ALIAS_MAP.get(name));
                    } else {
                        return;
                    }

                    // Process permissions
                    EnumSet<Permission> botPermissions = Objects.requireNonNull(queuedMessageReceivedEvent.getGuild().getMember(queuedMessageReceivedEvent.getJDA().getSelfUser())).getPermissions();
                    EnumSet<Permission> userPermissions = Objects.requireNonNull(queuedMessageReceivedEvent.getMember()).getPermissions();

                    Permission[] commandPermissions = command.getPermissions();
                    List<Permission> commandPermissionsList = Arrays.asList(commandPermissions);

                    boolean botHasRequiredPermissions = botPermissions.containsAll(commandPermissionsList);
                    boolean userHasRequiredPermissions = userPermissions.containsAll(commandPermissionsList);

                    // Permissions check
                    if (!botHasRequiredPermissions || !userHasRequiredPermissions) {
                        EmbedBuilder embedBuilder = EmbedUtil.errorMessage(queuedMessageReceivedEvent, "Missing Permissions",
                                                                           "The following permissions are required to run this command:");

                        for (Permission permission : commandPermissions) {
                            boolean botHasPermission = botPermissions.contains(permission);
                            boolean userHasPermission = userPermissions.contains(permission);

                            // Use integers as an error code
                            int permissionMode = 0;
                            if (!botHasPermission) permissionMode += 1;
                            if (!userHasPermission) permissionMode += 2;

                            // Match error code a description
                            String permissionIndicator;
                            switch (permissionMode) {
                                case 1:
                                    permissionIndicator = "Bot";
                                    break;
                                case 2:
                                    permissionIndicator = "User";
                                    break;
                                case 3:
                                    permissionIndicator = "Bot & User";
                                    break;
                                default:
                                    throw new IllegalStateException("Unexpected value: " + permissionMode);
                            }

                            embedBuilder.addField(permission.getName(), "Who: " + permissionIndicator, true);
                        }

                        queuedMessageReceivedEvent.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
                        return;
                    }

                    // NSFW check
                    if (command.isNSFW() && !queuedMessageReceivedEvent.getTextChannel().isNSFW()) {
                        EmbedBuilder embedBuilder = EmbedUtil.errorMessage(queuedMessageReceivedEvent, "NSFW Channel",
                                                                           "This command must be ran in an NSFW channel");

                        // Image from Discord's guide on NSFW channels
                        embedBuilder.setImage("https://support.discord.com/hc/article_attachments/360007795191/2_.jpg");

                        queuedMessageReceivedEvent.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
                        return;
                    }

                    String tag = queuedMessageReceivedEvent.getAuthor().getAsTag();
                    String flatArgs = String.join(", ", args);

                    Exception exception = null;
                    try {
                        // Command execution
                        command.execute(queuedMessageReceivedEvent, args);
                    } catch (Exception e) {
                        exception = e;
                    } finally {
                        if (exception == null) {
                            LOGGER.info("{} executed \"{}\" with arguments \"{}\"", tag, name, flatArgs);
                        } else {
                            LOGGER.error("{} failed \"{}\" with arguments \"{}\" and exception \"{}\"", tag, name, flatArgs, exception);

                            EmbedBuilder errorMessage = EmbedUtil.errorMessage(queuedMessageReceivedEvent, "Command Execution", exception.getMessage());
                            queuedMessageReceivedEvent.getChannel().sendMessageEmbeds(errorMessage.build()).queue();
                        }
                    }

                    queue.remove();
                } while (queue.size() > 0);

                userQueueMap.remove(messageReceivedEvent.getAuthor().getIdLong());
            });
        }
    }
}
