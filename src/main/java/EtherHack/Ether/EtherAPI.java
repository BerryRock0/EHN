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
   private final EtherLuaMethods etherLuaMethods = new EtherLuaMethods();
   public HashMap<String, Texture> textureCache = new HashMap<>();
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

   private void initStartupConfig()
   {
      Properties cfg = new Properties();
      
      //this. = ConfigUtils.getBooleanFromConfig(var1, "", false);
      this.mainUIAccentColor = ConfigUtils.getColorFromConfig(cfg, "mainUIAccentColor", new Color(56, 239, 125));
      this.vehiclesUIColor = ConfigUtils.getColorFromConfig(cfg, "vehiclesUIColor", new Color(150, 150, 200));
      this.zombiesUIColor = ConfigUtils.getColorFromConfig(cfg, "zombiesUIColor", new Color(255, 150, 100));
      this.playersUIColor = ConfigUtils.getColorFromConfig(cfg, "playersUIColor", new Color(255, 50, 100));
      this.isBypassDebugMode = ConfigUtils.getBooleanFromConfig(cfg, "isBypassDebugMode", false);
      this.isAlwaysRack = ConfigUtils.getBooleanFromConfig(cfg, "isAlwaysRack", false);
      this.isAlwaysRoundChamber = ConfigUtils.getBooleanFromConfig(cfg, "isAlwaysRoundChamber", false);
      this.isAlwaysRepaired = ConfigUtils.getBooleanFromConfig(cfg, "isAlwaysRepaired", false);
      this.isAlwaysKnockdown = ConfigUtils.getBooleanFromConfig(cfg, "isAlwaysKnockdown", false);
      this.isAlwaysAiming = ConfigUtils.getBooleanFromConfig(cfg, "isAlwaysAiming", false);
      this.isAlwaysCritical = ConfigUtils.getBooleanFromConfig(cfg, "isAlwaysCritical", false);
      this.isPlayerInSafeTeleported = ConfigUtils.getBooleanFromConfig(cfg, "isPlayerInSafeTeleported", false);
      this.isMultiHitZombies = ConfigUtils.getBooleanFromConfig(cfg, "isMultiHitZombies", false);
      this.isEnableNightVision = ConfigUtils.getBooleanFromConfig(cfg, "isEnableNightVision", false);
      this.isZombieDontAttack = ConfigUtils.getBooleanFromConfig(cfg, "isZombieDontAttack", false);
      this.isNoRecoil = ConfigUtils.getBooleanFromConfig(cfg, "isNoRecoil", false);
      this.isNoReload = ConfigUtils.getBooleanFromConfig(cfg, "isNoReload", false);
      this.isNoJam = ConfigUtils.getBooleanFromConfig(cfg, "isNoJam", false);
      this.isNoSpentRoundChamber = ConfigUtils.getBooleanFromConfig(cfg, "isNoSpentRoundChamber", false);
      this.isNoBroken = ConfigUtils.getBooleanFromConfig(cfg, "isNoBroken", false);
      this.isNoInfected = ConfigUtils.getBooleanFromConfig(cfg, "isNoInfected", false);
      this.isNoWet = ConfigUtils.getBooleanFromConfig(cfg, "isNoWet", false);
      this.isNoHoled = ConfigUtils.getBooleanFromConfig(cfg, "isNoHoled", false);      
      this.isNoDirted = ConfigUtils.getBooleanFromConfig(cfg, "isNoDirted", false);
      this.isNoBlooded = ConfigUtils.getBooleanFromConfig(cfg, "isNoBlooded", false);
      this.isUnlimitedCarry = ConfigUtils.getBooleanFromConfig(cfg, "isUnlimitedCarry", false);
      this.isUnlimitedCondition = ConfigUtils.getBooleanFromConfig(cfg, "isUnlimitedCondition", false);
      this.isUnlimitedAmmo = ConfigUtils.getBooleanFromConfig(cfg, "isUnlimitedAmmo", false);
      this.isMapDrawLocalPlayer = ConfigUtils.getBooleanFromConfig(cfg, "isMapDrawLocalPlayer", true);
      this.isMapDrawAllPlayers = ConfigUtils.getBooleanFromConfig(cfg, "isMapDrawAllPlayers", false);
      this.isMapDrawVehicles = ConfigUtils.getBooleanFromConfig(cfg, "isMapDrawVehicles", false);
      this.isMapDrawZombies = ConfigUtils.getBooleanFromConfig(cfg, "isMapDrawZombies", false);
   }

   public EtherAPI()
   {
      this.initStartupConfig();
      EventSubscriber.register(this);
   }

   @LuaEvents({@SubscribeLuaEvent(eventName = "OnResetLua"), @SubscribeLuaEvent(eventName = "OnMainMenuEnter")})
   public void loadAPI()
   {
      Logger.printLog("Loading EtherAPI...");

      if (this.exposer != null)
         this.exposer.destroy();

      this.exposer = new Exposer(LuaManager.converterManager, LuaManager.platform, LuaManager.env);
      this.exposer.exposeAPI(this.etherLuaMethods);
   }
   
   private void updateLocalPlayerFeatures()
   {
      ArrayList<InventoryItem> inventoryItems;
      IsoPlayer localPlayer = IsoPlayer.getInstance();
      InventoryItem playerItem = localPlayer.getPrimaryHandItem();
      HandWeapon weapon = (HandWeapon)playerItem;
      String weaponType = weapon.getFullType();
      
      if (localPlayer == null)
         return;

      if ((Boolean)SandboxOptions.instance.getOptionByName("MultiHitZombies").asConfigOption().getValueAsObject() != this.isMultiHitZombies)
         SandboxOptions.instance.set("MultiHitZombies", this.isMultiHitZombies);


      if (this.isEnableNightVision)
         localPlayer.setWearingNightVisionGoggles(this.isEnableNightVision);

      if(playerItem != null)
      {
         if (playerItem.getStringItemType().equals("RangedWeapon") && playerItem instanceof HandWeapon)
         {
            if(this.isAlwaysKnockdown) weapon.setAlwaysKnockdown(true);
            if(this.isAlwaysCritical) weapon.setCriticalChance(100.0f);
            if(this.isAlwaysRack) weapon.setRackAfterShoot(true);
            if(this.isNoJam) weapon.setJammed(false);
            if(this.isAlwaysRoundChamber) weapon.setRoundChambered(true);
            if(this.isNoSpentRoundChamber) weapon.setSpentRoundChambered(false);
            if(this.isAlwaysAiming) weapon.setAimingTime(0);
            if(this.isNoRecoil) weapon.setRecoilDelay(0);
            if(this.isNoReload) weapon.setReloadTime(0);
            if (this.isUnlimitedAmmo) playerItem.setCurrentAmmoCount(playerItem.getMaxAmmo());
         }

         if(this.isAlwaysRepaired) playerItem.setHaveBeenRepaired(1);
         if(this.isUnlimitedCondition) playerItem.setCondition(playerItem.getConditionMax());
      }

      if ((inventoryItems = localPlayer.getInventory().getItems()) != null && !inventoryItems.isEmpty())
      {
         for (InventoryItem item : inventoryItems)
         {
            if (item == null)
               continue;

            if (playerItem.getVisual() != null)
            { 
               if(this.isNoHoled)
                  for (int i = 0; i < BloodBodyPartType.MAX.index(); ++i)
                     item.getVisual().removeHole(i);

               if(this.isNoDirted) item.getVisual().removeDirt(); 
               if(this.isNoBlooded) item.getVisual().removeBlood();
            }
               
            if(this.isAlwaysRepaired) item.setHaveBeenRepaired(1);
            if (this.isNoBroken) item.setBroken(false);
            if(this.isUnlimitedCondition) item.setCondition(playerItem.getConditionMax());
            if(this.isNoWet) item.setWet(false);
            if(this.isNoInfected) item.setInfected(false); 
         }
      }
   }
   
   private void bypassDebugMode()
   {
      Core.debug = this.isBypassDebugMode && GameClient.bIngame;
   }

   @SubscribeLuaEvent(eventName = "OnRenderTick")
   public synchronized void updateAPI()
   {
      try
      {
         bypassDebugMode();
         updateLocalPlayerFeatures();
      }
      catch (Exception e)
      {}
   }
}
