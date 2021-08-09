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
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.io.FilenameUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AvatarCommand extends AbstractCommand {
    // Image URL suffix
    private static final String SUFFIX = "?size=1024";

    @Override
    public void execute(MessageReceivedEvent messageReceivedEvent, String[] args) {
        User target;  // The user we're displaying the avatar of
        User mentionedUser;

        if (args.length == 0) {  // If no argument provided, the target is the author
            target = messageReceivedEvent.getAuthor();
        } else if ((mentionedUser = messageReceivedEvent.getMessage().getMentionedUsers().get(0)) != null) {  // Check for mentioned users
            target = mentionedUser;
        } else {
            throw new IllegalArgumentException("User not found");
        }

        EmbedBuilder embedBuilder = EmbedUtil.embedModel(messageReceivedEvent);
        embedBuilder.setTitle(target.getName() + "'s Avatar");

        String url = target.getEffectiveAvatarUrl();  // Raw avatar URL
        String cleanedUrl = FilenameUtils.removeExtension(target.getEffectiveAvatarUrl());  // Prune the file extension

        List<String> formats = new ArrayList<>();
        formats.add(String.format("[[jpg]](%s)",  cleanedUrl + ".jpg"  + SUFFIX));  // JPG
        formats.add(String.format("[[png]](%s)",  cleanedUrl + ".png"  + SUFFIX));  // PNG
        formats.add(String.format("[[webp]](%s)", cleanedUrl + ".webp" + SUFFIX));  // WEBP
        if (url.endsWith(".gif")) formats.add(String.format("[[gif]](%s)", cleanedUrl + ".gif" + SUFFIX));  // GIF (if avatar is animated)

        embedBuilder.setDescription(String.join(" ", formats));  // Assemble links into one string

        embedBuilder.setImage(url + SUFFIX);
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
