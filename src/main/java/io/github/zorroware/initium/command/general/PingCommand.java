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

package io.github.zorroware.initium.command.general;

import io.github.zorroware.initium.command.AbstractCommand;
import io.github.zorroware.initium.util.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PingCommand extends AbstractCommand {
    @Override
    public void execute(MessageReceivedEvent messageReceivedEvent, String[] args) {
        // Begin latency measurement
        long start = System.currentTimeMillis();
        messageReceivedEvent.getChannel().sendMessage(":ping_pong: Ping Test").queue(message -> {
            // End latency measurement
            long end = System.currentTimeMillis();
            long apiLatency = end - start;

            EmbedBuilder pingEmbed = EmbedUtil.embedModel(messageReceivedEvent);
            pingEmbed.addField(":alarm_clock: API Latency", messageReceivedEvent.getJDA().getGatewayPing() + " ms", true);
            pingEmbed.addField(":robot: Client Latency", apiLatency + " ms", true);

            /*
             * Determine color for embed based on latency
             *
             * Less than 100    -> Green
             * Less than 200    -> Yellow
             * Greater than 200 -> Red
             */
            int color;
            if (apiLatency < 100) {
                color = 0x00ff00;
            } else if (apiLatency < 200) {
                color = 0xffff00;
            } else {
                color = 0xff0000;
            }
            pingEmbed.setColor(color);

            // Update the message with results
            message.editMessageEmbeds(pingEmbed.build()).queue();
        });
    }

    @Override
    public String getDescription() {
        return "Measures and displays API latency, client latency, and processing time.";
    }

    @Override
    public String[] getAliases() {
        return new String[] { "lag", "latency" };
    }
}
