# EtherHack B42 Compatibility Fork

<p align="center">
  <img src="demo/EtherLogo.png" alt="EtherHack Logo" width="360">
</p>

<p align="center">
  <a href="README.md">Overview</a> |
  <a href="README.en.md">English</a> |
  <a href="README.zh-CN.md">简体中文</a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Project%20Zomboid-Build%2042-2f6f4e" alt="Project Zomboid Build 42">
  <img src="https://img.shields.io/badge/Version-2.9.3-6f42c1" alt="EtherHack 2.9.3">
  <img src="https://img.shields.io/badge/Java-17-437291" alt="Java 17">
  <img src="https://img.shields.io/badge/ASM-9.9.1-5c4f99" alt="ASM 9.9.1">
  <img src="https://img.shields.io/github/license/ljy87263621/Project-Zomboid-EtherHack" alt="License">
</p>

EtherHack B42 - это форк EtherHack для совместимости с Project Zomboid Build
42. Он предназначен для локальной отладки, разработки модов и авторизованного
администрирования миров или серверов, которыми вы владеете или управляете.

## Важное замечание

В этом форке не поддерживаются старые пути обхода мультиплеера, подмены
администратора и обхода античита. Действия, связанные с мультиплеером, должны
проходить через канал `EtherDebug` клиент/сервер и выполняться только после
проверок `Capability` на стороне сервера Project Zomboid Build 42.

Используйте проект только в одиночной игре, локальных тестовых мирах или на
серверах, где у вас есть явное право администрирования. Уважайте правила
серверов, согласие игроков и условия игры.

## Содержание

- [Возможности](#возможности)
- [Структура репозитория](#структура-репозитория)
- [Требования](#требования)
- [Сборка](#сборка)
- [Установка](#установка)
- [Удаление](#удаление)
- [Использование](#использование)
- [Заметки для разработки](#заметки-для-разработки)
- [Перед публикацией на GitHub](#перед-публикацией-на-github)
- [Скриншоты](#скриншоты)
- [Лицензия](#лицензия)

## Возможности

Доступность функций зависит от режима игры, API Build 42 и прав на стороне
сервера.

| Раздел | Что входит |
| --- | --- |
| Интерфейс | Меню по `Insert`, изменяемый размер окна, боковые панели, сохранение настроек, перезагрузка Lua |
| Инструменты персонажа | Multi-hit, игнорирование игрока зомби, помощники строительства и фермерства, быстрые действия, ночное зрение, помощь с переносимым весом, выносливостью, патронами, прочностью, потребностями и состояниями |
| Авторизованные переключатели | God mode, невидимость, no-clip, unlimited carry, endurance и ammo через проверки `EtherDebug` |
| Предметы и рецепты | Браузер предметов, выдача предметов через авторизованный канал отладки, изучение рецептов, выдача ингредиентов для выбранного рецепта |
| Редактор игрока | Опыт и уровни навыков, черты, статистика игрока, медицинская панель |
| Мир | Действия редактирования объектов, панель механики транспорта, карта, перемещаемая мини-карта, запросы телепорта |
| Визуальные функции | Оверлеи игроков, транспорта и зомби, опция видимости объектов на 360 градусов, настраиваемые цвета интерфейса |
| Локализация | Игровые файлы перевода на английский, китайский и русский |

## Структура репозитория

```text
src/main/java/                  Java-установщик, bytecode-патчи, runtime bridge
src/main/resources/EtherHack/   Lua UI, медиа, переводы, серверный Lua для B42
lib/                            Локальные compile-time jar Project Zomboid
gradle/                         Gradle wrapper
docs/                           Заметки по публикации и поддержке
demo/                           Скриншоты и логотип
tools/                          Локальные скрипты обслуживания
```

Не коммитьте сгенерированные и локальные файлы:

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

## Требования

- Java 17 JDK или Java runtime, поставляемый с Project Zomboid.
- Локальная установка Project Zomboid Build 42.
- Compile-time jar из локального runtime игры, скопированные в `lib/`:

```text
zombie.jar
Kahlua.jar
fmod.jar
org.jar
```

Jar-файлы в `lib/` намеренно игнорируются, потому что они берутся из локальной
установки Project Zomboid и могут быть непригодны для повторного
распространения. См. [lib/README.md](lib/README.md).

## Сборка

```powershell
.\gradlew.bat clean build
```

Готовый jar будет создан здесь:

```text
build\EtherHack-2.9.3.jar
```

Версия берется из файла:

```text
src\main\resources\EtherHack\EtherHack.properties
```

## Установка

Запускайте установщик из корневой папки Project Zomboid:

```powershell
cd "D:\Apps\Steam\steamapps\common\ProjectZomboid"
& .\jre64\bin\java.exe -jar "D:\Dev\GitHub\Project-Zomboid-EtherHack_B42\build\EtherHack-2.9.3.jar" --install
```

Также можно использовать системный Java 17:

```powershell
java -jar "D:\Dev\GitHub\Project-Zomboid-EtherHack_B42\build\EtherHack-2.9.3.jar" --install
```

Установщик экспортирует файлы мода B42 в:

```text
%USERPROFILE%\Zomboid\mods\EtherHack
```

Он также записывает loose class overrides в корень игры, чтобы Build 42 загружал
их раньше `projectzomboid.jar`.

## Удаление

Запускайте из корневой папки Project Zomboid:

```powershell
& .\jre64\bin\java.exe -jar "D:\Dev\GitHub\Project-Zomboid-EtherHack_B42\build\EtherHack-2.9.3.jar" --uninstall
```

Деинсталлятор удаляет экспортированные файлы EtherHack и loose class overrides
для B42. Если ранее вы ставили старые сборки, после удаления проверьте файлы
игры через Steam.

## Использование

1. Соберите и установите jar.
2. Запустите Project Zomboid.
3. Нажмите `Insert`, чтобы открыть или закрыть меню EtherHack.

EtherHack может появиться в официальном Mod Loader, потому что установщик
экспортирует стандартную папку мода Build 42 для Lua, медиа и серверных файлов.

## Заметки для разработки

Полезные точки входа:

```text
src/main/java/EtherHack/Main.java
src/main/java/EtherHack/GamePatcher.java
src/main/java/EtherHack/Ether/EtherAPI.java
src/main/java/EtherHack/Ether/EtherMain.java
src/main/resources/EtherHack/lua/EtherHackMenu.lua
src/main/resources/EtherHack/lua/EtherDebugClient.lua
src/main/resources/EtherHack/media/lua/server/EtherHack/EtherDebugServer.lua
```

Панели Lua UI находятся здесь:

```text
src/main/resources/EtherHack/lua/components/
```

Авторизованные действия для мультиплеера следует добавлять через
`EtherDebugClient` и `EtherDebugServer`, проверяя нужный `Capability` Build 42
на сервере перед выполнением действия.

## Перед публикацией на GitHub

Рекомендуем выполнить:

```powershell
.\gradlew.bat clean build
git status --ignored
```

Затем проверьте [docs/GITHUB_CHECKLIST.md](docs/GITHUB_CHECKLIST.md).
Убедитесь, что build-артефакты, состояние IDE, локальные jar игры и
экспортированные игровые файлы не попали в commit.

## Скриншоты

![EtherHack screenshot 1](demo/1.jpg)
![EtherHack screenshot 2](demo/2.jpg)
![EtherHack screenshot 3](demo/3.jpg)
![EtherHack screenshot 4](demo/4.jpg)
![EtherHack screenshot 5](demo/5.jpg)
![EtherHack screenshot 6](demo/6.jpg)
![EtherHack screenshot 7](demo/7.jpg)
![EtherHack screenshot 8](demo/8.jpg)
![EtherHack screenshot 9](demo/9.jpg)

## Лицензия

MIT. См. [LICENSE.txt](LICENSE.txt).

## Благодарности

Этот форк основан на оригинальном проекте EtherHack. Текущая поддержка
сфокусирована на совместимости с Build 42, локальной отладке и авторизованном
администрировании.
