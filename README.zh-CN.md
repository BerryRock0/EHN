# EtherHack B42 兼容分支

这是 EtherHack 的 Project Zomboid Build 42 兼容分支。当前目标是让它适配 B42 的游戏结构、Lua/服务器脚本加载方式和多人权限模型，用于本地调试以及自有/自管服务器上的授权管理调试。

## 当前状态

- 目标游戏版本：Project Zomboid Build 42。
- 构建系统：Gradle + Java 17。
- 字节码库：ASM 9.9.1。
- 本地 Mod 目录：安装器会写入 `%USERPROFILE%\Zomboid\mods\EtherHack`。
- 游戏钩子：B42 下会在游戏根目录写入 loose class 覆盖，让它们优先于 `projectzomboid.jar` 加载。
- 多人调试：服务端 Lua 通过 B42 `Capability` 权限检查执行受控调试动作。

旧版中的多人绕过、管理员伪装、反作弊绕过路径不在本分支维护范围内。

## 目录结构

```text
src/main/java/                  Java 安装器、字节码补丁和运行时桥接
src/main/resources/EtherHack/   Lua UI、媒体、翻译、B42 服务端 Lua
lib/                            本地编译用 Project Zomboid jar，不提交
gradle/                         Gradle wrapper
docs/                           发布和维护文档
demo/                           截图素材，可按需保留
```

这些目录或文件不应提交到 GitHub：

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

## 准备依赖

需要 Java 17 JDK。

本项目编译时需要从本地 Project Zomboid 安装中准备这些 jar，并放入 `lib/`：

```text
zombie.jar
Kahlua.jar
fmod.jar
org.jar
```

这些文件来自本地游戏运行环境，可能不适合再分发，所以默认不提交。更多说明见 [lib/README.md](lib/README.md)。

## 构建

```powershell
.\gradlew.bat clean build
```

构建产物位于：

```text
build\EtherHack-<version>.jar
```

版本号来自：

```text
src\main\resources\EtherHack\EtherHack.properties
```

## 本地安装

从 Project Zomboid 游戏目录运行安装器：

```powershell
cd D:\Apps\Steam\steamapps\common\ProjectZomboid
& .\jre64\bin\java.exe -jar D:\Dev\Project-Zomboid-EtherHack-master\build\EtherHack-2.9.3.jar --install
```

安装器会写入：

```text
%USERPROFILE%\Zomboid\mods\EtherHack
```

并在游戏根目录写入 B42 需要的 loose class 覆盖：

```text
ProjectZomboid\zombie\...
```

## 卸载

同样从游戏目录运行：

```powershell
& .\jre64\bin\java.exe -jar D:\Dev\Project-Zomboid-EtherHack-master\build\EtherHack-2.9.3.jar --uninstall
```

B42 的 jar-based 游戏结构下，卸载器会删除 loose class 覆盖和导出的 EtherHack mod 文件。

## 使用

1. 构建并安装。
2. 启动 Project Zomboid。
3. 按 `Insert` 打开 EtherHack 菜单。

安装后，EtherHack 可能出现在官方 Mod Loader 中，这是因为安装器会导出一个标准 B42 mod 文件夹用于 Lua、媒体和服务端脚本。

## 多人授权调试通道

相关文件：

```text
src/main/resources/EtherHack/media/lua/server/EtherHack/EtherDebugServer.lua
src/main/resources/EtherHack/lua/EtherDebugClient.lua
```

多人调试动作由服务端检查 B42 `Capability` 后执行。例如获取物品会检查 `Capability.AddItem`。这是本分支支持的多人调试路径。

## 发布到 GitHub 前

见 [docs/GITHUB_CHECKLIST.md](docs/GITHUB_CHECKLIST.md)。

建议先运行：

```powershell
.\gradlew.bat clean build
git status --ignored
```

确认没有把 `build/`、`.gradle/`、`.vs/`、`mods/`、`lib/*.jar` 等本地文件提交进去。

## 安全和法律说明

本项目会修改本地 Project Zomboid 安装的加载行为，未来游戏更新可能导致不兼容。请仅在单人、本地测试世界，或你拥有/管理的服务器中使用，并遵守服务器规则和游戏条款。

## 许可证

MIT。见 [LICENSE.txt](LICENSE.txt)。

