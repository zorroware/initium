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

import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

/**
 * Displays bot information on startup.
 */
public class ReadyListener implements EventListener {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onEvent(@Nonnull GenericEvent event) {
        if (!(event instanceof ReadyEvent readyEvent)) return;

        String textBorder = "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~";

        LOGGER.info(textBorder);
        LOGGER.info("Bot Information");
        LOGGER.info(textBorder);
        LOGGER.info("Bot Account: " + readyEvent.getJDA().getSelfUser().getName());
        LOGGER.info("JDA Version: " + JDAInfo.VERSION);
        LOGGER.info("Java Runtime: " + Runtime.version().toString());
        LOGGER.info("Maximum Heap Size: " + (Runtime.getRuntime().maxMemory() / 1048576) + " MiB");
        LOGGER.info(textBorder);
        LOGGER.info("Guild Count: " + readyEvent.getJDA().getGuilds().size());
        LOGGER.info(textBorder);

        readyEvent.getJDA().removeEventListener(this); // ReadyEvent only fires once
    }
}
