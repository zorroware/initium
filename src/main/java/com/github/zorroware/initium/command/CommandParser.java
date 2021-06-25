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

package com.github.zorroware.initium.command;

import com.github.zorroware.initium.Initium;
import com.github.zorroware.initium.config.ConfigSchema;
import lombok.Getter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.Map;

/**
 * A parser for commands.
 */
public class CommandParser {
    private static final ConfigSchema CONFIG = Initium.config;
    private static final Map<String, Command> COMMANDS = Initium.COMMANDS;
    private static final Map<String, String> ALIASES = Initium.ALIASES;

    /**
     * A class containing various data for commands.
     */
    @Getter
    @SuppressWarnings("ClassCanBeRecord")
    public static class CommandData {
        private final String name;
        private final Command command;
        private final String[] args;

        /**
         * @param name          name of the command
         * @param command       {@link Command} object
         * @param args          any additional arguments passed to the command
         */
        public CommandData(String name, Command command, String[] args) {
            this.name = name;
            this.command = command;
            this.args = args;
        }
    }

    /**
     * Parses command data from a {@link MessageReceivedEvent}.
     * @param messageReceivedEvent {@link MessageReceivedEvent} instance
     * @return {@link CommandData} instance
     */
    public static CommandData parseData(MessageReceivedEvent messageReceivedEvent) {
        String raw = messageReceivedEvent.getMessage().getContentRaw();
        String[] formatted = raw.substring(CONFIG.getPrefix().length()).split(" ");
        String name = formatted[0];

        Command command;
        if (COMMANDS.containsKey(name)) {
            command = COMMANDS.get(name);
        } else if (ALIASES.containsKey(name)) {
            command = COMMANDS.get(ALIASES.get(name));
        } else {
            throw new IllegalArgumentException();
        }

        String[] args = Arrays.copyOfRange(formatted, 1, formatted.length);

        return new CommandData(name, command, args);
    }
}
