# Initium
![License](https://img.shields.io/github/license/zorroware/initium?style=flat-square)
![Build Status](https://img.shields.io/github/workflow/status/zorroware/initium/Gradle%20CI?style=flat-square)

A high-performance Discord bot template written in Java 16 and JDA that focuses on speed, elegance, and ease of use

##### Credits
[Reperak](https://github.com/ReperakPro) - Main Developer

##### License
Initium is licensed under the GNU GPLv3. You can find it [here](LICENSE).
____

## Java 16 Disclaimer
As stated in the description, Initium is written in **Java 16**.
Many developers dislike developing for the latest Java, so we'd like to make this obvious now to prevent inconveniences.

## What's inside?
* A simple and performant command parser
  * Command aliases
  * Static permission requirements
* Asynchronous commands
  * Fast and safe multithreading
* Bleeding-edge software
  * The latest JDA, Gradle, and Java
* Clean build.gradle
  * Written using the [Kotlin DSL](https://docs.gradle.org/current/userguide/kotlin_dsl.html)
* Complete and stylish help command
  * Command categories
  * Hidden command support
* Fancy logging
  * Colored terminal output 
  * Rolling file output with compression
* Intuitive configuration system
  * Configuration done in the TOML language using [TomlJ](https://github.com/tomlj/tomlj)
* Task system
  * Easily define routines for the bot to perform asynchronously

## Help Wanted
If you have the technical knowledge to add these missing features, please feel free to open a pull request if you wish:
* Automatic sharding
* Database support (e.g. storing configurations for individual users)
____

## User Guide
This section assumes you're using [IntelliJ IDEA](https://www.jetbrains.com/idea), and are familiar with Java and JVM configuration.

### JDK
If you don't have Java 16 already, download the corresponding JDK. Use the free [AdoptOpenJDK](https://adoptopenjdk.net/?variant=openjdk16&jvmVariant=hotspot) binaries.

### Building
`./gradlew shadowJar` on UNIX-like systems (e.g. Linux, macOS, *BSD)<br>
`gradlew.bat shadowJar` on Windows

### Refactoring
Chances are, you don't want to keep our package name. A good IDE will have refactoring support.

Refactor the following:
* The package name
* `mainClass` in [the build script](build.gradle.kts)
* `rootProject.name` in [the project settings](settings.gradle.kts)
* `rollingFileName` property in [the Log4j2 configuration](src/main/resources/log4j2.xml)

Test to make sure it builds correctly.

### Configuring
When running your Initium-based bot, it checks for a file named "config.toml" in the current working directory. Copy the TOML below and put it into a new file with that name.
```toml
prefix = "%"
token = "TOKEN"
```

### Running
For maximum performance, run the bot with a command like the following:<br>
`java -Xmx1024m -Xms1024m -jar initium-all.jar`

### Your First Command
#### Creating
In the `command` package, create a new class in one of the categories and make it extend `Command`<br>
In IntelliJ IDEA, press Ctrl + O, and override the `execute` and `getDescription` methods.

In `getDescription`, return what you'd like the help command to display for your command.<br>
In `execute`, enter the following:
```java
messageReceivedEvent.getChannel().sendMessage("Hello, world!").queue();
```

These aren't the only methods you can override. There's quite a few other ways you can customize your command.

#### Registering
Return to the main class. In the method `registerCommands`, enter the following:
```java
COMMANDS.put("mycommand", new MyCommand());
```
Tweak it for your own command, of course.<br>
The string will be what triggers it, so if your prefix is '%', you'd type "%mycommand".

You're all done! Fire up your bot to test it out.
____

## Learning More
A few other commands have been included to make your bot look great out of the box,
and they're a valuable resource for learning how the bot works. Each class has been thoroughly documented and commented
to make sure that what's going on inside your bot is straightforward and understandable.
Take a look at the various classes to understand more about your bot.

## Important Note About Templates
Due to the nature of GitHub templates, once you create your bot, you're on your own.
You're expected to update your dependencies and Gradle. Please do not ask for support for updating your bot.
