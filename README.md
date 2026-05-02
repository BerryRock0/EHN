# EtherHack B42 Compatibility Fork

[中文说明](README.zh-CN.md) | [English](README.en.md)

This repository is a Project Zomboid Build 42 compatibility fork of EtherHack.
It is maintained for local debugging and server-authorized administration on
servers you own or administer.

本仓库是 EtherHack 的 Project Zomboid Build 42 兼容分支，主要用于本地调试和自有/自管服务器上的授权管理调试。

## Quick Links / 快速入口

- [中文文档](README.zh-CN.md)
- [English documentation](README.en.md)
- [GitHub publish checklist / 发布检查清单](docs/GITHUB_CHECKLIST.md)
- [Local library notes / 本地依赖说明](lib/README.md)

## Important Note / 重要说明

Older multiplayer bypass and admin-spoofing paths are not maintained in this
fork. Multiplayer-facing debug actions should go through the `EtherDebug`
client/server channel and Project Zomboid B42 `Capability` checks.

本分支不维护旧版本中的多人绕过和管理员伪装路径。多人环境里的调试动作应通过 `EtherDebug` 客户端/服务端通道，并由 Project Zomboid B42 的 `Capability` 权限检查授权。

