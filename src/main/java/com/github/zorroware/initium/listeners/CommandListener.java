/*
 * Copyright (c) 2021 Zorroware
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or distribute this
 * software and associated documentation files (the "Software"), either in source
 * code form or as a compiled binary, for any purpose, commercial or
 * non-commercial, and by any means.
 *
 * The copyright notices in the Software and this entire statement, including the
 * above license grant, this restriction and the following disclaimer, must be
 * included in all distributions of the Software, in whole or in part, and in any
 * form.
 *
 * THE SOFTWARE DISTRIBUTED UNDER THIS LICENSE IS DISTRIBUTED ON AN "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, EITHER EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.zorroware.initium.listeners;

import com.github.zorroware.initium.Initium;
import com.github.zorroware.initium.command.Command;
import com.github.zorroware.initium.config.ConfigSchema;
import com.github.zorroware.initium.util.EmbedUtil;
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

/**
 * A message listener that handles processing and executing commands.
 */
public class CommandListener implements EventListener {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private static final ConfigSchema CONFIG = Initium.config;
    private static final Map<String, Command> COMMANDS = Initium.COMMANDS;
    private static final Map<String, String> ALIASES = Initium.ALIASES;

    private static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();

    @Override
    public void onEvent(@Nonnull GenericEvent event) {
        if (!(event instanceof MessageReceivedEvent messageReceivedEvent)) return;

        // These checks go first because they're most likely to fail, saving processing power
        if (!messageReceivedEvent.getMessage().getContentRaw().startsWith(CONFIG.getPrefix())) return;
        if (messageReceivedEvent.getChannelType().equals(ChannelType.PRIVATE)) return;
        if (messageReceivedEvent.getAuthor().isBot()) return;

        // Submit to thread pool for command processing
        THREAD_POOL.submit(() -> {
            // Command parsing
            String[] formatted = messageReceivedEvent.getMessage().getContentRaw().substring(CONFIG.getPrefix().length()).split(" ");
            String name = formatted[0];
            String[] args = Arrays.copyOfRange(formatted, 1, formatted.length);

            // Getting command object
            Command command;
            if (COMMANDS.containsKey(name)) {
                command = COMMANDS.get(name);
            } else if (ALIASES.containsKey(name)) {
                command = COMMANDS.get(ALIASES.get(name));
            } else {
                return;
            }

            // Permissions
            EnumSet<Permission> botPermissions = Objects.requireNonNull(messageReceivedEvent.getGuild().getMember(messageReceivedEvent.getJDA().getSelfUser())).getPermissions();
            EnumSet<Permission> userPermissions = Objects.requireNonNull(messageReceivedEvent.getMember()).getPermissions();
            Permission[] commandPermissions = command.getPermissions();
            List<Permission> commandPermissionsList = Arrays.asList(commandPermissions);

            boolean botHasRequiredPermissions = botPermissions.containsAll(commandPermissionsList);
            boolean userHasRequiredPermissions = userPermissions.containsAll(commandPermissionsList);

            // Permissions check
            if (!botHasRequiredPermissions || !userHasRequiredPermissions) {
                EmbedBuilder embedBuilder = EmbedUtil.embedModel(messageReceivedEvent);

                embedBuilder.setTitle("Missing Permissions");
                embedBuilder.setDescription("The following permissions are needed to run this command:");

                for (Permission permission : commandPermissions) {
                    boolean botHasPermission = botPermissions.contains(permission);
                    boolean userHasPermission = userPermissions.contains(permission);

                    int permissionMode = 0;
                    if (!botHasPermission) permissionMode += 1;
                    if (!userHasPermission) permissionMode += 2;

                    String permissionIndicator = switch (permissionMode) {
                        case 1 -> "Bot";
                        case 2 -> "User";
                        case 3 -> "Bot & User";
                        default -> throw new IllegalStateException("Unexpected value: " + permissionMode);
                    };

                    embedBuilder.addField(permission.getName(), "Who: " + permissionIndicator, true);
                }

                messageReceivedEvent.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
                return;
            }

            // NSFW check
            if (command.isNSFW() && !messageReceivedEvent.getTextChannel().isNSFW()) {
                EmbedBuilder embedBuilder = EmbedUtil.embedModel(messageReceivedEvent);

                embedBuilder.setTitle("NSFW Channel Required");
                embedBuilder.setDescription("This command must be ran in an NSFW channel.");
                embedBuilder.setImage("https://support.discord.com/hc/article_attachments/360007795191/2_.jpg"); // Image from Discord's guide on NSFW channels
                embedBuilder.setColor(0xff0000);

                messageReceivedEvent.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
                return;
            }

            String tag = messageReceivedEvent.getAuthor().getAsTag();
            String flatArgs = Arrays.toString(args);

            // Command execution
            try {
                command.execute(messageReceivedEvent, args);
            } catch (Exception e) {
                LOGGER.error(String.format("%s failed '%s' with arguments '%s' and exception '%s'", tag, name, flatArgs, e));

                // Dispatch error message
                EmbedBuilder errorMessage = EmbedUtil.errorMessage(messageReceivedEvent, "Command Execution", e.getMessage());
                messageReceivedEvent.getChannel().sendMessageEmbeds(errorMessage.build()).queue();
                return;
            }

            LOGGER.info(String.format("%s executed '%s' with arguments '%s'", tag, name, flatArgs));
        });
    }
}
