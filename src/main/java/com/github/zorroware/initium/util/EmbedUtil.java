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

package com.github.zorroware.initium.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.Instant;

/**
 * Utility to simplify creating frequently-used embeds.
 */
public class EmbedUtil {
    /**
     * Preconfigured {@link EmbedBuilder} that comes with an author and a timestamp.
     * @param messageReceivedEvent {@link MessageReceivedEvent} object
     * @return preconfigured {@link EmbedBuilder}
     */
    public static EmbedBuilder embedModel(MessageReceivedEvent messageReceivedEvent) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(messageReceivedEvent.getAuthor().getName(), null, messageReceivedEvent.getAuthor().getAvatarUrl());
        embedBuilder.setFooter("Powered by Initium");
        embedBuilder.setTimestamp(Instant.now());

        return embedBuilder;
    }

    /**
     * Generic {@link EmbedBuilder} for quick deployment of error messages.
     * @param messageReceivedEvent {@link MessageReceivedEvent} object
     * @param errorType type of error
     * @param errorMessage message to display
     * @return preconfigured {@link EmbedBuilder} for error message deployment
     */
    public static EmbedBuilder errorMessage(MessageReceivedEvent messageReceivedEvent, String errorType, String errorMessage) {
        EmbedBuilder embedBuilder = embedModel(messageReceivedEvent);
        embedBuilder.setTitle(errorType + " Error");
        embedBuilder.setDescription(errorMessage);
        embedBuilder.setColor(0xff0000);

        return embedBuilder;
    }
}
