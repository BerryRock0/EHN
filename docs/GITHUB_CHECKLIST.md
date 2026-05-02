# GitHub Publish Checklist / GitHub 发布检查清单

## English

Use this checklist before the first GitHub push.

### Keep

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
- `LICENSE.txt`
- `lib/README.md`
- `docs/`
- `demo/` if you want screenshots in the repository

### Do Not Commit

- `build/`
- `.gradle/`
- `.vs/`
- `.idea/`
- `.vscode/`
- `mods/`
- `tools/generate-cn-patch.js`
- `lib/*.jar`
- local logs
- exported game files
- Project Zomboid installation files

The localization patch work is intentionally excluded from this repository.

### Local Build Check

```powershell
.\gradlew.bat clean build
```

The CI workflow skips the Gradle build when the local Project Zomboid
compile-time jars are not present, because those jars are not tracked.

### First Push

```powershell
git init
git add .
git status
git commit -m "Prepare EtherHack B42 compatibility fork"
git branch -M main
git remote add origin <your-github-repo-url>
git push -u origin main
```

Review `git status` before committing. If any ignored local folder appears, stop
and update `.gitignore` before pushing.

## 中文

第一次推送到 GitHub 前，请按这个清单检查。

### 应保留

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
- `LICENSE.txt`
- `lib/README.md`
- `docs/`
- `demo/`，如果你想保留截图

### 不要提交

- `build/`
- `.gradle/`
- `.vs/`
- `.idea/`
- `.vscode/`
- `mods/`
- `tools/generate-cn-patch.js`
- `lib/*.jar`
- 本地日志
- 导出的游戏文件
- Project Zomboid 游戏安装文件

汉化补丁工作建议放在独立仓库或独立分支，不和 EtherHack B42 兼容分支混在一起。

### 本地构建检查

```powershell
.\gradlew.bat clean build
```

因为 Project Zomboid 的编译期 jar 不提交，GitHub Actions 在缺少这些 jar 时会跳过 Gradle 构建。

### 首次推送

```powershell
git init
git add .
git status
git commit -m "Prepare EtherHack B42 compatibility fork"
git branch -M main
git remote add origin <your-github-repo-url>
git push -u origin main
```

提交前一定看一眼 `git status`。如果 `build/`、`.gradle/`、`mods/`、`lib/*.jar` 等本地文件出现在待提交列表里，先停下来修 `.gitignore`。

