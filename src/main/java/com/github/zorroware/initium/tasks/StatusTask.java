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

package com.github.zorroware.initium.tasks;

import com.github.zorroware.initium.Initium;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.managers.Presence;

import java.util.TimerTask;

/**
 * A task that changes the status over time.
 */
public class StatusTask extends TimerTask {
    private static final String PREFIX = Initium.config.getPrefix();
    private static final Activity[] ACTIVITIES = {
            Activity.listening(PREFIX + "help"),
            Activity.watching("for commands"),
            Activity.playing("with JVM args")
    };
    private final Presence PRESENCE;

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
