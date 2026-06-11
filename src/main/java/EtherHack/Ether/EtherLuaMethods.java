package EtherHack.Ether;

import EtherHack.utils.Logger;
import EtherHack.utils.ConfigUtils;
import EtherHack.utils.EtherPaths;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import se.krka.kahlua.integration.annotations.LuaMethod;
import se.krka.kahlua.vm.KahluaTable;
import zombie.Lua.LuaManager;
import zombie.characters.IsoPlayer;
import zombie.core.Color;
import zombie.core.network.ByteBufferWriter;
import zombie.core.textures.Texture;
import zombie.inventory.InventoryItem;
import zombie.network.GameClient;
import zombie.network.PacketTypes;
import zombie.scripting.ScriptManager;
import zombie.scripting.objects.Recipe;

public class EtherLuaMethods {
   private static EtherLuaMethods instance = null;

   @LuaMethod(name = "getZombieUIColor", global = true)
   public static Color getZombieUIColor() {
      return EtherMain.getInstance().etherAPI.zombiesUIColor;
   }

   @LuaMethod(name = "setZombieUIColor",global = true)
   public static void setZombieUIColor(float var0, float var1, float var2) {
      Color var3 = new Color(var0, var1, var2);
      EtherMain.getInstance().etherAPI.zombiesUIColor = var3;
   }

   @LuaMethod(name = "getVehicleUIColor", global = true)
   public static Color getVehicleUIColor() {
      return EtherMain.getInstance().etherAPI.vehiclesUIColor;
   }

   @LuaMethod(name = "setVehicleUIColor", global = true)
   public static void setVehicleUIColor(float var0, float var1, float var2) {
      Color var3 = new Color(var0, var1, var2);
      EtherMain.getInstance().etherAPI.vehiclesUIColor = var3;
   }

   @LuaMethod(name = "getPlayersUIColor", global = true)
   public static Color getPlayersUIColor() {
      return EtherMain.getInstance().etherAPI.playersUIColor;
   }

   @LuaMethod(name = "setPlayersUIColor", global = true)
   public static void setPlayersUIColor(float var0, float var1, float var2) {
      Color var3 = new Color(var0, var1, var2);
      EtherMain.getInstance().etherAPI.playersUIColor = var3;
   }

   @LuaMethod(name = "setAccentUIColor", global = true)
   public static void setAccentUIColor(float var0, float var1, float var2) {
      Color var3 = new Color(var0, var1, var2);
      EtherMain.getInstance().etherAPI.mainUIAccentColor = var3;
   }

   @LuaMethod(name = "getEtherUIWidth", global = true)
   public static int getEtherUIWidth() {
      return getEtherUIConfigInt("width", 720);
   }

   @LuaMethod(name = "getEtherUIHeight", global = true)
   public static int getEtherUIHeight() {
      return getEtherUIConfigInt("height", 560);
   }

   @LuaMethod(name = "saveEtherUISize", global = true)
   public static void saveEtherUISize(int width, int height) {
      Properties config = new Properties();
      config.setProperty("width", Integer.toString(width));
      config.setProperty("height", Integer.toString(height));

      Path path = EtherPaths.resolveWritablePath("EtherHack/config/ui.properties");

      try {
         Files.createDirectories(path.getParent());

         try (FileOutputStream output = new FileOutputStream(path.toFile())) {
            config.store(output, null);
         }
      } catch (IOException e) {
         Logger.printLog("Error while saving UI config: " + e.getMessage());
      }
   }

   private static int getEtherUIConfigInt(String key, int defaultValue) {
      Properties config = new Properties();
      Path path = EtherPaths.resolveResourcePath("EtherHack/config/ui.properties");

      try (FileInputStream input = new FileInputStream(path.toFile())) {
         config.load(input);
      } catch (IOException ignored) {
         return defaultValue;
      }

      return ConfigUtils.getIntFromConfig(config, key, defaultValue);
   }

   @LuaMethod(name = "deleteConfig", global = true)
   public static void deleteConfig(String var0) {
      Path var1 = EtherPaths.resolveWritablePath("EtherHack/config/" + var0 + ".properties");

      try {
         Files.deleteIfExists(var1);
      } catch (IOException var3) {
         Logger.printLog("The file '" + var0 + "' does not exist. Deletion canceled. Exception: " + var3.getMessage());
      }

   }

   @LuaMethod(name = "getConfigList", global = true)
   public static ArrayList<String> getConfigList() {
      ArrayList<String> configFiles = new ArrayList<>();

      try {
         Path configFolderPath = EtherPaths.resolveWritablePath("EtherHack/config");

         // Create directories if they don't exist
         if (!Files.exists(configFolderPath)) {
            Files.createDirectories(configFolderPath);
            return configFiles; // Return empty list since directory was just created
         }

         List<Path> fileList = Files.list(configFolderPath)
                 .filter(file -> file.toString().endsWith(".properties"))
                 .toList();

         for(Path filePath: fileList){
            String fileName = filePath.getFileName().toString().replace(".properties","");
            configFiles.add(fileName);
         }

         return configFiles;

      } catch (IOException e) {
         Logger.printLog("An error occurred while getting the list of config files: " + e);
         return null;
      }
   }

   @LuaMethod(name = "loadConfig", global = true)
   public static void loadConfig(String var0) {
      EtherMain.getInstance().etherAPI.loadConfig(var0);
   }

   @LuaMethod(name = "saveConfig", global = true)
   public static void saveConfig(String var0) {
      EtherMain.getInstance().etherAPI.saveConfig(var0);
   }

   // Recipe and item manipulation
   @LuaMethod(name = "learnAllRecipes",global = true)
   public static void learnAllRecipes()
   {
      try
      {
         IsoPlayer player = IsoPlayer.getInstance();
         if (player != null)
         {
            ArrayList<Recipe> recipes = ScriptManager.instance.getAllRecipes();
            if (recipes != null)
            {
               for (Recipe recipe : recipes)
               {
                  if (recipe.getOriginalname() != null)
                  {
                     player.learnRecipe(recipe.getOriginalname());
                  }
               }
            }
         }
      }
      catch (Exception e)
      {
         Logger.printLog("Error in learnAllRecipes: " + e.getMessage());
      }
   }

   @LuaMethod(name = "giveItem",global = true)
   public static void giveItem(InventoryItem item, int count) {
      try {
         IsoPlayer player = IsoPlayer.getInstance();
         if (player != null) {
            for (int i = 0; i < count; i++) {
               player.getInventory().AddItem(item);
            }
         }
      } catch (Exception e) {
         Logger.printLog("Error in giveItem: " + e.getMessage());
      }
   }

   @LuaMethod(name = "giveItem",global = true)
   public static void giveItem(String var0, int var1) {
      IsoPlayer var2 = IsoPlayer.getInstance();
      if (var2 != null) {
         for(int var3 = 0; var3 < var1; ++var3) {
            var2.getInventory().AddItem(var0);
         }
      }

   }

   @LuaMethod(name = "isBypassDebugMode", global = true) public static boolean isBypassDebugMode() {return EtherMain.getInstance().etherAPI.isBypassDebugMode;}
   @LuaMethod(name = "toggleBypassDebugMode", global = true) public static void toggleBypassDebugMode(boolean var0) {EtherMain.getInstance().etherAPI.isBypassDebugMode = var0;}

   @LuaMethod(name="isAlwaysRack", global=true) public static boolean isAlwaysRack() {return EtherMain.getInstance().etherAPI.isAlwaysRack;}
   @LuaMethod(name="toggleAlwaysRack", global=true) public static void toggleAlwaysRack(boolean var0) {EtherMain.getInstance().etherAPI.isAlwaysRack = var0;}

   @LuaMethod(name="isAlwaysRoundChamber", global=true) public static boolean isAlwaysRoundChamber() {return EtherMain.getInstance().etherAPI.isAlwaysRoundChamber;}
   @LuaMethod(name="toggleAlwaysRoundChamber", global=true) public static void toggleAlwaysRoundChamber(boolean var0) {EtherMain.getInstance().etherAPI.isAlwaysRoundChamber = var0;}

   @LuaMethod(name="isAlwaysKnockdown", global=true) public static boolean isAlwaysKnockdown() {return EtherMain.getInstance().etherAPI.isAlwaysKnockdown;}
   @LuaMethod(name="toggleAlwaysKnockdown", global=true) public static void toggleAlwaysKnockdown(boolean var0) {EtherMain.getInstance().etherAPI.isAlwaysKnockdown = var0;}

   @LuaMethod(name="isAlwaysAiming", global=true) public static boolean isAlwaysAiming() {return EtherMain.getInstance().etherAPI.isAlwaysAiming;}
   @LuaMethod(name="toggleAlwaysAiming", global=true)public static void toggleAlwaysAiming(boolean var0) {EtherMain.getInstance().etherAPI.isAlwaysAiming = var0;}

   @LuaMethod(name="isAlwaysCritical", global=true) public static boolean isAlwaysCritical() {return EtherMain.getInstance().etherAPI.isAlwaysCritical;}
   @LuaMethod(name="toggleAlwaysCritical", global=true) public static void toggleAlwaysCritical(boolean var0) {EtherMain.getInstance().etherAPI.isAlwaysCritical = var0;}

   @LuaMethod(name = "isZombieDontAttack", global = true) public static boolean isZombieDontAttack() {return EtherMain.getInstance().etherAPI.isZombieDontAttack;}
   @LuaMethod(name = "toggleZombieDontAttack", global = true) public static void toggleZombieDontAttack(boolean var0) {EtherMain.getInstance().etherAPI.isZombieDontAttack = var0;}

   @LuaMethod(name = "toggleGodMod", global = true) public static void toggleGodMod(boolean var0){EtherMain.getInstance().etherAPI.godmod = var0;}
   @LuaMethod(name = "isGodMod", global = true) public static boolean isGodMod(){return EtherMain.getInstance().etherAPI.godmod;}
   
   @LuaMethod(name = "toggleInvisible", global = true) public static void toggleInvisible(boolean var0){EtherMain.getInstance().etherAPI.invisible = var0;}
   @LuaMethod(name = "isInvisible", global = true) public static boolean isInvisible(){return EtherMain.getInstance().etherAPI.invisible;}
   
   @LuaMethod(name = "toggleInvulnerable", global = true) public static void toggle(boolean var0){EtherMain.getInstance().etherAPI.invulnerable = var0;}
   @LuaMethod(name = "isInvulnerable", global = true) public static boolean isinvulnerable(){return EtherMain.getInstance().etherAPI.invulnerable;}

   @LuaMethod(name = "toggleEndurance", global = true) public static void toggleEndurance(boolean var0){EtherMain.getInstance().etherAPI.endurance = var0;}
   @LuaMethod(name = "isEndurance", global = true) public static boolean isEndurance(){return EtherMain.getInstance().etherAPI.endurance;}

   @LuaMethod(name = "toggleAmmo", global = true) public static void toggleAmmo(boolean var0){EtherMain.getInstance().etherAPI.ammo = var0;}
   @LuaMethod(name = "isAmmo", global = true) public static boolean isAmmo(){return EtherMain.getInstance().etherAPI.ammo;}

   @LuaMethod(name = "toggleCarry", global = true) public static void toggleCarry(boolean var0){EtherMain.getInstance().etherAPI.carry = var0;}
   @LuaMethod(name = "isCarry", global = true) public static boolean isCarry(){return EtherMain.getInstance().etherAPI.carry;}

   @LuaMethod(name = "toggleBuild", global = true) public static void toggleBuild(boolean var0){EtherMain.getInstance().etherAPI.build = var0;}
   @LuaMethod(name = "isBuild", global = true) public static boolean isBuild(){return EtherMain.getInstance().etherAPI.build;}

   @LuaMethod(name = "toggleFarming", global = true) public static void toggleFarming(boolean var0){EtherMain.getInstance().etherAPI.farming = var0;}
   @LuaMethod(name = "isFarming", global = true) public static boolean isFarming(){return EtherMain.getInstance().etherAPI.farming;}

   @LuaMethod(name = "toggleFishing", global = true) public static void toggleFishing(boolean var0){EtherMain.getInstance().etherAPI.fishing = var0;}
   @LuaMethod(name = "isFishing", global = true) public static boolean isFishing(){return EtherMain.getInstance().etherAPI.fishing;}

   @LuaMethod(name = "toggleHealth", global = true) public static void toggleHealth(boolean var0){EtherMain.getInstance().etherAPI.health = var0;}
   @LuaMethod(name = "isHealth", global = true)  public static boolean isHealth(){return EtherMain.getInstance().etherAPI.health;}

   @LuaMethod(name = "toggleMechanics", global = true) public static void toggleMechanics(boolean var0){EtherMain.getInstance().etherAPI.mechanics = var0;}
   @LuaMethod(name = "isMechanics", global = true)  public static boolean isMechanics(){return EtherMain.getInstance().etherAPI.mechanics;}

   @LuaMethod(name = "toggleFastMove", global = true) public static void toggleFastMove(boolean var0){EtherMain.getInstance().etherAPI.fastmove = var0;}
   @LuaMethod(name = "isFastMove", global = true)  public static boolean isFastMove(){return EtherMain.getInstance().etherAPI.fastmove;}

   @LuaMethod(name = "toggleMovables", global = true) public static void toggleMovables(boolean var0){EtherMain.getInstance().etherAPI.movable = var0;}
   @LuaMethod(name = "isMovables", global = true)  public static boolean isMovables(){return EtherMain.getInstance().etherAPI.movable;}

   @LuaMethod(name = "toggleTimedActionInstant", global = true) public static void toggleTimedActionInstant(boolean var0){EtherMain.getInstance().etherAPI.timedactioninstant = var0;}
   @LuaMethod(name = "isTimedActionInstant", global = true)  public static boolean isTimedActionInstant(){return EtherMain.getInstance().etherAPI.timedactioninstant;}

   @LuaMethod(name = "toggleKnowAllRecipes", global = true) public static void toggleKnowAllRecipes(boolean var0){EtherMain.getInstance().etherAPI.knowallrecipes = var0;}
   @LuaMethod(name = "isKnowAllRecipes", global = true)  public static boolean isKnowAllRecipes(){return EtherMain.getInstance().etherAPI.knowallrecipes;}

   @LuaMethod(name = "toggleBrushTool", global = true) public static void toggleBrushTool(boolean var0){EtherMain.getInstance().etherAPI.brushtool = var0;}
   @LuaMethod(name = "isBrushTool", global = true)  public static boolean isBrushTool(){return EtherMain.getInstance().etherAPI.brushtool;}

   @LuaMethod(name = "toggleNoClip", global = true) public static void toggleNoClip(boolean var0){EtherMain.getInstance().etherAPI.noclip = var0;}
   @LuaMethod(name = "isNoClip", global = true) public static boolean isNoClip(){return EtherMain.getInstance().etherAPI.noclip;}
   
   @LuaMethod(name = "toggleSee", global = true)  public static void toggleSee(boolean var0){EtherMain.getInstance().etherAPI.see = var0;}
   @LuaMethod(name = "isSee", global = true) public static boolean isSee(){return EtherMain.getInstance().etherAPI.see;}
   
   @LuaMethod(name = "toggleHear", global = true) public static void toggleHear(boolean var0){EtherMain.getInstance().etherAPI.hear = var0;}
   @LuaMethod(name = "isHear", global = true) public static boolean isHear(){return EtherMain.getInstance().etherAPI.hear;}
   
   @LuaMethod(name = "toggleZombiesDontAttack", global = true) public static void toggleZombiesDontAttack(boolean var0){EtherMain.getInstance().etherAPI.zombiesdontattack = var0;}
   @LuaMethod(name = "isZombiesDontAttack", global = true) public static boolean isZombiesDontAttack(){return EtherMain.getInstance().etherAPI.zombiesdontattack;}
   
   @LuaMethod(name = "toggleLootZed", global = true) public static void toggleLootZed(boolean var0){EtherMain.getInstance().etherAPI.lootzed = var0;}
   @LuaMethod(name = "isLootZed", global = true) public static boolean isLootZed(){return EtherMain.getInstance().etherAPI.lootzed;}
   
   @LuaMethod(name = "toggleLootLog", global = true) public static void toggleLoot(boolean var0){EtherMain.getInstance().etherAPI.lootlog = var0;}
   @LuaMethod(name = "isLootLog", global = true) public static boolean isLootLog(){return EtherMain.getInstance().etherAPI.lootlog;}
   
    @LuaMethod(name = "toggleDebugMenuContext", global = true) public static void toggleDebugMenuContext(boolean var0){EtherMain.getInstance().etherAPI.debugmenucontext = var0;}
   @LuaMethod(name = "isDebugMenuContext", global = true) public static boolean isDebugMenuContext(){return EtherMain.getInstance().etherAPI.debugmenucontext;}
   
   @LuaMethod(name = "toggleAnimal", global = true) public static void toggleAnimal(boolean var0){EtherMain.getInstance().etherAPI.animal = var0;}
   @LuaMethod(name = "isAnimal", global = true) public static boolean isAnimal(){return EtherMain.getInstance().etherAPI.animal;}
   
   @LuaMethod(name = "toggleAnimalExtraValues", global = true) public static void toggleAnimalExtraValues(boolean var0){EtherMain.getInstance().etherAPI.animalextravalues = var0;}
   @LuaMethod(name = "isAnimalExtraValues", global = true) public static boolean isAnimalExtraValues(){return EtherMain.getInstance().etherAPI.animalextravalues;}

   @LuaMethod(name = "isEnableNightVision", global = true) public static boolean isEnableNightVision() {return EtherMain.getInstance().etherAPI.isEnableNightVision;}
   @LuaMethod(name = "toggleNightVision", global = true) public static void toggleNightVision(boolean var0) {EtherMain.getInstance().etherAPI.isEnableNightVision = var0;}

   @LuaMethod(name = "isNoRecoil", global = true) public static boolean isNoRecoil() {return EtherMain.getInstance().etherAPI.isNoRecoil;}
   @LuaMethod(name = "toggleNoRecoil", global = true) public static void toggleNoRecoil(boolean var0) {EtherMain.getInstance().etherAPI.isNoRecoil = var0;}

   @LuaMethod(name="isNoReload", global=true) public static boolean isNoReload() {return EtherMain.getInstance().etherAPI.isNoReload;}
   @LuaMethod(name="toggleNoReload", global=true) public static void toggleNoReload(boolean var0) {EtherMain.getInstance().etherAPI.isNoReload = var0;}

   @LuaMethod(name="isNoJam", global=true) public static boolean isNoJam() {return EtherMain.getInstance().etherAPI.isNoJam;}
   @LuaMethod(name="toggleNoJam", global=true) public static void toggleNoJam(boolean var0) {EtherMain.getInstance().etherAPI.isNoJam = var0;}

   @LuaMethod(name="isNoSpentRoundChamber", global=true) public static boolean isNoSpentRoundChamber() {return EtherMain.getInstance().etherAPI.isNoSpentRoundChamber;}
   @LuaMethod(name="toggleNoSpentRoundChamber", global=true) public static void toggleNoSpentRoundChamber(boolean var0) {EtherMain.getInstance().etherAPI.isNoSpentRoundChamber = var0;}

   @LuaMethod(name = "isAutoRepairItems", global = true) public static boolean isAutoRepairItems() {return EtherMain.getInstance().etherAPI.isAutoRepairItems;}
   @LuaMethod(name = "toggleAutoRepairItems", global = true) public static void toggleAutoRepairItems(boolean var0) {EtherMain.getInstance().etherAPI.isAutoRepairItems = var0;}

   @LuaMethod(name = "resetWeaponsStats", global = true) public static void resetWeaponsStats() {EtherMain.getInstance().etherAPI.resetWeaponsStats();}
   @LuaMethod(name = "isTimedActionCheat", global = true) public static boolean isTimedActionCheat() {return EtherMain.getInstance().etherAPI.isTimedActionCheat;}
   @LuaMethod(name = "toggleTimedActionCheat", global = true) public static void toggleTimedActionCheat(boolean var0) {EtherMain.getInstance().etherAPI.isTimedActionCheat = var0;}
   
   @LuaMethod(name = "isMultiHitZombies", global = true) public static boolean isMultiHitZombies() {return EtherMain.getInstance().etherAPI.isMultiHitZombies;}
   @LuaMethod(name = "toggleMultiHitZombies", global = true) public static void toggleMultiHitZombies(boolean var0) {EtherMain.getInstance().etherAPI.isMultiHitZombies = var0;}

   @LuaMethod(name = "isUnlimitedCondition", global = true) public static boolean isUnlimitedCondition() {return EtherMain.getInstance().etherAPI.isUnlimitedCondition;}
   @LuaMethod(name = "toggleUnlimitedCondition", global = true) public static void toggleUnlimitedCondition(boolean var0) {EtherMain.getInstance().etherAPI.isUnlimitedCondition = var0;}

   @LuaMethod(name = "isMapDrawZombies", global = true) public static boolean isMapDrawZombies() {return EtherMain.getInstance().etherAPI.isMapDrawZombies;}
   @LuaMethod(name = "toggleMapDrawZombies", global = true) public static void toggleMapDrawZombies(boolean var0) {EtherMain.getInstance().etherAPI.isMapDrawZombies = var0;}

   @LuaMethod(name = "isMapDrawVehicles",global = true) public static boolean isMapDrawVehicles() {return EtherMain.getInstance().etherAPI.isMapDrawVehicles;}
   @LuaMethod(name = "toggleMapDrawVehicles", global = true) public static void toggleMapDrawVehicles(boolean var0) {EtherMain.getInstance().etherAPI.isMapDrawVehicles = var0;}

   @LuaMethod(name = "isMapDrawAllPlayers", global = true) public static boolean isMapDrawAllPlayers() {return EtherMain.getInstance().etherAPI.isMapDrawAllPlayers;}
   @LuaMethod(name = "toggleMapDrawAllPlayers", global = true) public static void toggleMapDrawAllPlayers(boolean var0) {EtherMain.getInstance().etherAPI.isMapDrawAllPlayers = var0;}

   @LuaMethod(name = "isMapDrawLocalPlayer", global = true) public static boolean isMapDrawLocalPlayer() {return EtherMain.getInstance().etherAPI.isMapDrawLocalPlayer;}
   @LuaMethod(name = "toggleMapDrawLocalPlayer", global = true) public static void toggleMapDrawLocalPlayer(boolean var0) {EtherMain.getInstance().etherAPI.isMapDrawLocalPlayer = var0;}

   @LuaMethod(name = "toggleUnlimitedAmmo", global = true) public static void toggleUnlimitedAmmo(boolean var0) {EtherMain.getInstance().etherAPI.isUnlimitedAmmo = var0;}
   @LuaMethod(name = "isUnlimitedAmmo", global = true) public static boolean isUnlimitedAmmo() {return EtherMain.getInstance().etherAPI.isUnlimitedAmmo;}

   @LuaMethod(name = "toggleEnableUnlimitedCarry", global = true) public static void toggleEnableUnlimitedCarry(boolean var0) {EtherMain.getInstance().etherAPI.isUnlimitedCarry = var0;}
   @LuaMethod(name = "isEnableUnlimitedCarry", global = true) public static boolean isEnableUnlimitedCarry() {return EtherMain.getInstance().etherAPI.isUnlimitedCarry;}

   @LuaMethod(name = "requireExtra", global = true)
   public static void requireExtra(String file) {
      try
      {
         String luaFile = file.endsWith(".lua") ? file : file + ".lua";
         String resolvedLuaFile = EtherMain.getInstance().etherLuaManager.resolveLuaFile(luaFile);
         
         if (!EtherMain.getInstance().etherLuaManager.luaFilesList.contains(luaFile))
            EtherMain.getInstance().etherLuaManager.luaFilesList.add(luaFile);
         
         LuaManager.RunLua(resolvedLuaFile);
      } catch (Exception e) {
         Logger.printLog("Error in requireExtra: " + e.getMessage());
      }
   }

   @LuaMethod(name = "getExtraTexture", global = true)
   public static Texture getExtraTexture(String path) {
      try {
         if (!path.endsWith(".png")) {
            Logger.printLog("Incorrect path to the image file. Required .png");
            return null;
         }

         ConcurrentHashMap<String, Texture> textureCache = EtherMain.getInstance().etherAPI.textureCache;
         String resolvedPath = EtherPaths.resolveResourcePathString(path);

         if (textureCache.containsKey(resolvedPath)) {
            return textureCache.get(resolvedPath);
         }

         try (FileInputStream fis = new FileInputStream(Paths.get(resolvedPath).toFile());
              BufferedInputStream bis = new BufferedInputStream(fis)) {
            Texture texture = new Texture(resolvedPath, bis, false);
            textureCache.put(resolvedPath, texture);
            return texture;
         }
      } catch (Exception e) {
         Logger.printLog("Error reading image: " + e.getMessage());
         return null;
      }
   }

   @LuaMethod(name = "getTranslate", global = true)
   public static String getTranslate(String var0, KahluaTable var1) {
      return EtherMain.getInstance().etherTranslator.getTranslate(var0, var1);
   }

   @LuaMethod(name = "getTranslate", global = true)
   public static String getTranslate(String var0) {
      return EtherMain.getInstance().etherTranslator.getTranslate(var0);
   }

   @LuaMethod(name = "getAccess", global = true)
   public static String getAccess()
   {
      IsoPlayer player = IsoPlayer.getInstance();
      if (player != null)
         return player.getAccessLevel();

      return "none";
   }
   
   @LuaMethod(name = "setAccess", global = true)
   public static void setAccess(String level)
   {
      IsoPlayer player = IsoPlayer.getInstance();
      if (player != null)
         player.accessLevel = level;
   }

   @LuaMethod(name = "setZombieKills", global = true)
   public static void setZombieKills(int kills) {
      IsoPlayer player = IsoPlayer.getInstance();
      if (player != null)
         player.setZombieKills(kills);

   }

   @LuaMethod(name = "setHoursAlive", global = true)
   public static void setHoursAlive(int hours) {
      IsoPlayer player = IsoPlayer.getInstance();
      if (player != null)
         player.setHoursSurvived(hours);

   }

   @LuaMethod(name = "getZombieKills", global = true)
   public static int getZombieKills() {
      IsoPlayer player = IsoPlayer.getInstance();
      if (player != null)
         return player.getZombieKills();

      return 0;
   }

   @LuaMethod(name = "getHoursAlive", global = true)
   public static int getHoursAlive() {
      IsoPlayer player = IsoPlayer.getInstance();
      if (player != null)
         return (int) player.getHoursSurvived();

      return 0;
   }

   @LuaMethod(name = "getAccentUIColor", global = true)
   public static Color getAccentUIColor() {
      return EtherMain.getInstance().etherAPI.mainUIAccentColor;
   }

   // Singleton pattern
   public static EtherLuaMethods getInstance() {
      if (instance == null)
         instance = new EtherLuaMethods();
      
      return instance;
   }
}
