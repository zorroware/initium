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

package com.github.zorroware.initium.tasks;

/**
 * Acts as a solution to {@link java.util.Timer} tasks not running as soon as they are initialized.
 * Extending this class is helpful even if the task doesn't need to run on start.
 */
public abstract class ImmediateTask implements Runnable {
    /**
     * The same as {@link Runnable}'s {@code run} method but returning the runnable.
     */
    public Runnable runFirstTime() {
        run();
        return this;
    }
}
