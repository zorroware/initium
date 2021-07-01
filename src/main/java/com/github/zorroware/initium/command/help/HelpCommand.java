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

package com.github.zorroware.initium.command.help;

import com.github.zorroware.initium.Initium;
import com.github.zorroware.initium.command.AbstractCommand;
import com.github.zorroware.initium.command.CommandGroup;
import com.github.zorroware.initium.config.ConfigSchema;
import com.github.zorroware.initium.util.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.*;

public class HelpCommand extends AbstractCommand {
    private static final ConfigSchema CONFIG = Initium.config;
    private static final Map<String, AbstractCommand> COMMANDS = Initium.COMMANDS;

    @Override
    public void execute(MessageReceivedEvent messageReceivedEvent, String[] args) {
        EmbedBuilder embedBuilder = EmbedUtil.embedModel(messageReceivedEvent);

        if (args.length == 0) {
            embedBuilder.setTitle("Command Help");
            embedBuilder.setDescription(String.format("To get information on a specific command, run `%shelp %s`", CONFIG.getPrefix(), getUsage()));

            for (CommandGroup commandGroup : CommandGroup.values()) {
                List<String> commandsList = new ArrayList<>();

                COMMANDS.forEach((name, command) -> {
                    if (command.getCommandGroup() == commandGroup && !command.isHidden()) {
                        commandsList.add(name);
                    }
                });

                embedBuilder.addField(commandGroup.toString() + " Commands", String.join(" • ", commandsList), false);
            }

            embedBuilder.setColor(0x4444ff);
        } else {
            String commandName = args[0];

            if (COMMANDS.containsKey(commandName)) {
                AbstractCommand command = COMMANDS.get(commandName);

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
                Iterator<String> iterator = Arrays.stream(command.getAliases()).iterator();
                while (iterator.hasNext()) {
                    aliasesStringBuilder.append('`').append(iterator.next()).append('`');
                    if (iterator.hasNext()) {
                        aliasesStringBuilder.append(", ");
                    }
                }
                embedBuilder.addField("Aliases", aliasesStringBuilder.toString(), false);

                embedBuilder.setColor(0x00ccdd);
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
