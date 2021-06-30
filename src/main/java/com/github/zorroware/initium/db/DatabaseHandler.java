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

package com.github.zorroware.initium.db;

import com.github.zorroware.initium.Initium;
import com.github.zorroware.initium.db.schema.GuildSchema;
import com.github.zorroware.initium.db.schema.UserSchema;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.Cursor;
import org.dizitart.no2.objects.ObjectRepository;

import static org.dizitart.no2.objects.filters.ObjectFilters.eq;

/**
 * Provides abstraction for the Nitrite database.
 */
public class DatabaseHandler {
    private static final Nitrite DB = Initium.db;

    @Getter
    private static final ObjectRepository<UserSchema> userRepository = DB.getRepository(UserSchema.class);
    @Getter
    private static final ObjectRepository<GuildSchema> guildRepository = DB.getRepository(GuildSchema.class);

    /**
     * Searches the database for a user. If the user is not found, a new one is created.
     *
     * @param user the {@link User} to process in the database
     * @return processed {@link UserSchema}
     */
    public static UserSchema processUser(User user) {
        Cursor<UserSchema> cursor = userRepository.find(eq("userId", user.getId()));

        UserSchema userSchema;
        if (cursor.size() == 0) {
            userSchema = new UserSchema();
            userSchema.setUserId(user.getId());

            userRepository.insert(userSchema);
        } else {
            userSchema = cursor.firstOrDefault();
        }

        return userSchema;
    }

    /**
     * Searches the database for a user. If the user is not found, a new one is created.
     *
     * @param guild the {@link Guild} to process in the database
     * @return processed {@link GuildSchema}
     */
    public static GuildSchema processGuild(Guild guild) {
        Cursor<GuildSchema> cursor = guildRepository.find(eq("guildId", guild.getId()));

        GuildSchema guildSchema;
        if (cursor.size() == 0) {
            guildSchema = new GuildSchema();
            guildSchema.setGuildId(guild.getId());

            guildRepository.insert(guildSchema);
        } else {
            guildSchema = cursor.firstOrDefault();
        }

        return guildSchema;
    }
}
