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
