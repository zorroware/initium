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

package com.github.zorroware.initium.command.help;

import com.github.zorroware.initium.Initium;
import com.github.zorroware.initium.command.Command;
import com.github.zorroware.initium.command.CommandGroup;
import com.github.zorroware.initium.config.ConfigSchema;
import com.github.zorroware.initium.util.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.cli.CommandLine;

import java.util.*;

public class HelpCommand extends Command {
    private static final ConfigSchema CONFIG = Initium.config;
    private static final Map<String, Command> COMMANDS = Initium.COMMANDS;

    @Override
    public void execute(MessageReceivedEvent messageReceivedEvent, String[] args, CommandLine cmd, String[] filteredArgs) {
        EmbedBuilder embedBuilder = EmbedUtil.embedModel(messageReceivedEvent);

        if (filteredArgs.length == 0) {
            embedBuilder.setTitle("Command Help");
            embedBuilder.setDescription(String.format("To get information on a specific command, run `%shelp %s`", CONFIG.getPrefix(), getUsage()));

            for (CommandGroup commandGroup : CommandGroup.values()) {
                ArrayList<String> commandsList = new ArrayList<>();

                for (Map.Entry<String, Command> entry : COMMANDS.entrySet()) {
                    Command command = entry.getValue();
                    if (command.getCommandGroup() == commandGroup && !command.isHidden()) {
                        commandsList.add(entry.getKey());
                    }
                }

                embedBuilder.addField(commandGroup.toString() + " Commands", String.join(" • ", commandsList), false);
            }

            embedBuilder.setColor(0x4444ff);
        } else {
            String commandName = filteredArgs[0]; // Get the command name from the first argument

            if (COMMANDS.containsKey(commandName)) {
                Command command = COMMANDS.get(commandName);

                embedBuilder.setTitle("Command Info • " + commandName);
                embedBuilder.addField("Description", command.getDescription() == null ? "No description provided." : command.getDescription(), false);

                StringBuilder usage = new StringBuilder();
                usage.append(CONFIG.getPrefix()); // Prefix
                usage.append(commandName); // Name
                if (command.getUsage() != null) usage.append(' ').append(command.getUsage()); // Arguments

                embedBuilder.addField("Usage", usage.toString(), false);

                StringBuilder aliases = new StringBuilder();

                Iterator<String> iterator = Arrays.stream(command.getAliases()).iterator();
                while (iterator.hasNext()) {
                    aliases.append('`').append(iterator.next()).append('`');
                    if (iterator.hasNext()) {
                        aliases.append(", ");
                    }
                }

                embedBuilder.addField("Aliases", aliases.toString(), false);
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
