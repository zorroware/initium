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

package com.github.zorroware.initium;

import com.github.zorroware.initium.command.Command;
import com.github.zorroware.initium.command.general.AvatarCommand;
import com.github.zorroware.initium.command.general.PingCommand;
import com.github.zorroware.initium.command.help.HelpCommand;
import com.github.zorroware.initium.command.moderation.KickCommand;
import com.github.zorroware.initium.config.ConfigLoader;
import com.github.zorroware.initium.config.ConfigSchema;
import com.github.zorroware.initium.listeners.CommandListener;
import com.github.zorroware.initium.listeners.ReadyListener;
import com.github.zorroware.initium.tasks.StatusTask;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

/**
 * The main class for the bot.
 */
public class Initium {
    public static ConfigSchema config;
    public static JDA jda;
    public static final Map<String, Command> COMMANDS = new HashMap<>();
    public static final Map<String, String> ALIASES = new HashMap<>();
    public static final Timer TIMER = new Timer();

    public static void main(String[] args) throws IOException, LoginException {
        config = new ConfigLoader("config.toml").load();
        jda = JDABuilder.createDefault(config.getToken()).build();

        registerCommands();
        registerListeners();
        registerTasks();
    }

    private static void registerCommands() {
        // General
        COMMANDS.put("avatar", new AvatarCommand());
        COMMANDS.put("ping", new PingCommand());

        // Help
        COMMANDS.put("help", new HelpCommand());

        // Moderation
        COMMANDS.put("kick", new KickCommand());

        // Aliases
        for (String command : COMMANDS.keySet()) {
            for (String alias : COMMANDS.get(command).getAliases()) {
                ALIASES.put(alias, command);
            }
        }
    }

    private static void registerListeners() {
        jda.addEventListener(new CommandListener());
        jda.addEventListener(new ReadyListener());
    }

    private static void registerTasks() {
        TIMER.schedule(new StatusTask(jda.getPresence()), 0, 30000);
    }
}
