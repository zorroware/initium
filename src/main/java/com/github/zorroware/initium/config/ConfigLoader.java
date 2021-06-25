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

package com.github.zorroware.initium.config;

import lombok.SneakyThrows;
import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

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
        configSchema.setName(tomlParseResult.getString("name"));
        configSchema.setPrefix(tomlParseResult.getString("prefix"));
        configSchema.setToken(tomlParseResult.getString("token"));

        configSchema.setParsingThreads(Math.toIntExact(Objects.requireNonNull(tomlParseResult.getLong("parsing_threads"))));

        return configSchema;
    }
}
