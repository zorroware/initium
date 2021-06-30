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

package com.github.zorroware.initium.command.general;

import com.github.zorroware.initium.command.Command;
import com.github.zorroware.initium.util.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.io.FilenameUtils;

import java.util.ArrayList;
import java.util.List;

public class AvatarCommand extends Command {
    @Override
    public void execute(MessageReceivedEvent messageReceivedEvent, String[] args) {
        List<User> mentionedUsers = messageReceivedEvent.getMessage().getMentionedUsers();
        String sizeSuffix = "?size=1024";
        User target;

        if (args.length == 0) {
            target = messageReceivedEvent.getAuthor();
        } else if (mentionedUsers.size() != 0) {
            target = mentionedUsers.get(0);
        } else {
            throw new IllegalArgumentException("No user mentioned");
        }

        EmbedBuilder embedBuilder = EmbedUtil.embedModel(messageReceivedEvent);
        embedBuilder.setTitle(target.getName() + "'s Avatar");

        String url = target.getEffectiveAvatarUrl(); // Raw avatar URL
        String cleanedUrl = FilenameUtils.removeExtension(target.getEffectiveAvatarUrl()); // Prune the file extension

        List<String> formats = new ArrayList<>();
        formats.add(String.format("[[png]](%s)", cleanedUrl + ".png" + sizeSuffix)); // PNG
        formats.add(String.format("[[jpg]](%s)", cleanedUrl + ".jpg" + sizeSuffix)); // JPG
        if (url.endsWith(".gif")) formats.add(String.format("[[gif]](%s)", cleanedUrl + ".gif" + sizeSuffix)); // GIF (if avatar is animated)
        formats.add(String.format("[[webp]](%s)", cleanedUrl + ".webp" + sizeSuffix)); // WEBP

        embedBuilder.setDescription(String.join(" ", formats)); // Assemble links into one string

        embedBuilder.setImage(url + sizeSuffix);
        embedBuilder.setColor(0x6600bb);

        messageReceivedEvent.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
    }

    @Override
    public String getDescription() {
        return "Displays the avatar of a user, or yourself if no user is mentioned.";
    }

    @Override
    public String getUsage() {
        return "[<user>]";
    }

    @Override
    public String[] getAliases() {
        return new String[] { "av", "pfp" };
    }
}
