# EtherHack B42 兼容分支

<p align="center">
  <img src="demo/EtherLogo.png" alt="EtherHack Logo" width="360">
</p>

<p align="center">
  <a href="README.md">总览</a> |
  <a href="README.en.md">English</a> |
  <a href="README.ru.md">Русский</a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Project%20Zomboid-Build%2042-2f6f4e" alt="Project Zomboid Build 42">
  <img src="https://img.shields.io/badge/Version-2.9.3-6f42c1" alt="EtherHack 2.9.3">
  <img src="https://img.shields.io/badge/Java-17-437291" alt="Java 17">
  <img src="https://img.shields.io/badge/ASM-9.9.1-5c4f99" alt="ASM 9.9.1">
  <img src="https://img.shields.io/github/license/ljy87263621/Project-Zomboid-EtherHack" alt="License">
</p>

EtherHack B42 是 EtherHack 面向 Project Zomboid Build 42 的兼容分支。这个分支主要用于本地调试、Mod
开发，以及你拥有或受托管理的世界和服务器上的授权管理流程。

## 重要说明

本分支不维护旧版多人绕过、管理员伪装或反作弊绕过路径。涉及多人环境的动作应通过
`EtherDebug` 客户端/服务端通道，并在服务端通过 Project Zomboid Build 42 的 `Capability`
权限检查后再执行。

请只在单人游戏、本地测试世界，或你拥有明确管理权限的服务器中使用本项目。请遵守服务器规则、玩家同意原则和游戏条款。

## 目录

- [功能概览](#功能概览)
- [仓库结构](#仓库结构)
- [准备依赖](#准备依赖)
- [构建](#构建)
- [安装](#安装)
- [卸载](#卸载)
- [使用](#使用)
- [开发说明](#开发说明)
- [发布到 GitHub 前](#发布到-github-前)
- [截图](#截图)
- [许可证](#许可证)

## 功能概览

实际可用功能取决于游戏模式、Build 42 API 和服务端权限。

| 模块 | 内容 |
| --- | --- |
| UI 外壳 | `Insert` 呼出菜单、可缩放窗口、侧边栏面板、配置保存、Lua 重载 |
| 角色工具 | 多重打击、僵尸忽略、建造/耕作辅助、即时动作、夜视、负重/耐力/弹药/耐久辅助、需求和情绪状态控制 |
| 服务端授权开关 | God Mode、隐身、No Clip、无限负重、无限耐力、无限弹药，通过 `EtherDebug` 权限检查执行 |
| 物品和配方 | 物品浏览器、通过授权调试通道生成物品、学习配方、为当前选中配方补齐材料 |
| 玩家编辑 | 技能经验和等级、特质、玩家统计、医疗面板 |
| 世界工具 | 世界对象编辑、车辆机械面板、地图面板、可移动小地图、传送请求 |
| 视觉辅助 | 玩家、车辆、僵尸信息绘制，360 度对象可视选项，可配置 UI 颜色 |
| 本地化 | 游戏内英文、中文、俄文翻译文件 |

## 仓库结构

```text
src/main/java/                  Java 安装器、字节码补丁、运行时桥接
src/main/resources/EtherHack/   Lua UI、媒体资源、翻译、B42 服务端 Lua
lib/                            本地 Project Zomboid 编译期 jar，默认忽略
gradle/                         Gradle wrapper
docs/                           发布和维护说明
demo/                           截图和 Logo 资源
tools/                          本地维护脚本
```

这些生成文件或本机文件不应提交：

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

## 准备依赖

- Java 17 JDK，或 Project Zomboid 自带的 Java 运行时。
- 本地 Project Zomboid Build 42 安装。
- 从本地游戏运行环境复制到 `lib/` 的编译期 jar：

```text
zombie.jar
Kahlua.jar
fmod.jar
org.jar
```

`lib/` 中的 jar 来自本地 Project Zomboid 安装，可能不适合再分发，所以默认被 Git 忽略。更多说明见
[lib/README.md](lib/README.md)。

## 构建

```powershell
.\gradlew.bat clean build
```

构建产物位于：

```text
build\EtherHack-2.9.3.jar
```

版本号来自：

```text
src\main\resources\EtherHack\EtherHack.properties
```

## 安装

在 Project Zomboid 游戏根目录运行安装器：

```powershell
cd "D:\Apps\Steam\steamapps\common\ProjectZomboid"
& .\jre64\bin\java.exe -jar "D:\Dev\GitHub\Project-Zomboid-EtherHack_B42\build\EtherHack-2.9.3.jar" --install
```

也可以使用系统 Java 17：

```powershell
java -jar "D:\Dev\GitHub\Project-Zomboid-EtherHack_B42\build\EtherHack-2.9.3.jar" --install
```

安装器会把 B42 Mod 文件导出到：

```text
%USERPROFILE%\Zomboid\mods\EtherHack
```

同时会在游戏根目录写入 loose class 覆盖文件，让 Build 42 在 `projectzomboid.jar` 之前加载它们。

## 卸载

同样从 Project Zomboid 游戏根目录运行：

```powershell
& .\jre64\bin\java.exe -jar "D:\Dev\GitHub\Project-Zomboid-EtherHack_B42\build\EtherHack-2.9.3.jar" --uninstall
```

卸载器会移除导出的 EtherHack 文件和 B42 loose class 覆盖文件。如果你曾安装过更旧的版本，卸载后建议通过 Steam
校验游戏文件。

## 使用

1. 构建并安装 jar。
2. 启动 Project Zomboid。
3. 按 `Insert` 打开或关闭 EtherHack 菜单。

安装后，EtherHack 可能会出现在官方 Mod Loader 中，这是因为安装器会导出标准 Build 42 Mod 文件夹，用于 Lua、媒体资源和服务端脚本。

## 开发说明

常用入口：

```text
src/main/java/EtherHack/Main.java
src/main/java/EtherHack/GamePatcher.java
src/main/java/EtherHack/Ether/EtherAPI.java
src/main/java/EtherHack/Ether/EtherMain.java
src/main/resources/EtherHack/lua/EtherHackMenu.lua
src/main/resources/EtherHack/lua/EtherDebugClient.lua
src/main/resources/EtherHack/media/lua/server/EtherHack/EtherDebugServer.lua
```

Lua UI 面板位于：

```text
src/main/resources/EtherHack/lua/components/
```

多人环境中的授权动作应通过 `EtherDebugClient` 和 `EtherDebugServer` 增加，并在服务端检查对应的 Build 42
`Capability` 后再执行。

## 发布到 GitHub 前

建议先运行：

```powershell
.\gradlew.bat clean build
git status --ignored
```

然后检查 [docs/GITHUB_CHECKLIST.md](docs/GITHUB_CHECKLIST.md)。确认构建产物、IDE 状态、本地游戏 jar 和导出的游戏文件没有被加入提交。

## 截图

![EtherHack 截图 1](demo/1.jpg)
![EtherHack 截图 2](demo/2.jpg)
![EtherHack 截图 3](demo/3.jpg)
![EtherHack 截图 4](demo/4.jpg)
![EtherHack 截图 5](demo/5.jpg)
![EtherHack 截图 6](demo/6.jpg)
![EtherHack 截图 7](demo/7.jpg)
![EtherHack 截图 8](demo/8.jpg)
![EtherHack 截图 9](demo/9.jpg)

## 许可证

MIT。见 [LICENSE.txt](LICENSE.txt)。

## 致谢

本分支基于原 EtherHack 项目维护，当前重点是 Build 42 兼容、本地调试和授权管理场景。
