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

package com.github.zorroware.initium.command;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

/**
 * An abstract class representing a full command.
 */
public abstract class Command {
    public void execute(MessageReceivedEvent messageReceivedEvent, CommandMetadata metadata, String[] args, String name, CommandLine cmd, String[] filtered, String raw) {
    }

    public String getDescription() {
        return null;
    }

    public String getUsage() {
        return null;
    }

    public String[] getAliases() {
        return new String[0];
    }

    public CommandGroup getCommandGroup() {
        return CommandGroup.GENERAL;
    }

    public boolean isHidden() {
        return false;
    }

    public boolean isNSFW() {
        return false;
    }

    public Options getOptions(Options options) {
        return options;
    }

    public Permission[] getPermissions() {
        return Permission.EMPTY_PERMISSIONS;
    }
}
