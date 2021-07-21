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

package io.github.zorroware.initium.command.help;

import io.github.zorroware.initium.Initium;
import io.github.zorroware.initium.command.AbstractCommand;
import io.github.zorroware.initium.command.CommandGroup;
import io.github.zorroware.initium.config.Config;
import io.github.zorroware.initium.util.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.*;

public class HelpCommand extends AbstractCommand {
    // References
    private static final Config CONFIG = Initium.getConfig();
    private static final Map<String, AbstractCommand> COMMAND_MAP = Initium.getCommandMap();

    @Override
    public void execute(MessageReceivedEvent messageReceivedEvent, String[] args) {
        EmbedBuilder embedBuilder = EmbedUtil.embedModel(messageReceivedEvent);

        if (args.length == 0) {  // Main help menu
            embedBuilder.setTitle("Command Help");
            embedBuilder.setDescription(String.format("To get information on a specific command, run `%shelp %s`", CONFIG.getPrefix(), getUsage()));

            for (CommandGroup commandGroup : CommandGroup.values()) {
                List<String> commandsList = new ArrayList<>();

                // Iterate through each command
                COMMAND_MAP.forEach((name, command) -> {
                    if (command.getCommandGroup() == commandGroup && !command.isHidden()) {
                        commandsList.add(name);
                    }
                });

                embedBuilder.addField(commandGroup.toString() + " Commands", String.join(" • ", commandsList), false);
            }

            embedBuilder.setColor(0x4444ff);
        } else {  // Individual command help
            String commandName = args[0];

            if (COMMAND_MAP.containsKey(commandName)) {
                AbstractCommand command = COMMAND_MAP.get(commandName);

                embedBuilder.setTitle("Command Info • " + commandName);
                embedBuilder.setDescription(command.getDescription() == null ? "No description provided." : command.getDescription());

                // Usage field
                StringBuilder usageStringBuilder = new StringBuilder();
                usageStringBuilder.append(CONFIG.getPrefix());
                usageStringBuilder.append(commandName);
                if (command.getUsage() != null) usageStringBuilder.append(' ').append(command.getUsage());
                embedBuilder.addField("Usage", usageStringBuilder.toString(), false);

                // Aliases field
                StringBuilder aliasesStringBuilder = new StringBuilder();
                if (command.getAliases().length > 0) {
                    Iterator<String> iterator = Arrays.stream(command.getAliases()).iterator();
                    while (iterator.hasNext()) {
                        aliasesStringBuilder.append('`').append(iterator.next()).append('`');
                        if (iterator.hasNext()) {
                            aliasesStringBuilder.append(", ");
                        }
                    }
                } else {
                    aliasesStringBuilder.append("No aliases");
                }

                embedBuilder.addField("Aliases", aliasesStringBuilder.toString(), false);

                // Permissions field
                StringBuilder permissionsStringBuilder = new StringBuilder();
                if (command.getPermissions().length > 0) {
                    for (Permission permission : command.getPermissions()) {
                        permissionsStringBuilder.append("• ").append(permission.getName()).append('\n');
                    }
                } else {
                    permissionsStringBuilder.append("No permissions required");
                }
                embedBuilder.addField("Permissions", permissionsStringBuilder.toString(), false);

                embedBuilder.setColor(0x22dd11);
            } else {
                throw new IllegalArgumentException("Unknown command");
            }
        }

        messageReceivedEvent.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
    }

    @Override
    public String getDescription() {
        return "Display information on commands.";
    }

    @Override
    public String getUsage() {
        return "[<command>]";
    }

    @Override
    public String[] getAliases() {
        return new String[] { "h" };
    }

    @Override
    public CommandGroup getCommandGroup() {
        return CommandGroup.HELP;
    }
}
