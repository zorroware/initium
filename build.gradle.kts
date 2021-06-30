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
    implementation(group = "net.dv8tion",              name = "JDA",                version = "4.3.0_282") { exclude(module = "opus-java") }

    // General Libraries
    implementation(group = "com.google.guava",         name = "guava",              version = "30.1.1-jre")
    implementation(group = "commons-io",               name = "commons-io",         version = "2.10.0")
    implementation(group = "org.tomlj",                name = "tomlj",              version = "1.0.0")

    // Backend Libraries
    implementation(group = "org.apache.logging.log4j", name = "log4j-api",          version = "2.14.1")
    implementation(group = "org.apache.logging.log4j", name = "log4j-core",         version = "2.14.1")
    implementation(group = "org.apache.logging.log4j", name = "log4j-slf4j18-impl", version = "2.14.1")
    implementation(group = "org.dizitart",             name = "nitrite",            version = "3.4.3")
    implementation(group = "org.slf4j",                name = "slf4j-api",          version = "1.7.31")
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

    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }

    jar {
        manifest.attributes["Multi-Release"] = true
    }

    shadowJar {
        minimize {
            exclude(dependency("org.apache.logging.log4j:.*:.*")) // Log4J is broken by minimization due to its heavy usage of reflection
        }
    }
}
