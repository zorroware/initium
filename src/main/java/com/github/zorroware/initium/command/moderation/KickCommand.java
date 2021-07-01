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

package com.github.zorroware.initium.command.moderation;

import com.github.zorroware.initium.command.AbstractCommand;
import com.github.zorroware.initium.command.CommandGroup;
import com.github.zorroware.initium.util.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;

public class KickCommand extends AbstractCommand {
    @Override
    public void execute(MessageReceivedEvent messageReceivedEvent, String[] args) {
        if (messageReceivedEvent.getMessage().getMentionedUsers().isEmpty()) throw new IllegalArgumentException("No user mentioned");

        Member target = messageReceivedEvent.getMessage().getMentionedMembers().get(0);
        String reason = args.length <= 1 ? "No reason provided" : String.join(" ", Arrays.copyOfRange(args, 1, args.length));

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
