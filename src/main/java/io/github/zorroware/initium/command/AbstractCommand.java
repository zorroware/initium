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

package io.github.zorroware.initium.command;

import lombok.Getter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * An abstract class representing a full command.
 */
public abstract class AbstractCommand {
    public abstract void execute(MessageReceivedEvent messageReceivedEvent, String[] args);

    public final @Getter String description = null;
    public final @Getter String usage = null;
    public final @Getter String[] aliases = new String[0];
    public final @Getter CommandGroup commandGroup = CommandGroup.GENERAL;
    public final @Getter boolean isHidden = false;
    public final @Getter boolean isNSFW = false;
    public final @Getter Permission[] permissions = Permission.EMPTY_PERMISSIONS;
}
