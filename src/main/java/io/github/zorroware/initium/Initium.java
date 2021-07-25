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

package io.github.zorroware.initium;

import io.github.zorroware.initium.command.AbstractCommand;
import io.github.zorroware.initium.command.general.AvatarCommand;
import io.github.zorroware.initium.command.general.PingCommand;
import io.github.zorroware.initium.command.help.HelpCommand;
import io.github.zorroware.initium.command.moderation.KickCommand;
import io.github.zorroware.initium.config.Config;
import io.github.zorroware.initium.listeners.CommandListener;
import io.github.zorroware.initium.tasks.StatusTask;
import io.github.zorroware.initium.util.NotificationUtil;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.Getter;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The main class for the bot.
 */
public class Initium {
    // Core
    private static @Getter Config config;
    private static @Getter JDA jda;

    // Commands
    private static final @Getter Map<String, AbstractCommand> commandMap = new Object2ObjectOpenHashMap<>();
    private static final @Getter Map<String, String> aliasMap = new Object2ObjectOpenHashMap<>();

    // Tasks
    private static final @Getter ScheduledExecutorService taskService = Executors.newSingleThreadScheduledExecutor();

    @SneakyThrows
    public static void main(String[] args) {
        config = Config.load("config.toml");
        jda = JDABuilder.createDefault(config.getToken()).build();

        jda.awaitReady();
        registerCommands();
        registerListeners();
        registerTasks();

        NotificationUtil.handleUpdateNotification();
    }

    /**
     * Loads commands into the command map and loads aliases into the alias map
     */
    private static void registerCommands() {
        // General
        commandMap.put("avatar", new AvatarCommand());
        commandMap.put("ping",   new PingCommand());

        // Help
        commandMap.put("help",   new HelpCommand());

        // Moderation
        commandMap.put("kick",   new KickCommand());

        // Aliases
        for (String command : commandMap.keySet()) {
            for (String alias : commandMap.get(command).getAliases()) {
                aliasMap.put(alias, command);
            }
        }
    }

    /**
     * Loads listeners into the JDA
     */
    private static void registerListeners() {
        jda.addEventListener(new CommandListener());
    }

    /**
     * Schedules tasks in a {@link ScheduledExecutorService}
     */
    private static void registerTasks() {
        taskService.scheduleAtFixedRate(new StatusTask(jda.getPresence()), 0, 30, TimeUnit.SECONDS);
    }
}
