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

package io.github.zorroware.initium.config;

import lombok.Getter;
import lombok.Setter;
import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A class of variables used for bot configuration
 */
@Getter @Setter
public class Config {
    private String prefix;
    private String token;

    /**
     * @return new instance of {@link Config} from file contents
     */
    public static Config load(String filename) throws IOException {
        Path path = Paths.get(filename);
        TomlParseResult tomlParseResult = Toml.parse(path);

        Config config = new Config();
        config.setPrefix(tomlParseResult.getString("prefix"));
        config.setToken (tomlParseResult.getString("token"));
        return config;
    }
}
