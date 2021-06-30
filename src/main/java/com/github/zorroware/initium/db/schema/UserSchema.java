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

package com.github.zorroware.initium.db.schema;

import com.github.zorroware.initium.db.DatabaseHandler;
import lombok.Getter;
import lombok.Setter;
import org.dizitart.no2.objects.Id;

/**
 * A class representing a Discord user in the database.
 */
@Getter
@Setter
public class UserSchema {
    @Id
    String userId;

    /**
     * Saves a user to the database.
     * When performing any write operation, this must be called else the user will not save.
     */
    public void saveUser() {
        DatabaseHandler.getUserRepository().update(this);
    }
}