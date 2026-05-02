This directory is for local compile-time Project Zomboid libraries.

The Gradle build expects these files:

- `zombie.jar`
- `Kahlua.jar`
- `fmod.jar`
- `org.jar`

They are intentionally not tracked in Git because they come from the local
Project Zomboid installation/runtime and may not be redistributable.

Copy them into this directory before running:

```powershell
.\gradlew.bat build
```

