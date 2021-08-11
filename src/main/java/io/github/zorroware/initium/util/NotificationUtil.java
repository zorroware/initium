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

package io.github.zorroware.initium.util;

import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDAInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Scanner;

/**
 * Provides methods for displaying important messages to the log.
 */
public class NotificationUtil {
    // Logger
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationUtil.class);

    /**
     * Checks the JDA Maven repository for updates to JDA, and displays a message if an update is found.
     * The message content and type changes depending on how outdated the current JDA version is.
     */
    @SneakyThrows
    public static void handleUpdateNotification() {
        // Parse current release build from Maven metadata
        String availableVersion = new Scanner(new URL("https://m2.dv8tion.net/releases/net/dv8tion/JDA/maven-metadata.xml").openStream())
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
