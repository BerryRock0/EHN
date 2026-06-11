package EtherHack.Ether;

import EtherHack.annotations.LuaEvents;
import EtherHack.annotations.SubscribeLuaEvent;
import EtherHack.utils.ColorUtils;
import EtherHack.utils.ConfigUtils;
import EtherHack.utils.EtherPaths;
import EtherHack.utils.EventSubscriber;
import EtherHack.utils.Exposer;
import EtherHack.utils.Logger;
import EtherHack.utils.Rendering;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


import se.krka.kahlua.converter.KahluaConverterManager;
import se.krka.kahlua.j2se.J2SEPlatform;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.Platform;
import zombie.SandboxOptions;
import zombie.Lua.LuaManager;
import zombie.characterTextures.BloodBodyPartType;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoZombie;
import zombie.core.Color;
import zombie.core.Core;
import zombie.core.textures.Texture;
import zombie.inventory.InventoryItem;
import zombie.inventory.types.HandWeapon;
import zombie.iso.IsoWorld;
import zombie.ui.UIFont;
import zombie.vehicles.BaseVehicle;

import static zombie.Lua.LuaManager.env;

public class EtherAPI {
   private Exposer exposer;
   final ConcurrentHashMap<String, Texture> textureCache = new ConcurrentHashMap<>();
   private final ConcurrentHashMap<String, float[]> originalWeaponStats = new ConcurrentHashMap<>();
   public Color mainUIAccentColor;
   public Color vehiclesUIColor;
   public Color zombiesUIColor;
   public Color playersUIColor;
   public boolean isBypassDebugMode;
   public boolean isAlwaysRack;
   public boolean isAlwaysRoundChamber;
   public boolean isAlwaysKnockdown;
   public boolean isAlwaysCritical;
   public boolean isAlwaysAiming;
   public boolean isPlayerInSafeTeleported;
   public boolean isMultiHitZombies;
   public boolean isTimedActionCheat;
   public boolean isEnableNightVision;
   public boolean isZombieDontAttack;
   public boolean isNoRecoil;
   public boolean isNoReload;
   public boolean isNoJam;
   public boolean isNoSpentRoundChamber;
   public boolean isUnlimitedCarry;
   public boolean isUnlimitedCondition;
   public boolean isUnlimitedAmmo;
   public boolean isAutoRepairItems;
   public boolean isMapDrawLocalPlayer;
   public boolean isMapDrawAllPlayers;
   public boolean isMapDrawVehicles;
   public boolean isMapDrawZombies;
   public boolean godmod;
   public boolean invisible;
   public boolean invulnerable;
   public boolean endurance;
   public boolean ammo;
   public boolean carry;
   public boolean build;
   public boolean farming;
   public boolean fishing;
   public boolean health;
   public boolean mechanics;
   public boolean fastmove;
   public boolean movables;
   public boolean timedactioninstant;
   public boolean knowallrecipes;
   public boolean brushtool;
   public boolean noclip;
   public boolean see;
   public boolean hear;
   public boolean zombiesdontattack;
   public boolean lootzed;
   public boolean lootlog;
   public boolean debugmenucontext;
   public boolean animal;
   public boolean animalextravalues;

   

   public void saveConfig(String var1) {
      String var2 = EtherPaths.resolveWritablePath("EtherHack/config/" + var1 + ".properties").toString();
      Properties var3 = new Properties();
      var3.setProperty("mainUIAccentColor", ColorUtils.colorToString(this.mainUIAccentColor));
      var3.setProperty("vehiclesUIColor", ColorUtils.colorToString(this.vehiclesUIColor));
      var3.setProperty("zombiesUIColor", ColorUtils.colorToString(this.zombiesUIColor));
      var3.setProperty("playersUIColor", ColorUtils.colorToString(this.playersUIColor));
      var3.setProperty("isBypassDebugMode", Boolean.toString(this.isBypassDebugMode));
      var3.setProperty("isAlwaysRack", Boolean.toString(this.isAlwaysRack));
      var3.setProperty("isAlwaysRoundChamber", Boolean.toString(this.isAlwaysRoundChamber));
      var3.setProperty("isAlwaysKnockdown", Boolean.toString(this.isAlwaysKnockdown));
      var3.setProperty("isAlwaysAiming", Boolean.toString(this.isAlwaysAiming));
      var3.setProperty("isAlwaysCritical", Boolean.toString(this.isAlwaysCritical));
      var3.setProperty("isPlayerInSafeTeleported", Boolean.toString(this.isPlayerInSafeTeleported));
      var3.setProperty("isMultiHitZombies", Boolean.toString(this.isMultiHitZombies));
      var3.setProperty("isPlayerInSafeTeleported", Boolean.toString(this.isPlayerInSafeTeleported));
      var3.setProperty("isMultiHitZombies", Boolean.toString(this.isMultiHitZombies));
      var3.setProperty("isEnableNightVision", Boolean.toString(this.isEnableNightVision));
      var3.setProperty("isZombieDontAttack", Boolean.toString(this.isZombieDontAttack));
      var3.setProperty("isNoRecoil", Boolean.toString(this.isNoRecoil));
      var3.setProperty("isNoReload", Boolean.toString(this.isNoReload));
      var3.setProperty("isNoJam", Boolean.toString(this.isNoJam));
      var3.setProperty("isNoSpentRoundChamber", Boolean.toString(this.isNoSpentRoundChamber));
      var3.setProperty("isUnlimitedCarry", Boolean.toString(this.isUnlimitedCarry));
      var3.setProperty("isUnlimitedCondition", Boolean.toString(this.isUnlimitedCondition));
      var3.setProperty("isUnlimitedAmmo", Boolean.toString(this.isUnlimitedAmmo));
      var3.setProperty("isAutoRepairItems", Boolean.toString(this.isAutoRepairItems));
      var3.setProperty("isMapDrawLocalPlayer", Boolean.toString(this.isMapDrawLocalPlayer));
      var3.setProperty("isMapDrawAllPlayers", Boolean.toString(this.isMapDrawAllPlayers));
      var3.setProperty("isMapDrawVehicles", Boolean.toString(this.isMapDrawVehicles));
      var3.setProperty("isMapDrawZombies", Boolean.toString(this.isMapDrawZombies));
      var3.setProperty("godmod", Boolean.toString(this.));
      var3.setProperty("invisible", Boolean.toString(this.));
      var3.setProperty("invulnerable", Boolean.toString(this.));
      var3.setProperty("endurance", Boolean.toString(this.));
      var3.setProperty("ammo", Boolean.toString(this.));
      var3.setProperty("carry", Boolean.toString(this.));
      var3.setProperty("build", Boolean.toString(this.));
      var3.setProperty("farming", Boolean.toString(this.));
      var3.setProperty("fishing", Boolean.toString(this.));
      var3.setProperty("health", Boolean.toString(this.));
      var3.setProperty("mechanics", Boolean.toString(this.));
      var3.setProperty("fastmove", Boolean.toString(this.));
      var3.setProperty("movables", Boolean.toString(this.));
      var3.setProperty("timedactioninstant", Boolean.toString(this.));
      var3.setProperty("knowallrecipes", Boolean.toString(this.));
      var3.setProperty("brushtool", Boolean.toString(this.));
      var3.setProperty("noclip", Boolean.toString(this.));
      var3.setProperty("see", Boolean.toString(this.));
      var3.setProperty("hear", Boolean.toString(this.));
      var3.setProperty("zombiesdontattack", Boolean.toString(this.));
      var3.setProperty("lootzed", Boolean.toString(this.));
      var3.setProperty("lootlog", Boolean.toString(this.));
      var3.setProperty("debugmenucontext", Boolean.toString(this.));
      var3.setProperty("animal", Boolean.toString(this.));
      var3.setProperty("animalextravalues", Boolean.toString(this.));

      try {
         java.nio.file.Files.createDirectories(java.nio.file.Path.of(var2).getParent());
         FileOutputStream var4 = new FileOutputStream(var2);

         try {
            var3.store(var4, (String)null);
         } catch (Throwable var8) {
            try {
               var4.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }

            throw var8;
         }

         var4.close();
      } catch (IOException var9) {
         Logger.printLog("Error while saving config: " + var9);
      }

   }

   public void loadConfig(String var1) {
      String var2 = EtherPaths.resolveResourcePathString("EtherHack/config/" + var1 + ".properties");
      Properties var3 = new Properties();

      try {
         FileInputStream var4 = new FileInputStream(var2);

         try {
            var3.load(var4);
         } catch (Throwable var8) {
            try {
               var4.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }

            throw var8;
         }

         var4.close();
      } catch (IOException var9) {
         Logger.printLog("The config file was not found. Loading canceled.");
         return;
      }
      
      //this. = ConfigUtils.getBooleanFromConfig(var3, "", false);
      this.mainUIAccentColor = ConfigUtils.getColorFromConfig(var3, "mainUIAccentColor", new Color(56, 239, 125));
      this.vehiclesUIColor = ConfigUtils.getColorFromConfig(var3, "vehiclesUIColor", new Color(150, 150, 200));
      this.zombiesUIColor = ConfigUtils.getColorFromConfig(var3, "zombiesUIColor", new Color(255, 150, 100));
      this.playersUIColor = ConfigUtils.getColorFromConfig(var3, "playersUIColor", new Color(255, 50, 100));
      this.isBypassDebugMode = ConfigUtils.getBooleanFromConfig(var3, "isBypassDebugMode", false);
      this.isAlwaysRack = ConfigUtils.getBooleanFromConfig(var3, (String)"isAlwaysRack", false);
      this.isAlwaysRoundChamber = ConfigUtils.getBooleanFromConfig(var3, "isAlwaysRoundChamber", false);
      this.isAlwaysKnockdown = ConfigUtils.getBooleanFromConfig(var3, "isAlwaysKnockdown", false);
      this.isAlwaysAiming = ConfigUtils.getBooleanFromConfig(var3, "isAlwaysAiming", false);
      this.isAlwaysCritical = ConfigUtils.getBooleanFromConfig(var3, "isAlwaysCritical", false);
      this.isPlayerInSafeTeleported = ConfigUtils.getBooleanFromConfig(var3, "isPlayerInSafeTeleported", false);
      this.isMultiHitZombies = ConfigUtils.getBooleanFromConfig(var3, "isMultiHitZombies", false);
      this.isEnableNightVision = ConfigUtils.getBooleanFromConfig(var3, "isEnableNightVision", false);
      this.isZombieDontAttack = ConfigUtils.getBooleanFromConfig(var3, "isZombieDontAttack", false);
      this.isNoRecoil = ConfigUtils.getBooleanFromConfig(var3, "isNoRecoil", false);
      this.isNoReload = ConfigUtils.getBooleanFromConfig(var3, "isNoReload", false);
      this.isNoJam = ConfigUtils.getBooleanFromConfig(var3, "isNoJam", false);
      this.isNoSpentRoundChamber = ConfigUtils.getBooleanFromConfig(var3, "isNoSpentRoundChamber", false);
      this.isUnlimitedCarry = ConfigUtils.getBooleanFromConfig(var3, "isUnlimitedCarry", false);
      this.isUnlimitedCondition = ConfigUtils.getBooleanFromConfig(var3, "isUnlimitedCondition", false);
      this.isUnlimitedAmmo = ConfigUtils.getBooleanFromConfig(var3, "isUnlimitedAmmo", false);
      this.isAutoRepairItems = ConfigUtils.getBooleanFromConfig(var3, "isAutoRepairItems", false);
      this.isMapDrawLocalPlayer = ConfigUtils.getBooleanFromConfig(var3, "isMapDrawLocalPlayer", true);
      this.isMapDrawAllPlayers = ConfigUtils.getBooleanFromConfig(var3, "isMapDrawAllPlayers", false);
      this.isMapDrawVehicles = ConfigUtils.getBooleanFromConfig(var3, "isMapDrawVehicles", false);
      this.isMapDrawZombies = ConfigUtils.getBooleanFromConfig(var3, "isMapDrawZombies", false);
      this.godmod = ConfigUtils.getBooleanFromConfig(var3, "godmod", false);
      this.invisible = ConfigUtils.getBooleanFromConfig(var3, "invisible", false);
      this.invulnerable = ConfigUtils.getBooleanFromConfig(var3, "endurance", false);
      this.endurance = ConfigUtils.getBooleanFromConfig(var3, "", false);
      this.ammo = ConfigUtils.getBooleanFromConfig(var3, "ammo", false);
      this.carry = ConfigUtils.getBooleanFromConfig(var3, "carry", false);
      this.build = ConfigUtils.getBooleanFromConfig(var3, "build", false);
      this.farming = ConfigUtils.getBooleanFromConfig(var3, "farming", false);
      this.fishing = ConfigUtils.getBooleanFromConfig(var3, "fishing", false);
      this.health = ConfigUtils.getBooleanFromConfig(var3, "health", false);
      this.mechanics = ConfigUtils.getBooleanFromConfig(var3, "mechanics", false);
      this.fastmove = ConfigUtils.getBooleanFromConfig(var3, "fastmove", false);
      this.movables = ConfigUtils.getBooleanFromConfig(var3, "movables", false);
      this.timedactioninstant = ConfigUtils.getBooleanFromConfig(var3, "timedactioninstant", false);
      this.knowallrecipes = ConfigUtils.getBooleanFromConfig(var3, "knowallrecipes", false);
      this.brushtool = ConfigUtils.getBooleanFromConfig(var3, "brushtool", false);
      this.noclip = ConfigUtils.getBooleanFromConfig(var3, "noclip", false);
      this.see = ConfigUtils.getBooleanFromConfig(var3, "see", false);
      this.hear = ConfigUtils.getBooleanFromConfig(var3, "hear", false);
      this.zombiesdontattack = ConfigUtils.getBooleanFromConfig(var3, "zombiesdontattack", false);
      this.lootzed = ConfigUtils.getBooleanFromConfig(var3, "lootzed", false);
      this.lootlog = ConfigUtils.getBooleanFromConfig(var3, "lootlog", false);
      this.debugmenucontext = ConfigUtils.getBooleanFromConfig(var3, "debugmenucontext", false);
      this.animal = ConfigUtils.getBooleanFromConfig(var3, "animal", false);
      this.animalextravalues = ConfigUtils.getBooleanFromConfig(var3, "animalextravalues", false);
   }

   private void initStartupConfig() {
      Properties var1 = new Properties();

      try {
         FileInputStream var2 = new FileInputStream(EtherPaths.resolveResourcePathString("EtherHack/config/startup.properties"));

         try {
            var1.load(var2);
         } catch (Throwable var6) {
            try {
               var2.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }

            throw var6;
         }

         var2.close();
      } catch (IOException var7) {
         Logger.printLog("Startup file not found. Loading default settings.");
      }
      
      //this. = ConfigUtils.getBooleanFromConfig(var3, "", false);
      this.mainUIAccentColor = ConfigUtils.getColorFromConfig(var1, "mainUIAccentColor", new Color(56, 239, 125));
      this.vehiclesUIColor = ConfigUtils.getColorFromConfig(var1, "vehiclesUIColor", new Color(150, 150, 200));
      this.zombiesUIColor = ConfigUtils.getColorFromConfig(var1, "zombiesUIColor", new Color(255, 150, 100));
      this.playersUIColor = ConfigUtils.getColorFromConfig(var1, "playersUIColor", new Color(255, 50, 100));
      this.isBypassDebugMode = ConfigUtils.getBooleanFromConfig(var1, "isBypassDebugMode", false);
      this.isPlayerInSafeTeleported = ConfigUtils.getBooleanFromConfig(var1, "isPlayerInSafeTeleported", false);
      this.isMultiHitZombies = ConfigUtils.getBooleanFromConfig(var1, "isMultiHitZombies", false);
      this.isEnableNightVision = ConfigUtils.getBooleanFromConfig(var1, "isEnableNightVision", false);
      this.isZombieDontAttack = ConfigUtils.getBooleanFromConfig(var1, "isZombieDontAttack", false);
      this.isNoRecoil = ConfigUtils.getBooleanFromConfig(var1, "isNoRecoil", false);
      this.isNoReload = ConfigUtils.getBooleanFromConfig(var1, "isNoReload", false);
      this.isNoJam = ConfigUtils.getBooleanFromConfig(var1, "isNoJam", false);
      this.isNoSpentRoundChamber = ConfigUtils.getBooleanFromConfig(var1, "isNoSpentRoundChamber", false);
      this.isUnlimitedCarry = ConfigUtils.getBooleanFromConfig(var1, "isUnlimitedCarry", false);
      this.isUnlimitedCondition = ConfigUtils.getBooleanFromConfig(var1, "isUnlimitedCondition", false);
      this.isUnlimitedAmmo = ConfigUtils.getBooleanFromConfig(var1, "isUnlimitedAmmo", false);
      this.isAutoRepairItems = ConfigUtils.getBooleanFromConfig(var1, "isAutoRepairItems", false);
      this.isMapDrawLocalPlayer = ConfigUtils.getBooleanFromConfig(var1, "isMapDrawLocalPlayer", true);
      this.isMapDrawAllPlayers = ConfigUtils.getBooleanFromConfig(var1, "isMapDrawAllPlayers", false);
      this.isMapDrawVehicles = ConfigUtils.getBooleanFromConfig(var1, "isMapDrawVehicles", false);
      this.isMapDrawZombies = ConfigUtils.getBooleanFromConfig(var1, "isMapDrawZombies", false);
      this.godmod = ConfigUtils.getBooleanFromConfig(var1, "godmod", false);
      this.invisible = ConfigUtils.getBooleanFromConfig(var1, "invisible", false);
      this.invulnerable = ConfigUtils.getBooleanFromConfig(var1, "endurance", false);
      this.endurance = ConfigUtils.getBooleanFromConfig(var1, "", false);
      this.ammo = ConfigUtils.getBooleanFromConfig(var1, "ammo", false);
      this.carry = ConfigUtils.getBooleanFromConfig(var1, "carry", false);
      this.build = ConfigUtils.getBooleanFromConfig(var1, "build", false);
      this.farming = ConfigUtils.getBooleanFromConfig(var1, "farming", false);
      this.fishing = ConfigUtils.getBooleanFromConfig(var1, "fishing", false);
      this.health = ConfigUtils.getBooleanFromConfig(var1, "health", false);
      this.mechanics = ConfigUtils.getBooleanFromConfig(var1, "mechanics", false);
      this.fastmove = ConfigUtils.getBooleanFromConfig(var1, "fastmove", false);
      this.movables = ConfigUtils.getBooleanFromConfig(var1, "movables", false);
      this.timedactioninstant = ConfigUtils.getBooleanFromConfig(var1, "timedactioninstant", false);
      this.knowallrecipes = ConfigUtils.getBooleanFromConfig(var1, "knowallrecipes", false);
      this.brushtool = ConfigUtils.getBooleanFromConfig(var1, "brushtool", false);
      this.noclip = ConfigUtils.getBooleanFromConfig(var1, "noclip", false);
      this.see = ConfigUtils.getBooleanFromConfig(var1, "see", false);
      this.hear = ConfigUtils.getBooleanFromConfig(var1, "hear", false);
      this.zombiesdontattack = ConfigUtils.getBooleanFromConfig(var1, "zombiesdontattack", false);
      this.lootzed = ConfigUtils.getBooleanFromConfig(var1, "lootzed", false);
      this.lootlog = ConfigUtils.getBooleanFromConfig(var1, "lootlog", false);
      this.debugmenucontext = ConfigUtils.getBooleanFromConfig(var1, "debugmenucontext", false);
      this.animal = ConfigUtils.getBooleanFromConfig(var1, "animal", false);
      this.animalextravalues = ConfigUtils.getBooleanFromConfig(var1, "animalextravalues", false);
   }

   public EtherAPI() {
      this.initStartupConfig();
      EventSubscriber.register(this);
   }

   @LuaEvents({
           @SubscribeLuaEvent(eventName = "OnResetLua"),
           @SubscribeLuaEvent(eventName = "OnMainMenuEnter")
   })
   public void loadAPI() {
      Logger.printLog("Loading EtherAPI...");

      if (this.exposer != null) {
         this.exposer.destroy();
      }

      this.exposer = new SafeExposer(LuaManager.converterManager,
              LuaManager.platform,
              LuaManager.env);

      this.exposer.exposeAPI(new EtherLuaMethods());
   }

   // Inner class for safe method exposure
   private class SafeExposer extends Exposer {
      public SafeExposer(KahluaConverterManager m, Platform p, KahluaTable e) {
         super(m, (J2SEPlatform) p, e);
      }

      public void exposeAPI(EtherLuaMethods methods) {
         exposeGlobalFunctions(methods);
      }
   }

   public void resetWeaponsStats() {
      IsoPlayer var1 = IsoPlayer.getInstance();
      if (var1 != null) {
         ArrayList var2 = var1.getInventory().getItems();
         if (var2 != null && !var2.isEmpty()) {
            Iterator var3 = var2.iterator();

            while(true) {
               InventoryItem var4;
               HandWeapon var5;
               do {
                  do {
                     if (!var3.hasNext()) {
                        return;
                     }

                     var4 = (InventoryItem)var3.next();
                  } while(!(var4 instanceof HandWeapon));

                  var5 = (HandWeapon)var4;
               } while(!var4.getStringItemType().equals("RangedWeapon") && !var4.getStringItemType().equals("MeleeWeapon"));

               String var6 = var5.getFullType();
               if (this.originalWeaponStats.containsKey(var6)) {
                  float[] var7 = (float[])this.originalWeaponStats.get(var6);
                  var5.setExtraDamage(var7[0]);
                  var5.setMaxDamage(var7[1]);
                  var5.setMinDamage(var7[2]);
                  var5.setMaxRange(var7[3]);
                  var5.setMinRange(var7[4]);
                  var5.setHitChance((int)var7[5]);
               }
            }
         }
      }
   }

   private void updateCharacterPrivilegies()
   {
      IsoPlayer var1 = IsoPlayer.getInstance();

      if (var1.isGodMod() != this.godmod) var1.setGodMod(this.godmod);
      if (var1.isInvisible() != this.invisible) var1.setInvisible(this.invisible);
      if (var1.isInvulnerable() != this.invulnerable) var1.setInvulnerable(this.invulnerable);      
      if (var1.isCanUseBrushTool() != this.brushtool) var1.setCanUseBrushTool(this.brushtool);
      if (var1.isCanUseDebugMenuContext() != this.debugmenucontext) var1.setCanUseDebugMenuContext(this.debugmenucontext);
      if (var1.isZombiesDontAttack() != this.zombiesdontattack) var1.setZombiesDontAttack(this.zombiesdontattack);
      if (var1.isUnlimitedCarry() != this.carry) var1.setUnlimitedCarry(this.carry);
      if (var1.isBuildCheat() != this.build) var1.setBuildCheat(this.build);
      if (var1.isFarmingCheat() != this.farming) var1.setFarmingCheat(this.farming);
      if (var1.isFishingCheat() != this.fishing) var1.setFishingCheat(this.fishing);
      if (var1.isHealthCheat() != this.health) var1.setHealthCheat(this.health);
      if (var1.isMechanicsCheat() != this.mechanics) var1.setMechanicsCheat(this.mechanics);
      if (var1.isFastMoveCheat() != this.fastmove) var1.setFastMoveCheat(this.fastmove);
      if (var1.isMovablesCheat() != this.movables) var1.setMovablesCheat(this.movables);
      if (var1.isAnimalCheat() != this.animal) var1.setAnimalCheat(this.animal);
      if (var1.isAnimalExtraValuesCheat() != this.animalextravalues) var1.setAnimalExtraValuesCheat(this.animalextravalues);
      if (var1.isTimedActionInstantCheat() != this.timedactioninstant) var1.setTimedActionInstantCheat(this.timedactioninstant);
      if (var1.isKnowAllRecipes() != this.knowallrecipes) var1.setKnowAllRecipes(this.knowallrecipes);
      if (var1.isUnlimitedAmmo() != this.ammo) var1.setUnlimitedAmmo(this.ammo);
      if (var1.isUnlimitedEndurance() != this.endurance) var1.setUnlimitedEndurance(this.endurance);
      if (var1.canUseLootZed() != this.lootzed) var1.setCanUseLootZed(this.lootzed);
      if (var1.canUseLootLog() != this.lootlog) var1.setCanUseLootLog(this.lootlog);
      if (var1.canSeeAll() != this.see) var1.setCanSeeAll(this.see);
      if (var1.canHearAll() != this.hear) var1.setCanHearAll(this.hear);
   }

   
   private void updateLocalPlayerFeatures()
   {
      IsoPlayer var1 = IsoPlayer.getInstance();
      InventoryItem var2 = var1.getPrimaryHandItem();
      HandWeapon var3 = (HandWeapon)var2;
      String var4 = var3.getFullType();
      
      if (var1 == null)
         return;

         if ((Boolean)SandboxOptions.instance.getOptionByName("MultiHitZombies").asConfigOption().getValueAsObject() != this.isMultiHitZombies)
            SandboxOptions.instance.set("MultiHitZombies", this.isMultiHitZombies);


         if (var1.isWearingNightVisionGoggles() != this.isEnableNightVision)
            var1.setWearingNightVisionGoggles(this.isEnableNightVision);


         if (var2 != null && var2.getStringItemType().equals("RangedWeapon") && var2 instanceof HandWeapon)
         {
            if(this.isAlwaysKnockdown)
                var3.setAlwaysKnockdown(true);
            
            if(this.isAlwaysCritical)
                var3.setCriticalChance(100.0f);
            
            if(this.isAlwaysRack)
                var3.setRackAfterShoot(true);
            
            if(this.isNoJam)
                var3.setJammed(false);
            
            if(this.isAlwaysRoundChamber)
                var3.setRoundChambered(true);
            
            if(this.isNoSpentRoundChamber)
                var3.setSpentRoundChambered(false);
            
            if(this.isAlwaysAiming)
                var3.setAimingTime(0);
            
            if(this.isNoRecoil)
                var3.setRecoilDelay(0);
            
            if(this.isNoReload)
                var3.setReloadTime(0);

            if (this.isUnlimitedAmmo)
                var2.setCurrentAmmoCount(var2.getMaxAmmo());
         }

         if (this.isUnlimitedCondition && var2 != null)
         {
            var2.setHaveBeenRepaired(1);
            var2.setCondition(var2.getConditionMax());
         }

         if (this.isAutoRepairItems) {
            ArrayList var7 = var1.getInventory().getItems();
            if (var7 != null && !var7.isEmpty()) {
               Iterator var8 = var7.iterator();

               label175:
               while(true) {
                  InventoryItem var5;
                  do {
                     if (!var8.hasNext()) {
                        break label175;
                     }

                     var5 = (InventoryItem)var8.next();
                  } while(var5 == null);

                  if (var5.isBroken()) {
                     var5.setBroken(false);
                  }

                  var5.setHaveBeenRepaired(1);
                  if (var5.getVisual() != null) {
                     for(int var6 = 0; var6 < BloodBodyPartType.MAX.index(); ++var6) {
                        var5.getVisual().removeHole(var6);
                        var5.getVisual().removeDirt();
                        var5.getVisual().removeBlood();
                     }
                  }

                  var5.setWet(false);
                  var5.setInfected(false);
                  var5.setCondition(var5.getConditionMax());
               }
            }
         }
   }

      private void bypassDebugMode()
      {
         Core.debug = this.isBypassDebugMode;
      }

   @SubscribeLuaEvent(eventName = "OnRenderTick")
   public synchronized void updateAPI() {
      try {
         updateLocalPlayerFeatures();
      } catch (Exception e) {
      }
   }
}
