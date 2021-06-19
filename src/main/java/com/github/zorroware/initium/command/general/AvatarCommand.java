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

import com.github.zorroware.initium.util.EmbedUtil;
import com.github.zorroware.initium.command.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.io.FilenameUtils;

import java.util.ArrayList;
import java.util.List;

public class AvatarCommand extends Command {
    private static final String SIZE_SUFFIX = "?size=1024"; // This will be appended to the direct URL of the avatar

    @Override
    public void execute(MessageReceivedEvent messageReceivedEvent, String[] args, CommandLine cmd, String[] filteredArgs) {
        List<User> mentionedUsers = messageReceivedEvent.getMessage().getMentionedUsers();
        User target;

        if (args.length == 0) { // if (no args provided)
            target = messageReceivedEvent.getAuthor();
        } else if (mentionedUsers.size() != 0) { // else if (message has a valid user mention)
            target = mentionedUsers.get(0);
        } else {
            throw new IllegalArgumentException("No user was mentioned");
        }

        EmbedBuilder embedBuilder = EmbedUtil.embedModel(messageReceivedEvent);
        embedBuilder.setTitle(target.getName() + "'s Avatar");

        String url = target.getEffectiveAvatarUrl(); // Raw avatar URL
        String cleanedUrl = FilenameUtils.removeExtension(target.getEffectiveAvatarUrl()); // Prune the file extension

        List<String> formats = new ArrayList<>();
        formats.add(String.format("[[png]](%s)", cleanedUrl + ".png" + SIZE_SUFFIX)); // PNG
        formats.add(String.format("[[jpg]](%s)", cleanedUrl + ".jpg" + SIZE_SUFFIX)); // JPG
        if (url.endsWith(".gif")) formats.add(String.format("[[gif]](%s)", cleanedUrl + ".gif" + SIZE_SUFFIX)); // GIF (if avatar is animated)
        formats.add(String.format("[[webp]](%s)", cleanedUrl + ".webp" + SIZE_SUFFIX)); // WEBP

        embedBuilder.setDescription(String.join(" ", formats)); // Assemble links into one string

        embedBuilder.setImage(url + SIZE_SUFFIX);
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
