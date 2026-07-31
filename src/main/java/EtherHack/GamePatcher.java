package EtherHack;

import EtherHack.utils.Info;
import EtherHack.utils.EtherPaths;
import EtherHack.utils.Logger;
import EtherHack.utils.Patch;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Comparator;
import java.util.jar.JarFile;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

/**
 * Класс, отвечающий за установку и удаление чита из кодов игры
 */
public class GamePatcher {

   /**
    * Список всех файлов, подлежащих инъекции
    */
   private final String[] patchFiles = new String[]{
           "GameWindow.class", "inventory/ItemContainer.class", "Lua/LuaEventManager.class"
   };

   /**
    * Loose class overrides created by older EtherHack builds that are no longer
    * patched by the B42-compatible installer.
    */
   private final String[] legacyPatchFilesToRemove = new String[]{
           "Lua/LuaManager.class"
   };

   /**
    * Название игровой папки с .class файлами
    */
   private final String gameClassFolder = "zombie";

   /**
    * Папки и файлы, которые нужно экспортировать в корневую директорию игры
    */
   private final String whiteListPathEtherFiles = "EtherHack";

   private final Path gameRoot = Paths.get("").toAbsolutePath();
   private final Path gameJar = gameRoot.resolve("projectzomboid.jar");

   /**
    * Экспортирование файлов EtherHack в корневую директорию игры
    */
   public void extractEtherHack() {
      try {
         String jarFilePath = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
         Path currentDirectory = gameRoot;
         Path b42ModDirectory = EtherPaths.b42ModDirectory();
         Path b42CommonMediaDirectory = EtherPaths.b42CommonMediaDirectory();
         Path b42VersionMediaDirectory = EtherPaths.b42VersionMediaDirectory();

         try (JarFile jarFile = new JarFile(jarFilePath)) {
            Files.createDirectories(b42CommonMediaDirectory);
            Files.createDirectories(b42VersionMediaDirectory);

            jarFile.stream().filter((entry) -> entry.getName().startsWith(whiteListPathEtherFiles + "/"))
                    .forEach((entry) -> {
                       try {
                          Path b42ExtractPath = getB42ExtractPath(entry.getName());
                          Path legacyExtractPath = currentDirectory.resolve(entry.getName());

                          if (entry.isDirectory()) {
                             Files.createDirectories(b42ExtractPath);
                             Files.createDirectories(legacyExtractPath);
                          } else {
                             Files.createDirectories(b42ExtractPath.getParent());
                             Files.createDirectories(legacyExtractPath.getParent());

                             try (InputStream inputStream = jarFile.getInputStream(entry)) {
                                Files.copy(inputStream, b42ExtractPath, StandardCopyOption.REPLACE_EXISTING);
                             }

                             try (InputStream inputStream = jarFile.getInputStream(entry)) {
                                Files.copy(inputStream, legacyExtractPath, StandardCopyOption.REPLACE_EXISTING);
                             }
                          }
                       } catch (IOException e) {
                          e.printStackTrace();
                       }
                    });
            writeB42ModInfo(b42ModDirectory);
            Logger.print("Extraction completed successfully");
            Logger.print("B42 local mod files were written to: " + b42ModDirectory);
         }
      } catch (URISyntaxException | IOException e) {
         e.printStackTrace();
      }
   }

   private Path getB42ExtractPath(String entryName) {
      Path relativePath = Path.of(entryName);

      if (entryName.startsWith("EtherHack/media/lua/")) {
         String standardLuaPath = entryName.substring("EtherHack/media/".length());
         return EtherPaths.b42VersionMediaDirectory().resolve(standardLuaPath);
      }

      if (entryName.startsWith("EtherHack/lua/")) {
         return EtherPaths.b42VersionMediaDirectory().resolve(relativePath);
      }

      return EtherPaths.b42CommonMediaDirectory().resolve(relativePath);
   }

   private void writeB42ModInfo(Path b42ModDirectory) throws IOException {
      Path versionDirectory = b42ModDirectory.resolve(EtherPaths.B42_VERSION_FOLDER);
      Files.createDirectories(versionDirectory);
      String version = Info.CHEAT_VERSION == null ? "unknown" : Info.CHEAT_VERSION;
      String modInfo = String.join(System.lineSeparator(),
              "name=EtherHack",
              "id=" + EtherPaths.MOD_ID,
              "description=EtherHack B42 compatibility package. Java bytecode hooks are installed separately by the patcher.",
              "version=" + version,
              "");
      Files.writeString(versionDirectory.resolve("mod.info"), modInfo);
   }


   /**
    *  Удаление всех экспортированных файлов EtherHack из директории игры
    */
   public void uninstallEtherHackFiles() {
      Logger.print("Deleting all EtherHack files...");

      try {
         Path currentDirectory = gameRoot;
         Path targetPath = currentDirectory.resolve(whiteListPathEtherFiles);
         if (Files.exists(targetPath)) {
            deleteRecursively(targetPath, currentDirectory);
         }

         Path b42ModDirectory = EtherPaths.b42ModDirectory();
         if (Files.exists(b42ModDirectory)) {
            deleteRecursively(b42ModDirectory, EtherPaths.userZomboidDirectory().resolve("mods"));
         }
         Logger.print("Deletion EtherHack files completed successfully");
      } catch (IOException except) {
         except.printStackTrace();
      }

   }

   /**
    * Создает резервные копии игровых файлов, если они еще не существуют.
    * Файлы будут сохранены с расширением .bkup в той же папке, что и оригиналы.
    */
   public void backupGameFiles() {
      if (isB42JarGameFolder()) {
         Logger.print("B42 jar-based game layout detected. Patched classes will be written as loose classpath overrides; no jar backup is required.");
         return;
      }

      for (int i = 0; i < patchFiles.length; i++) {
         String iteration = "[" + (i + 1) + "/" + patchFiles.length + "]";
         Logger.print("Creating a backup file '" + patchFiles[i] + "' " + iteration);

         Path originalFilePath = gameRoot.resolve(gameClassFolder).resolve(patchFiles[i]);

         if (Files.exists(originalFilePath)) {
            try {
               Path backupFilePath = Paths.get(originalFilePath + ".bkup");

               if (Files.exists(backupFilePath)) {
                  Logger.print("Backup of the file already exists. Skipping backup.");
               } else {
                  Files.copy(originalFilePath, backupFilePath);
               }
            } catch (IOException e) {
               Logger.print("Error while creating backup file: " + e.getMessage());
            }
         } else {
            Logger.print(patchFiles[i] + " file not found.");
         }
      }

      Logger.print("Backups of game files have been completed!");
   }

   /**
    * Внедрение в файл игрового окна
    */
   public void patchGameWindow() {
      Patch.injectIntoClass("zombie/GameWindow", "init", true, (method) -> {
         AbstractInsnNode insertionPoint = null;

         for (AbstractInsnNode insn : method.instructions.toArray()) {
            if (insn instanceof MethodInsnNode) {
               MethodInsnNode methodInsn = (MethodInsnNode) insn;
               if (methodInsn.getOpcode() == Opcodes.INVOKESTATIC
                       && methodInsn.owner.equals("zombie/Lua/LuaManager")
                       && methodInsn.name.equals("init")) {
                  insertionPoint = insn;
                  break;
               }
            }
         }

         if (insertionPoint != null) {
            Logger.printLog("Found LuaManager.init() invocation in the GameWindow init method.");
         } else {
            throw new IllegalStateException("Cannot find LuaManager.init() invocation in the method when patching the Game window");
         }

         AbstractInsnNode returnInsn = null;
         for (AbstractInsnNode insn : method.instructions.toArray()) {
            if (insn.getOpcode() == Opcodes.RETURN) {
               returnInsn = insn;
            }
         }

         if (returnInsn != null) {
            InsnList initEtherInstructions = new InsnList();
            initEtherInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "EtherHack/Ether/EtherMain", "initSafe", "()V", false));
            method.instructions.insertBefore(returnInsn, initEtherInstructions);
         } else {
            throw new IllegalStateException("Could not find the end of the method when patching the Game window");
         }
      });

   }

   /**
    * Внедрение в файлы игровых предметов
    */
   public void patchItemContainer() {
      Patch.injectIntoClass("zombie/inventory/ItemContainer", "getWeight", false, (method) -> {
         InsnList newInstructions = new InsnList();
         LabelNode carryOnLabel = new LabelNode();
         newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "EtherHack/Ether/EtherMain", "getInstance", "()LEtherHack/Ether/EtherMain;", false));
         newInstructions.add(new JumpInsnNode(Opcodes.IFNULL, carryOnLabel));
         newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "EtherHack/Ether/EtherMain", "getInstance", "()LEtherHack/Ether/EtherMain;", false));
         newInstructions.add(new FieldInsnNode(Opcodes.GETFIELD, "EtherHack/Ether/EtherMain", "etherAPI", "LEtherHack/Ether/EtherAPI;"));
         newInstructions.add(new JumpInsnNode(Opcodes.IFNULL, carryOnLabel));
         newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "EtherHack/Ether/EtherMain", "getInstance", "()LEtherHack/Ether/EtherMain;", false));
         newInstructions.add(new FieldInsnNode(Opcodes.GETFIELD, "EtherHack/Ether/EtherMain", "etherAPI", "LEtherHack/Ether/EtherAPI;"));
         newInstructions.add(new FieldInsnNode(Opcodes.GETFIELD, "EtherHack/Ether/EtherAPI", "isUnlimitedCarry", "Z"));
         newInstructions.add(new JumpInsnNode(Opcodes.IFEQ, carryOnLabel));
         newInstructions.add(new InsnNode(Opcodes.F_SAME));
         newInstructions.add(new InsnNode(Opcodes.IRETURN));
         newInstructions.add(carryOnLabel);
         method.instructions.insert(newInstructions);
      });
      Patch.injectIntoClass("zombie/inventory/ItemContainer", "getCapacityWeight", false, (method) -> {
         InsnList newInstructions = new InsnList();
         LabelNode carryOnLabel = new LabelNode();
         newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "EtherHack/Ether/EtherMain", "getInstance", "()LEtherHack/Ether/EtherMain;", false));
         newInstructions.add(new JumpInsnNode(Opcodes.IFNULL, carryOnLabel));
         newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "EtherHack/Ether/EtherMain", "getInstance", "()LEtherHack/Ether/EtherMain;", false));
         newInstructions.add(new FieldInsnNode(Opcodes.GETFIELD, "EtherHack/Ether/EtherMain", "etherAPI", "LEtherHack/Ether/EtherAPI;"));
         newInstructions.add(new JumpInsnNode(Opcodes.IFNULL, carryOnLabel));
         newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "EtherHack/Ether/EtherMain", "getInstance", "()LEtherHack/Ether/EtherMain;", false));
         newInstructions.add(new FieldInsnNode(Opcodes.GETFIELD, "EtherHack/Ether/EtherMain", "etherAPI", "LEtherHack/Ether/EtherAPI;"));
         newInstructions.add(new FieldInsnNode(Opcodes.GETFIELD, "EtherHack/Ether/EtherAPI", "isUnlimitedCarry", "Z"));
         newInstructions.add(new JumpInsnNode(Opcodes.IFEQ, carryOnLabel));
         newInstructions.add(new InsnNode(Opcodes.FCONST_0));
         newInstructions.add(new InsnNode(Opcodes.FRETURN));
         newInstructions.add(carryOnLabel);
         method.instructions.insert(newInstructions);
      });
      Patch.injectIntoClass("zombie/inventory/ItemContainer", "getContentsWeight", false, (method) -> {
         InsnList newInstructions = new InsnList();
         LabelNode carryOnLabel = new LabelNode();
         newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "EtherHack/Ether/EtherMain", "getInstance", "()LEtherHack/Ether/EtherMain;", false));
         newInstructions.add(new JumpInsnNode(Opcodes.IFNULL, carryOnLabel));
         newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "EtherHack/Ether/EtherMain", "getInstance", "()LEtherHack/Ether/EtherMain;", false));
         newInstructions.add(new FieldInsnNode(Opcodes.GETFIELD, "EtherHack/Ether/EtherMain", "etherAPI", "LEtherHack/Ether/EtherAPI;"));
         newInstructions.add(new JumpInsnNode(Opcodes.IFNULL, carryOnLabel));
         newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "EtherHack/Ether/EtherMain", "getInstance", "()LEtherHack/Ether/EtherMain;", false));
         newInstructions.add(new FieldInsnNode(Opcodes.GETFIELD, "EtherHack/Ether/EtherMain", "etherAPI", "LEtherHack/Ether/EtherAPI;"));
         newInstructions.add(new FieldInsnNode(Opcodes.GETFIELD, "EtherHack/Ether/EtherAPI", "isUnlimitedCarry", "Z"));
         newInstructions.add(new JumpInsnNode(Opcodes.IFEQ, carryOnLabel));
         newInstructions.add(new InsnNode(Opcodes.FCONST_0));
         newInstructions.add(new InsnNode(Opcodes.FRETURN));
         newInstructions.add(carryOnLabel);
         method.instructions.insert(newInstructions);
      });
   }

   /**
    * Внедрение в файл LuaEventManager
    */
   public void patchLuaEventManager() {
      Patch.injectIntoClass("zombie/Lua/LuaEventManager", "triggerEvent", true, (method) -> {
         InsnList toInject = new InsnList();
         toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
         toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "EtherHack/utils/EventSubscriber", "invokeSubscriber", "(Ljava/lang/String;)V", false));
         method.instructions.insertBefore(method.instructions.get(0), toInject);
      });
   }

   /**
    * Проверяет, содержит ли хотя бы один из заданных файлов аннотацию @Injected.
    * @return true, если аннотация @Injected найдена хотя бы в одном файле. false в противном случае.
    */
   public boolean checkInjectedAnnotations() {
      return Arrays.stream(patchFiles)
              .anyMatch(filePath -> Patch.isInjectedAnnotationPresent(filePath, gameClassFolder));
   }

   /**
    * Проверяет наличие игровой папки и определенных файлов внутри.
    * @return true, если игровая папка и все требуемые файлы присутствуют. false в противном случае.
    */
   public boolean isGameFolder() {
      Path gameFolderPath = gameRoot.resolve(gameClassFolder);

      // Проверяем, существует ли папка игры
      if (Files.exists(gameFolderPath) && Files.isDirectory(gameFolderPath)) {
         // Если папка существует, проверяем наличие всех необходимых файлов
         boolean hasLooseClasses = Arrays.stream(patchFiles)
                 .allMatch(fileName -> Files.exists(gameFolderPath.resolve(fileName)));
         if (hasLooseClasses) {
            return true;
         }
      }

      return isB42JarGameFolder();
   }

   private boolean isB42JarGameFolder() {
      if (!Files.exists(gameJar)) {
         return false;
      }

      try (JarFile jarFile = new JarFile(gameJar.toFile())) {
         return Arrays.stream(patchFiles)
                 .allMatch(fileName -> jarFile.getEntry(gameClassFolder + "/" + fileName) != null);
      } catch (IOException e) {
         Logger.print("Error while reading projectzomboid.jar: " + e.getMessage());
         return false;
      }
   }

   /**
    * Патчинг игровых bytecode файлов игры
    * для реализации собственного фунционала
    */
   public void patchGame() {
      Logger.printCredits();

      Logger.print("Preparing to install the EtherHack...");
      Patch.setClassSource(gameRoot, gameJar);

      if (!isGameFolder()) {
         Logger.print("No game files were found in this directory. Place the cheat in the root folder of the game");
         return;
      }

      deleteLegacyLoosePatchFiles();

      Logger.print("Checking for injections in game files");

      if (checkInjectedAnnotations()) {
         Logger.print("Signs of interference were found in the game files. If you have installed this cheat before, run it with the '--uninstall' flag. Otherwise, check the integrity of the game files via Steam");
         return;
      }
      Logger.print("No signs of injections were found. Preparing for backup...");
      backupGameFiles();
      Logger.print("Preparation for injection into game file...");

      patchGameWindow();
      patchItemContainer();
      patchLuaEventManager();

      Patch.saveModifiedClasses();

      Logger.print("The injections were completed!");

      Logger.print("Extracting EtherHack files to the current directory...");

      extractEtherHack();

      Logger.print("The cheat installation is complete, you can enter the game!");
   }

   /**
    * Восстановление оригинальных файлов игры
    */
   public void restoreFiles() {
      Logger.printCredits();
      Logger.print("Restoring files...");

      for(int i = 0; i < patchFiles.length; ++i) {
         String fileName = patchFiles[i];
         String iteration = "[" + (i + 1) + "/" + patchFiles.length + "]";
         Logger.print("Restoring the file '" + fileName + "' " + iteration);
         Path originalFilePath = gameRoot.resolve("zombie").resolve(patchFiles[i]);
         Path backupFilePath = Paths.get(originalFilePath.toString() + ".bkup");
         if (Files.exists(backupFilePath)) {
            try {
               if (Files.exists(originalFilePath)) {
                  Files.delete(originalFilePath);
               }

               Files.move(backupFilePath, originalFilePath);
            } catch (IOException e) {
               Logger.print("Error when restoring the game file '" + fileName + "': " + e.getMessage());
            }
         } else if (isB42JarGameFolder() && Files.exists(originalFilePath)) {
            try {
               Files.delete(originalFilePath);
            } catch (IOException e) {
               Logger.print("Error when deleting patched B42 class '" + fileName + "': " + e.getMessage());
            }
         } else {
            Logger.print("Backup file '" + fileName + ".bkup' not found. Skipping restore");
         }
      }

      Logger.print("Files restoration completed!");
      deleteLegacyLoosePatchFiles();
      uninstallEtherHackFiles();
   }

   private void deleteLegacyLoosePatchFiles() {
      for (String fileName : legacyPatchFilesToRemove) {
         Path looseClassPath = gameRoot.resolve(gameClassFolder).resolve(fileName);
         if (!Files.exists(looseClassPath)) {
            continue;
         }

         try {
            Files.delete(looseClassPath);
            Logger.print("Deleted legacy patched B42 class '" + fileName + "'");
         } catch (IOException e) {
            Logger.print("Error when deleting legacy patched B42 class '" + fileName + "': " + e.getMessage());
         }
      }
   }

   private void deleteRecursively(Path targetPath, Path allowedParent) throws IOException {
      Path normalizedTarget = targetPath.toAbsolutePath().normalize();
      Path normalizedParent = allowedParent.toAbsolutePath().normalize();

      if (!normalizedTarget.startsWith(normalizedParent) || normalizedTarget.equals(normalizedParent)) {
         throw new IOException("Refusing to delete unsafe path: " + normalizedTarget);
      }

      Files.walk(normalizedTarget).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
   }
}
