# EtherHack B42 Compatibility Fork

<p align="center">
  <img src="demo/EtherLogo.png" alt="EtherHack Logo" width="360">
</p>

<p align="center">
  <a href="README.en.md">English</a> |
  <a href="README.zh-CN.md">简体中文</a> |
  <a href="README.ru.md">Русский</a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Project%20Zomboid-Build%2042-2f6f4e" alt="Project Zomboid Build 42">
  <img src="https://img.shields.io/badge/Java-17-437291" alt="Java 17">
  <img src="https://img.shields.io/badge/Gradle-Kotlin%20DSL-02303a" alt="Gradle Kotlin DSL">
  <img src="https://img.shields.io/github/license/ljy87263621/Project-Zomboid-EtherHack" alt="License">
</p>

This repository is a Project Zomboid Build 42 compatibility fork of EtherHack.
It is maintained for local debugging, mod development, and server-authorized
administration on worlds or servers you own or administer.

本仓库是 EtherHack 的 Project Zomboid Build 42 兼容分支，面向本地调试、Mod
开发，以及自有或受托管理服务器上的授权管理场景。

Этот репозиторий является совместимым с Project Zomboid Build 42 форком
EtherHack. Он предназначен для локальной отладки, разработки модов и
администрирования серверов, которыми вы владеете или управляете.

## Repository Notice

Older multiplayer bypass, admin-spoofing, and anti-cheat bypass paths are not
maintained in this fork. Multiplayer-facing debug actions should go through the
`EtherDebug` client/server channel and Project Zomboid Build 42 `Capability`
checks.

旧版多人绕过、管理员伪装和反作弊绕过路径不在本分支维护范围内。多人环境中的调试动作应通过
`EtherDebug` 客户端/服务端通道，并由 Project Zomboid Build 42 的 `Capability`
权限检查授权。

Старые пути обхода мультиплеера, подмены администратора и обхода античита в
этом форке не поддерживаются. Действия отладки в мультиплеере должны проходить
через канал `EtherDebug` клиент/сервер и проверки `Capability` в Project Zomboid
Build 42.

## Quick Links

- [English documentation](README.en.md)
- [中文说明](README.zh-CN.md)
- [Русская документация](README.ru.md)
- [GitHub publishing checklist](docs/GITHUB_CHECKLIST.md)
- [Local library notes](lib/README.md)

## At A Glance

```text
src/main/java/                  Java installer, bytecode patches, runtime bridge
src/main/resources/EtherHack/   Lua UI, media, translations, B42 server Lua
lib/                            Local Project Zomboid compile-time jars
docs/                           Publishing and maintenance notes
demo/                           Screenshots and logo assets
```

Build locally with:

```powershell
.\gradlew.bat clean build
```

The build expects local Project Zomboid jars in `lib/`. They are ignored because
they come from a local game installation and may not be redistributable.

## Screenshots

![EtherHack screenshot 1](demo/1.jpg)
![EtherHack screenshot 2](demo/2.jpg)
![EtherHack screenshot 3](demo/3.jpg)

## License

MIT. See [LICENSE.txt](LICENSE.txt).
