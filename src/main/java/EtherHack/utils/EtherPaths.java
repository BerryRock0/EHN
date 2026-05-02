package EtherHack.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class EtherPaths {
   public static final String MOD_ID = "EtherHack";
   public static final String B42_VERSION_FOLDER = "42";
   public static final String LEGACY_ROOT = "EtherHack";

   private EtherPaths() {
   }

   public static Path userZomboidDirectory() {
      return Paths.get(System.getProperty("user.home"), "Zomboid");
   }

   public static Path b42ModDirectory() {
      return userZomboidDirectory().resolve("mods").resolve(MOD_ID);
   }

   public static Path b42CommonMediaDirectory() {
      return b42ModDirectory().resolve("common").resolve("media");
   }

   public static Path b42VersionDirectory() {
      return b42ModDirectory().resolve(B42_VERSION_FOLDER);
   }

   public static Path b42VersionMediaDirectory() {
      return b42VersionDirectory().resolve("media");
   }

   public static Path resolveResourcePath(String relativePath) {
      String cleanPath = normalize(relativePath);
      for (Path candidate : candidateResourcePaths(cleanPath)) {
         if (Files.exists(candidate)) {
            return candidate;
         }
      }
      return Paths.get(cleanPath);
   }

   public static Path resolveWritablePath(String relativePath) {
      String cleanPath = normalize(relativePath);
      Path legacyPath = Paths.get(cleanPath);
      if (Files.exists(legacyPath) || Files.exists(legacyPath.getParent())) {
         return legacyPath;
      }
      return b42CommonMediaDirectory().resolve(cleanPath);
   }

   public static String resolveResourcePathString(String relativePath) {
      return resolveResourcePath(relativePath).toString();
   }

   private static List<Path> candidateResourcePaths(String cleanPath) {
      List<Path> paths = new ArrayList<>();
      Path relative = Paths.get(cleanPath);
      paths.add(relative);
      paths.add(b42VersionMediaDirectory().resolve(relative));
      paths.add(b42CommonMediaDirectory().resolve(relative));
      return paths;
   }

   private static String normalize(String path) {
      return path.replace('\\', '/');
   }
}
