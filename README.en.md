# EtherHack B42 Compatibility Fork

<p align="center">
  <img src="demo/EtherLogo.png" alt="EtherHack Logo" width="360">
</p>

<p align="center">
  <a href="README.md">Overview</a> |
  <a href="README.zh-CN.md">简体中文</a> |
  <a href="README.ru.md">Русский</a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Project%20Zomboid-Build%2042-2f6f4e" alt="Project Zomboid Build 42">
  <img src="https://img.shields.io/badge/Version-2.9.3-6f42c1" alt="EtherHack 2.9.3">
  <img src="https://img.shields.io/badge/Java-17-437291" alt="Java 17">
  <img src="https://img.shields.io/badge/ASM-9.9.1-5c4f99" alt="ASM 9.9.1">
  <img src="https://img.shields.io/github/license/ljy87263621/Project-Zomboid-EtherHack" alt="License">
</p>

EtherHack B42 is a compatibility fork of EtherHack for Project Zomboid Build 42.
It focuses on local debugging, mod development, and server-authorized
administration workflows for worlds or servers you own or administer.

## Notice

This fork does not maintain the older multiplayer bypass, admin-spoofing, or
anti-cheat bypass paths. Multiplayer-facing actions are expected to use the
`EtherDebug` client/server channel and Project Zomboid Build 42 `Capability`
checks before the server applies them.

Use this project only in single-player, local test worlds, or servers where you
have explicit administrative permission. Respect server rules, player consent,
and the game terms.

## Contents

- [Features](#features)
- [Repository Layout](#repository-layout)
- [Prerequisites](#prerequisites)
- [Build](#build)
- [Install](#install)
- [Uninstall](#uninstall)
- [Usage](#usage)
- [Development Notes](#development-notes)
- [Publishing To GitHub](#publishing-to-github)
- [Screenshots](#screenshots)
- [License](#license)

## Features

Availability depends on game mode, Build 42 APIs, and server-side permissions.

| Area | Included |
| --- | --- |
| UI shell | Resizable `Insert` menu, sidebar panels, saved settings, Lua reload action |
| Character tools | Multi-hit, zombie ignore, build/farming helpers, timed actions, night vision, carry/endurance/ammo/condition helpers, needs and moodle controls |
| Server-authorized toggles | God mode, invisibility, no-clip, unlimited carry, endurance, and ammo through `EtherDebug` capability checks |
| Items and recipes | Item browser, item spawning through the authorized debug channel, recipe learning, selected-recipe ingredient helper |
| Player editing | Skill XP and levels, traits, player stats, medical panel |
| World tools | Object edit context actions, vehicle mechanics panel, map panel, movable minimap, teleport requests |
| Visuals | Player, vehicle, and zombie overlays, 360-degree object visibility option, configurable UI colors |
| Localization | English, Chinese, and Russian in-game translation files |

## Repository Layout

```text
src/main/java/                  Java installer, bytecode patches, runtime bridge
src/main/resources/EtherHack/   Lua UI, media, translations, B42 server Lua
lib/                            Local compile-time Project Zomboid jars, ignored
gradle/                         Gradle wrapper
docs/                           Publishing and maintenance notes
demo/                           Screenshots and logo assets
tools/                          Local maintenance scripts
```

Do not commit generated or machine-local files:

```text
build/
.gradle/
.vs/
.idea/
.vscode/
mods/
github-ready/
lib/*.jar
*.log
```

## Prerequisites

- Java 17 JDK, or the Java runtime bundled with Project Zomboid.
- A local Project Zomboid Build 42 installation.
- These compile-time jars copied from your local game runtime into `lib/`:

```text
zombie.jar
Kahlua.jar
fmod.jar
org.jar
```

The jars in `lib/` are intentionally ignored because they come from the local
Project Zomboid installation and may not be redistributable. See
[lib/README.md](lib/README.md).

## Build

```powershell
.\gradlew.bat clean build
```

The jar is written to:

```text
build\EtherHack-2.9.3.jar
```

The version is read from:

```text
src\main\resources\EtherHack\EtherHack.properties
```

## Install

Run the installer from the Project Zomboid game directory:

```powershell
cd "D:\Apps\Steam\steamapps\common\ProjectZomboid"
& .\jre64\bin\java.exe -jar "D:\Dev\GitHub\Project-Zomboid-EtherHack_B42\build\EtherHack-2.9.3.jar" --install
```

You can also use a system Java 17 runtime:

```powershell
java -jar "D:\Dev\GitHub\Project-Zomboid-EtherHack_B42\build\EtherHack-2.9.3.jar" --install
```

The installer writes the exported B42 mod files to:

```text
%USERPROFILE%\Zomboid\mods\EtherHack
```

It also writes loose class overrides under the game root so Build 42 loads them
before `projectzomboid.jar`.

## Uninstall

Run from the Project Zomboid game directory:

```powershell
& .\jre64\bin\java.exe -jar "D:\Dev\GitHub\Project-Zomboid-EtherHack_B42\build\EtherHack-2.9.3.jar" --uninstall
```

The uninstaller removes exported EtherHack files and B42 loose class overrides.
If you installed older builds before this fork, verify game files through Steam
after uninstalling.

## Usage

1. Build and install the jar.
2. Start Project Zomboid.
3. Press `Insert` to open or close the EtherHack menu.

The EtherHack entry may appear in the official Mod Loader because the installer
exports a standard Build 42 mod folder for Lua, media, and server files.

## Development Notes

Useful entry points:

```text
src/main/java/EtherHack/Main.java
src/main/java/EtherHack/GamePatcher.java
src/main/java/EtherHack/Ether/EtherAPI.java
src/main/java/EtherHack/Ether/EtherMain.java
src/main/resources/EtherHack/lua/EtherHackMenu.lua
src/main/resources/EtherHack/lua/EtherDebugClient.lua
src/main/resources/EtherHack/media/lua/server/EtherHack/EtherDebugServer.lua
```

Lua UI panels live under:

```text
src/main/resources/EtherHack/lua/components/
```

Server-authorized multiplayer actions should be added through `EtherDebugClient`
and `EtherDebugServer`, with the relevant Build 42 `Capability` checked on the
server before applying any action.

## Publishing To GitHub

Before publishing, run:

```powershell
.\gradlew.bat clean build
git status --ignored
```

Then review [docs/GITHUB_CHECKLIST.md](docs/GITHUB_CHECKLIST.md). Make sure
generated output, IDE state, local game jars, and exported game files are not
staged.

## Screenshots

![EtherHack screenshot 1](demo/1.jpg)
![EtherHack screenshot 2](demo/2.jpg)
![EtherHack screenshot 3](demo/3.jpg)
![EtherHack screenshot 4](demo/4.jpg)
![EtherHack screenshot 5](demo/5.jpg)
![EtherHack screenshot 6](demo/6.jpg)
![EtherHack screenshot 7](demo/7.jpg)
![EtherHack screenshot 8](demo/8.jpg)
![EtherHack screenshot 9](demo/9.jpg)

## License

MIT. See [LICENSE.txt](LICENSE.txt).

## Credits

This fork builds on the original EtherHack project and keeps the B42-compatible
maintenance work focused on local debugging and authorized administration.
