# EtherHack B42 Compatibility Fork

This repository is a Project Zomboid Build 42 compatibility fork of EtherHack.
It focuses on local debugging and server-authorized administration workflows
for servers you own or administer.

## Status

- Target game line: Project Zomboid Build 42.
- Build system: Gradle with Java 17.
- Bytecode library: ASM 9.9.1.
- Local mod layout: the installer writes B42 files under `%USERPROFILE%\Zomboid\mods\EtherHack`.
- Game hooks: B42 loose class overrides are written under the Project Zomboid game root so they load before `projectzomboid.jar`.
- Multiplayer debugging: server Lua uses B42 `Capability` checks before applying controlled debug actions.

The older multiplayer bypass, admin-spoofing, and anti-cheat bypass paths are
not maintained in this fork.

## Repository Layout

```text
src/main/java/                  Java installer, bytecode patches, runtime bridge
src/main/resources/EtherHack/   Lua UI, media, translations, B42 server Lua
lib/                            Local compile-time Project Zomboid jars, not committed
gradle/                         Gradle wrapper
docs/                           Publishing and maintenance notes
demo/                           Screenshots, optional
```

Do not commit local/generated files such as:

```text
build/
.gradle/
.vs/
.idea/
mods/
tools/generate-cn-patch.js
lib/*.jar
*.log
```

## Prerequisites

Use a Java 17 JDK.

The Gradle build expects these local Project Zomboid compile-time jars in
`lib/`:

```text
zombie.jar
Kahlua.jar
fmod.jar
org.jar
```

These files come from a local Project Zomboid installation/runtime and may not
be redistributable, so they are intentionally ignored. See [lib/README.md](lib/README.md).

## Build

```powershell
.\gradlew.bat clean build
```

The jar is written to:

```text
build\EtherHack-<version>.jar
```

The version comes from:

```text
src\main\resources\EtherHack\EtherHack.properties
```

## Install Locally

Run the installer from the Project Zomboid game directory:

```powershell
cd D:\Apps\Steam\steamapps\common\ProjectZomboid
& .\jre64\bin\java.exe -jar D:\Dev\Project-Zomboid-EtherHack-master\build\EtherHack-2.9.3.jar --install
```

The installer writes:

```text
%USERPROFILE%\Zomboid\mods\EtherHack
```

and patched loose game classes under the game root:

```text
ProjectZomboid\zombie\...
```

## Uninstall

Run from the Project Zomboid game directory:

```powershell
& .\jre64\bin\java.exe -jar D:\Dev\Project-Zomboid-EtherHack-master\build\EtherHack-2.9.3.jar --uninstall
```

For B42 jar-based installs, the patcher removes the loose class overrides and
the exported EtherHack mod files.

## Usage

1. Build and install.
2. Start Project Zomboid.
3. Press `Insert` to open the EtherHack menu.

The EtherHack entry may appear in the official Mod Loader because the installer
exports a standard B42 mod folder for Lua, media, and server files.

## Server-Authorized Debug Channel

Relevant files:

```text
src/main/resources/EtherHack/media/lua/server/EtherHack/EtherDebugServer.lua
src/main/resources/EtherHack/lua/EtherDebugClient.lua
```

Server actions are checked against Project Zomboid B42 capabilities before being
applied. For example, item spawning checks `Capability.AddItem`. This is the
supported multiplayer debugging path in this fork.

## Before Publishing To GitHub

See [docs/GITHUB_CHECKLIST.md](docs/GITHUB_CHECKLIST.md).

Recommended local check:

```powershell
.\gradlew.bat clean build
git status --ignored
```

Make sure `build/`, `.gradle/`, `.vs/`, `mods/`, `lib/*.jar`, and other local
files are not staged.

## Legal / Safety

This project modifies a local Project Zomboid installation at runtime and may be
incompatible with future game updates. Use it only in single-player, local test
worlds, or servers you own/administer. Respect server rules and game terms.

## License

MIT. See [LICENSE.txt](LICENSE.txt).

