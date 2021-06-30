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
import org.dizitart.no2.Nitrite;

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
    public static Nitrite db;
    public static final Map<String, Command> COMMANDS = new HashMap<>();
    public static final Map<String, String> ALIASES = new HashMap<>();
    public static final Timer TIMER = new Timer();

    public static void main(String[] args) throws IOException, LoginException {
        config = new ConfigLoader("config.toml").load();
        db = Nitrite.builder().filePath("database.db").compressed().openOrCreate();
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
