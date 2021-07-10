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

package io.github.zorroware.initium.tasks;

import io.github.zorroware.initium.Initium;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.managers.Presence;

/**
 * A task that changes the status over time.
 */
public class StatusTask implements Runnable {
    private static final String PREFIX = Initium.getConfig().getPrefix();
    private static final Activity[] ACTIVITIES = { Activity.listening(PREFIX + "help"),
                                                   Activity.watching("for commands") };
    private final Presence PRESENCE;

    // Current position in the list of statuses
    int index = 0;

    public StatusTask(Presence PRESENCE) {
        this.PRESENCE = PRESENCE;
    }

    @Override
    public void run() {
        PRESENCE.setActivity(ACTIVITIES[index]);
        index = (index + 1) % ACTIVITIES.length;
    }
}
