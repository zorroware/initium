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

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("application")
    id("java")

    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("io.freefair.lombok") version "6.0.0-m2"
}

repositories {
    maven("https://m2.dv8tion.net/releases")
    mavenCentral()
}

dependencies {
    // JDA
    implementation(group = "net.dv8tion",              name = "JDA",                version = "4.3.0_280") { exclude(module = "opus-java") }

    // General Libraries
    implementation(group = "com.google.guava",         name = "guava",              version = "30.1.1-jre")
    implementation(group = "commons-cli",              name = "commons-cli",        version = "1.4")
    implementation(group = "commons-io",               name = "commons-io",         version = "2.10.0")
    implementation(group = "org.tomlj",                name = "tomlj",              version = "1.0.0")

    // Backend Libraries
    implementation(group = "org.apache.logging.log4j", name = "log4j-api",          version = "2.14.1")
    implementation(group = "org.apache.logging.log4j", name = "log4j-core",         version = "2.14.1")
    implementation(group = "org.apache.logging.log4j", name = "log4j-slf4j18-impl", version = "2.14.1")
    implementation(group = "org.slf4j",                name = "slf4j-api",          version = "1.7.30")
}

application {
    mainClass.set("com.github.zorroware.initium.Initium")
}

java {
    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_16
}

tasks {
    withType<AbstractArchiveTask>().configureEach {
        from("LICENSE") // Include license within every jar

        // Reproducible builds
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
    }

    withType<Jar>().configureEach {
        manifest.attributes["Multi-Release"] = true
    }

    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }

    withType<ShadowJar>().configureEach {
        minimize {
            exclude(dependency("org.apache.logging.log4j:.*:.*")) // Log4J is broken by minimization due to its heavy usage of reflection
        }
    }
}
