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

package com.github.zorroware.initium.command.general;

import com.github.zorroware.initium.command.Command;
import com.github.zorroware.initium.command.CommandMetadata;
import com.github.zorroware.initium.util.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.cli.CommandLine;

public class PingCommand extends Command {
    @Override
    public void execute(MessageReceivedEvent messageReceivedEvent, CommandMetadata metadata, String[] args, String name, CommandLine cmd, String[] filtered, String raw) {
        long start = System.currentTimeMillis();
        messageReceivedEvent.getChannel().sendMessage(":ping_pong: Ping Test").queue(message -> {
            long end = System.currentTimeMillis();
            long apiLatency = end - start;

            EmbedBuilder pingEmbed = EmbedUtil.embedModel(messageReceivedEvent);
            pingEmbed.addField(":alarm_clock: API Latency", messageReceivedEvent.getJDA().getGatewayPing() + " ms", true);
            pingEmbed.addField(":robot: Client Latency", apiLatency + " ms", true);
            pingEmbed.addField(":thought_balloon: Processing Time", metadata.getProcessingTime() + " ms", true);

            int color;
            if (apiLatency < 100) {
                color = 0x00ff00;
            } else if (apiLatency < 200) {
                color = 0xffff00;
            } else {
                color = 0xff0000;
            }
            pingEmbed.setColor(color);

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
