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

package com.github.zorroware.initium.listeners;

import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Displays bot information on startup.
 */
public class ReadyListener implements EventListener {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @SneakyThrows
    @Override
    public void onEvent(@Nonnull GenericEvent event) {
        if (!(event instanceof ReadyEvent readyEvent)) return;
        readyEvent.getJDA().removeEventListener(this); // ReadyEvent only fires once

        // Parse current release build from Maven metadata
        String availableVersion = new Scanner(new URL("https://m2.dv8tion.net/releases/net/dv8tion/JDA/maven-metadata.xml").openStream(),StandardCharsets.UTF_8)
                .useDelimiter("\\A")
                .next()
                .split("<release>")[1]
                .split("</release>")[0];

        String currentVersion = JDAInfo.VERSION;

        int availableBuild = Integer.parseInt(availableVersion.split("_")[1]);
        int currentBuild = Integer.parseInt(JDAInfo.VERSION_BUILD);

        if (availableBuild > currentBuild) {
            String message = String.format("Current: %s, Latest: %s", currentVersion, availableVersion);
            if (availableBuild - currentBuild < 15) {
                LOGGER.info("There's a new build of JDA available! " + message);
            } else {
                LOGGER.warn("You're running an old version of JDA! " + message);
            }
        }
    }
}
