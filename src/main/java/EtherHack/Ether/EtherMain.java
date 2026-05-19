package EtherHack.Ether;

import EtherHack.utils.Logger;

public class EtherMain {
   private static EtherMain instance;
   private static boolean initialized;
   public EtherTranslator etherTranslator;
   public EtherLuaManager etherLuaManager;
   public EtherAPI etherAPI;

   private EtherMain() {
   }

   public static synchronized void initSafe() {
      if (initialized) {
         Logger.printLog("EtherHack is already initialized. Skipping duplicate init.");
         return;
      }

      try {
         getInstance().init();
         initialized = true;
      } catch (Throwable throwable) {
         Logger.printLog("EtherHack initialization failed: " + throwable);
         throwable.printStackTrace();
      }
   }

   public void init() {
      Logger.printLog("Initializing EtherHack...");
      this.etherTranslator = new EtherTranslator();
      this.etherTranslator.loadTranslations();
      this.etherAPI = new EtherAPI();
      this.etherAPI.loadAPI();
      this.etherLuaManager = new EtherLuaManager();
      this.etherLuaManager.loadLua();
      Logger.printLog("Initialization EtherHack was completed!");
   }

   public static EtherMain getInstance() {
      if (instance == null) {
         instance = new EtherMain();
      }

      return instance;
   }
}
