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
