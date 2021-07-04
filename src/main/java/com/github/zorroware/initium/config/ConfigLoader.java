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

package com.github.zorroware.initium.config;

import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Assembles an instance of {@link ConfigSchema} from a configuration file.
 */
public class ConfigLoader {
    private final Path CONFIG_PATH;

    /**
     * @param filename path of config file
     */
    public ConfigLoader(String filename) {
        this.CONFIG_PATH = Paths.get(filename);
    }

    /**
     * @return assembled {@link ConfigSchema} object from the contents of the config file
     */
    public ConfigSchema load() throws IOException {
        TomlParseResult tomlParseResult = Toml.parse(CONFIG_PATH);

        ConfigSchema configSchema = new ConfigSchema();
        configSchema.setPrefix(tomlParseResult.getString("prefix"));
        configSchema.setToken(tomlParseResult.getString("token"));
        return configSchema;
    }
}
