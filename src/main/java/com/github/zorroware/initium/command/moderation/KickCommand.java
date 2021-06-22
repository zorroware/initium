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

package com.github.zorroware.initium.command.moderation;

import com.github.zorroware.initium.command.Command;
import com.github.zorroware.initium.command.CommandGroup;
import com.github.zorroware.initium.util.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;

public class KickCommand extends Command {
    @Override
    public void execute(MessageReceivedEvent messageReceivedEvent, String[] args) {
        if (messageReceivedEvent.getMessage().getMentionedUsers().isEmpty()) throw new IllegalArgumentException("No users mentioned");

        Member target = messageReceivedEvent.getMessage().getMentionedMembers().get(0);
        String reason = args.length <= 1 ? "No reason provided." : String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        messageReceivedEvent.getGuild().kick(target, reason).queue();

        EmbedBuilder embedBuilder = EmbedUtil.embedModel(messageReceivedEvent);
        embedBuilder.setTitle(String.format("%s has been kicked by %s", target.getUser().getName(), messageReceivedEvent.getAuthor().getName()));
        embedBuilder.setDescription("Reason: " + reason);
        embedBuilder.setColor(0x00ee99);

        messageReceivedEvent.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
    }

    @Override
    public String getDescription() {
        return "Kick a user from the guild.";
    }

    @Override
    public String getUsage() {
        return "<user> [<reason>]";
    }

    @Override
    public CommandGroup getCommandGroup() {
        return CommandGroup.MODERATION;
    }

    @Override
    public Permission[] getPermissions() {
        return new Permission[] { Permission.KICK_MEMBERS };
    }
}
