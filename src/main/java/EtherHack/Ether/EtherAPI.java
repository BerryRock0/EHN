package EtherHack.Ether;

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


import EtherHack.annotations.LuaEvents;
import EtherHack.annotations.SubscribeLuaEvent;
import EtherHack.utils.ColorUtils;
import EtherHack.utils.ConfigUtils;
import EtherHack.utils.EtherPaths;
import EtherHack.utils.EventSubscriber;
import EtherHack.utils.Exposer;
import EtherHack.utils.Logger;
import EtherHack.utils.Rendering;

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
   public boolean isAlwaysRepaired;
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
   public boolean isNoBroken;
   public boolean isNoInfected;
   public boolean isNoWet;
   public boolean isNoHoled;
   public boolean isNoDirted;
   public boolean isNoBlooded;
   public boolean isFastCorpseDrag;
   public boolean isUnlimitedCarry;
   public boolean isUnlimitedCondition;
   public boolean isUnlimitedAmmo;
   public boolean isMapDrawLocalPlayer;
   public boolean isMapDrawAllPlayers;
   public boolean isMapDrawVehicles;
   public boolean isMapDrawZombies;

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
      var3.setProperty("isAlwaysRepaired", Boolean.toString(this.isAlwaysRepaired));
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
      var3.setProperty("isNoBroken", Boolean.toString(this.isNoBroken));
      var3.setProperty("isNoInfected", Boolean.toString(this.isNoInfected));
      var3.setProperty("isNoWet", Boolean.toString(this.isNoWet)); 
      var3.setProperty("isNoHoled", Boolean.toString(this.isNoHoled));
      var3.setProperty("isNoDirted", Boolean.toString(this.isNoDirted));
      var3.setProperty("isNoBlooded", Boolean.toString(this.isNoBlooded));
      var3.setProperty("isUnlimitedCarry", Boolean.toString(this.isUnlimitedCarry));
      var3.setProperty("isUnlimitedCondition", Boolean.toString(this.isUnlimitedCondition));
      var3.setProperty("isUnlimitedAmmo", Boolean.toString(this.isUnlimitedAmmo));
      var3.setProperty("isAutoRepairItems", Boolean.toString(this.isAutoRepairItems));
      var3.setProperty("isMapDrawLocalPlayer", Boolean.toString(this.isMapDrawLocalPlayer));
      var3.setProperty("isMapDrawAllPlayers", Boolean.toString(this.isMapDrawAllPlayers));
      var3.setProperty("isMapDrawVehicles", Boolean.toString(this.isMapDrawVehicles));
      var3.setProperty("isMapDrawZombies", Boolean.toString(this.isMapDrawZombies));

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
      this.isAlwaysRepaired = ConfigUtils.getBooleanFromConfig(var3, "isAlwaysRepaired", false);
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
      this.isNoBroken = ConfigUtils.getBooleanFromConfig(var3, "isNoBroken", false);
      this.isNoInfected = ConfigUtils.getBooleanFromConfig(var3, "isNoInfected", false);
      this.isNoWet = ConfigUtils.getBooleanFromConfig(var3, "isNoWet", false);
      this.isNoHoled = ConfigUtils.getBooleanFromConfig(var3, "isNoHoled", false);      
      this.isNoDirted = ConfigUtils.getBooleanFromConfig(var3, "isNoDirted", false);
      this.isNoBlooded = ConfigUtils.getBooleanFromConfig(var3, "isNoBlooded", false);
      this.isUnlimitedCarry = ConfigUtils.getBooleanFromConfig(var3, "isUnlimitedCarry", false);
      this.isUnlimitedCondition = ConfigUtils.getBooleanFromConfig(var3, "isUnlimitedCondition", false);
      this.isUnlimitedAmmo = ConfigUtils.getBooleanFromConfig(var3, "isUnlimitedAmmo", false);
      this.isAutoRepairItems = ConfigUtils.getBooleanFromConfig(var3, "isAutoRepairItems", false);
      this.isMapDrawLocalPlayer = ConfigUtils.getBooleanFromConfig(var3, "isMapDrawLocalPlayer", true);
      this.isMapDrawAllPlayers = ConfigUtils.getBooleanFromConfig(var3, "isMapDrawAllPlayers", false);
      this.isMapDrawVehicles = ConfigUtils.getBooleanFromConfig(var3, "isMapDrawVehicles", false);
      this.isMapDrawZombies = ConfigUtils.getBooleanFromConfig(var3, "isMapDrawZombies", false);
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
      
      //this. = ConfigUtils.getBooleanFromConfig(var1, "", false);
      this.mainUIAccentColor = ConfigUtils.getColorFromConfig(var1, "mainUIAccentColor", new Color(56, 239, 125));
      this.vehiclesUIColor = ConfigUtils.getColorFromConfig(var1, "vehiclesUIColor", new Color(150, 150, 200));
      this.zombiesUIColor = ConfigUtils.getColorFromConfig(var1, "zombiesUIColor", new Color(255, 150, 100));
      this.playersUIColor = ConfigUtils.getColorFromConfig(var1, "playersUIColor", new Color(255, 50, 100));
      this.isBypassDebugMode = ConfigUtils.getBooleanFromConfig(var1, "isBypassDebugMode", false);
      this.isAlwaysRack = ConfigUtils.getBooleanFromConfig(var1, (String)"isAlwaysRack", false);
      this.isAlwaysRoundChamber = ConfigUtils.getBooleanFromConfig(var1, "isAlwaysRoundChamber", false);
      this.isAlwaysRepaired = ConfigUtils.getBooleanFromConfig(var1, "isAlwaysRepaired", false);
      this.isAlwaysKnockdown = ConfigUtils.getBooleanFromConfig(var1, "isAlwaysKnockdown", false);
      this.isAlwaysAiming = ConfigUtils.getBooleanFromConfig(var1, "isAlwaysAiming", false);
      this.isAlwaysCritical = ConfigUtils.getBooleanFromConfig(var1, "isAlwaysCritical", false);
      this.isPlayerInSafeTeleported = ConfigUtils.getBooleanFromConfig(var1, "isPlayerInSafeTeleported", false);
      this.isMultiHitZombies = ConfigUtils.getBooleanFromConfig(var1, "isMultiHitZombies", false);
      this.isEnableNightVision = ConfigUtils.getBooleanFromConfig(var1, "isEnableNightVision", false);
      this.isZombieDontAttack = ConfigUtils.getBooleanFromConfig(var1, "isZombieDontAttack", false);
      this.isNoRecoil = ConfigUtils.getBooleanFromConfig(var1, "isNoRecoil", false);
      this.isNoReload = ConfigUtils.getBooleanFromConfig(var1, "isNoReload", false);
      this.isNoJam = ConfigUtils.getBooleanFromConfig(var1, "isNoJam", false);
      this.isNoSpentRoundChamber = ConfigUtils.getBooleanFromConfig(var1, "isNoSpentRoundChamber", false);
      this.isNoBroken = ConfigUtils.getBooleanFromConfig(var1, "isNoBroken", false);
      this.isNoInfected = ConfigUtils.getBooleanFromConfig(var1, "isNoInfected", false);
      this.isNoWet = ConfigUtils.getBooleanFromConfig(var1, "isNoWet", false);
      this.isNoHoled = ConfigUtils.getBooleanFromConfig(var1, "isNoHoled", false);      
      this.isNoDirted = ConfigUtils.getBooleanFromConfig(var1, "isNoDirted", false);
      this.isNoBlooded = ConfigUtils.getBooleanFromConfig(var1, "isNoBlooded", false);
      this.isUnlimitedCarry = ConfigUtils.getBooleanFromConfig(var1, "isUnlimitedCarry", false);
      this.isUnlimitedCondition = ConfigUtils.getBooleanFromConfig(var1, "isUnlimitedCondition", false);
      this.isUnlimitedAmmo = ConfigUtils.getBooleanFromConfig(var1, "isUnlimitedAmmo", false);
      this.isMapDrawLocalPlayer = ConfigUtils.getBooleanFromConfig(var1, "isMapDrawLocalPlayer", true);
      this.isMapDrawAllPlayers = ConfigUtils.getBooleanFromConfig(var1, "isMapDrawAllPlayers", false);
      this.isMapDrawVehicles = ConfigUtils.getBooleanFromConfig(var1, "isMapDrawVehicles", false);
      this.isMapDrawZombies = ConfigUtils.getBooleanFromConfig(var1, "isMapDrawZombies", false);
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
   
   private void updateLocalPlayerFeatures()
   {
      IsoPlayer var1 = IsoPlayer.getInstance();
      ArrayList var2 = var1.getInventory().getItems();
      Iterator var3 = var3.iterator();
      InventoryItem var4 = (InventoryItem)var3.next();
      HandWeapon var5 = (HandWeapon)var4;
      String var6 = var5.getFullType();
      
      if (var1 == null)
         return;

         if ((Boolean)SandboxOptions.instance.getOptionByName("MultiHitZombies").asConfigOption().getValueAsObject() != this.isMultiHitZombies)
            SandboxOptions.instance.set("MultiHitZombies", this.isMultiHitZombies);


         if (this.isEnableNightVision)
            var1.setWearingNightVisionGoggles(this.isEnableNightVision);


         if(var4 != null)
         {
            if (var4.getStringItemType().equals("RangedWeapon") && var4 instanceof HandWeapon)
            {
               if(this.isAlwaysKnockdown)
                var5.setAlwaysKnockdown(true);
            
               if(this.isAlwaysCritical)
                var5.setCriticalChance(100.0f);
            
               if(this.isAlwaysRack)
                var5.setRackAfterShoot(true);
            
               if(this.isNoJam)
                var5.setJammed(false);
            
               if(this.isAlwaysRoundChamber)
                var5.setRoundChambered(true);
            
               if(this.isNoSpentRoundChamber)
                var5.setSpentRoundChambered(false);
            
               if(this.isAlwaysAiming)
                var5.setAimingTime(0);
            
               if(this.isNoRecoil)
                var5.setRecoilDelay(0);
            
               if(this.isNoReload)
                var5.setReloadTime(0);

               if (this.isUnlimitedAmmo)
                var4.setCurrentAmmoCount(var2.getMaxAmmo());
            }

            if (var4.getVisual() != null)
            { 
               if(this.isNoHoled)
               for (int var7 = 0; var7 < BloodBodyPartType.MAX.index(); ++var7)
               var4.getVisual().removeHole(var7);

               if(this.isNoDirted)
               var4.getVisual().removeDirt(); 
               
               if(this.isNoBlooded)
               var4.getVisual().removeBlood();   
            }
 
            if(this.isUnlimitedCondition)
            var4.setCondition(var2.getConditionMax());

            if (this.isNoBroken) 
            var4.setBroken(false);
            
            if(this.isAlwaysRepaired)
            var4.setHaveBeenRepaired(1);

            if(this.isNoWet)
            var4.setWet(false);

            if(this.isNoInfected)
            var4.setInfected(false);
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
