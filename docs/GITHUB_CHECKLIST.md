# GitHub Publish Checklist

Use this checklist before pushing the repository to GitHub or preparing a public
release.

## Keep

- `src/`
- `gradle/`
- `gradlew`
- `gradlew.bat`
- `build.gradle.kts`
- `settings.gradle.kts`
- `.github/`
- `.gitignore`
- `README.md`
- `README.en.md`
- `README.zh-CN.md`
- `README.ru.md`
- `LICENSE.txt`
- `lib/README.md`
- `docs/`
- `demo/`, if you want screenshots and the logo in the repository
- `tools/export-github-ready.ps1`, if you want a local export helper

## Do Not Commit

- `build/`
- `.gradle/`
- `.vs/`
- `.idea/`
- `.vscode/`
- `mods/`
- `github-ready/`
- `release/`
- `releases/`
- `tools/generate-cn-patch.js`
- `lib/*.jar`
- local logs
- exported game files
- Project Zomboid installation files

The Project Zomboid compile-time jars are intentionally excluded because they
come from a local game installation and may not be redistributable.

## Local Build Check

```powershell
.\gradlew.bat clean build
```

The CI workflow skips the Gradle build when the local Project Zomboid
compile-time jars are not present.

## Git Status Check

```powershell
git status --ignored
```

Before committing, make sure generated output, IDE state, local game jars, and
exported game files are absent from the staged file list.

## Optional Export

To create a clean local copy for manual inspection:

```powershell
.\tools\export-github-ready.ps1
```

The export is written to `github-ready/EtherHack-B42`, which is ignored by Git.

## 中文检查清单

首次推送到 GitHub 或准备公开发布前，请检查以下内容。

## 应保留

- `src/`
- `gradle/`
- `gradlew`
- `gradlew.bat`
- `build.gradle.kts`
- `settings.gradle.kts`
- `.github/`
- `.gitignore`
- `README.md`
- `README.en.md`
- `README.zh-CN.md`
- `README.ru.md`
- `LICENSE.txt`
- `lib/README.md`
- `docs/`
- `demo/`，如果你希望仓库包含截图和 Logo
- `tools/export-github-ready.ps1`，如果你希望保留本地导出脚本

## 不要提交

- `build/`
- `.gradle/`
- `.vs/`
- `.idea/`
- `.vscode/`
- `mods/`
- `github-ready/`
- `release/`
- `releases/`
- `tools/generate-cn-patch.js`
- `lib/*.jar`
- 本地日志
- 导出的游戏文件
- Project Zomboid 游戏安装文件

Project Zomboid 的编译期 jar 来自本地游戏安装，可能不适合再分发，所以应保持在 Git 外。

## 本地构建检查

```powershell
.\gradlew.bat clean build
```

如果 CI 环境中没有本地 Project Zomboid 编译期 jar，GitHub Actions 会跳过 Gradle 构建。

## Git 状态检查

```powershell
git status --ignored
```

提交前确认构建产物、IDE 状态、本地游戏 jar 和导出的游戏文件没有进入暂存区。

## 可选导出

如果想生成一个便于人工检查的干净副本：

```powershell
.\tools\export-github-ready.ps1
```

导出目录是 `github-ready/EtherHack-B42`，该目录已被 Git 忽略。
