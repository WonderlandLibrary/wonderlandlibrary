package org.alphacentauri;

import java.io.File;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import org.alphacentauri.core.CoreConfig;
import org.alphacentauri.launcher.api.API;
import org.alphacentauri.management.alts.Account;
import org.alphacentauri.management.alts.AltList;
import org.alphacentauri.management.managers.CommandManager;
import org.alphacentauri.management.managers.EventManager;
import org.alphacentauri.management.managers.FriendManager;
import org.alphacentauri.management.managers.KeyBindManager;
import org.alphacentauri.management.managers.ModuleManager;
import org.alphacentauri.management.managers.NotificationManager;
import org.alphacentauri.management.managers.PropertyManager;
import org.alphacentauri.management.managers.TaskManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.darkstorm.minecraft.gui.ACGuiManager;
import org.darkstorm.minecraft.gui.theme.simple.SimpleTheme;
import org.lwjgl.opengl.Display;

public class AC {
   private static AC instance;
   public final boolean debug = API.getUsername().equalsIgnoreCase("Castelbuilder") || API.getUsername().equalsIgnoreCase("SaubereSacheLP") || API.getUsername().equalsIgnoreCase("h0p3");
   public final String version = "b6";
   private Logger logger;
   private ModuleManager moduleManager;
   private CommandManager commandManager;
   private EventManager eventManager;
   private PropertyManager propertyManager;
   private KeyBindManager keyBindManager;
   private FriendManager friendManager;
   private NotificationManager notificationManager;
   private TaskManager taskManager;
   private CoreConfig config;
   private Random random;
   private AltList alts;
   private Thread mainThread;
   private Executor threadPool;
   public ACGuiManager guiManager;
   private boolean ghost = false;

   public AC() {
      instance = this;
      this.mainThread = Thread.currentThread();
      this.threadPool = Executors.newFixedThreadPool(10);
      this.random = new Random();
      this.config = new CoreConfig();
      this.alts = new AltList();
      this.logger = LogManager.getLogger("Alpha Centauri");

      try {
         String lastAccount = getConfig().getLastAccount();
         if(lastAccount != null) {
            String[] split = lastAccount.split(":");
            (new Thread(() -> {
               getMC().session = (new Account(split[0], split[1])).login();
            })).start();
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      this.taskManager = new TaskManager();
      this.notificationManager = new NotificationManager();
      this.friendManager = new FriendManager();
      this.propertyManager = new PropertyManager();
      this.keyBindManager = new KeyBindManager();
      this.moduleManager = new ModuleManager();
      this.commandManager = new CommandManager();
      this.eventManager = new EventManager();
      this.propertyManager.loadConfig("config");
      Runtime.getRuntime().addShutdownHook(new Thread(this::onShutdown));
   }

   public static AC getInstance() {
      return instance;
   }

   public static Logger getLogger() {
      return getInstance().logger;
   }

   public static String getVersion() {
      getInstance().getClass();
      return "b6";
   }

   public static CoreConfig getConfig() {
      return getInstance().config;
   }

   public static Minecraft getMC() {
      return Minecraft.getMinecraft();
   }

   public static KeyBindManager getKeyBindManager() {
      return getInstance().keyBindManager;
   }

   public static CommandManager getCommandManager() {
      return getInstance().commandManager;
   }

   public static boolean isGhost() {
      return getInstance().ghost;
   }

   public static void bootstrap() {
      new AC();
   }

   public static ModuleManager getModuleManager() {
      return getInstance().moduleManager;
   }

   public static NotificationManager getNotificationManager() {
      return getInstance().notificationManager;
   }

   public static PropertyManager getPropertyManager() {
      return getInstance().propertyManager;
   }

   public static EventManager getEventManager() {
      return getInstance().eventManager;
   }

   public static Executor getThreadPool() {
      return getInstance().threadPool;
   }

   public void onShutdown() {
      try {
         getMC().shutdown();
      } catch (Exception var5) {
         ;
      }

      try {
         Display.destroy();
      } catch (Exception var4) {
         ;
      }

      try {
         this.mainThread.interrupt();
      } catch (Exception var3) {
         ;
      }

      try {
         getGuiManager().save();
         getConfig().save();
         this.propertyManager.loadConfig("config");
         this.alts.save();
      } catch (Exception var2) {
         ;
      }

      System.out.println("Shutting down!");
   }

   public static File getDataDir() {
      return new File(getMC().mcDataDir.getAbsoluteFile() + "//AlphaCentauri//");
   }

   public static AltList getAlts() {
      return getInstance().alts;
   }

   public static TaskManager getTaskManager() {
      return getInstance().taskManager;
   }

   public static boolean isDebug() {
      return getInstance().debug;
   }

   public static void setGhost(boolean ghost) {
      if(ghost) {
         if(getMC().ingameGUI != null) {
            getMC().ingameGUI.getChatGUI().clearACMessages();
         }

         Display.setTitle("Minecraft 1.8.9");
      } else {
         Display.setTitle("Alpha Centauri " + getVersion());
      }

      getInstance().ghost = ghost;
   }

   public static FriendManager getFriendManager() {
      return getInstance().friendManager;
   }

   public static ACGuiManager getGuiManager() {
      if(getInstance().guiManager == null) {
         getInstance().guiManager = new ACGuiManager();
         getInstance().guiManager.setTheme(new SimpleTheme());
         getInstance().guiManager.setup();
         getInstance().guiManager.load();
      }

      return getInstance().guiManager;
   }

   public static void addChat(String name, Object message) {
      addChat(name, String.valueOf(message));
   }

   public static void addChat(String name, String message) {
      if(getMC().getPlayer() != null) {
         String prefix = "§8[§4AC§8]";
         if(API.getUsername().equalsIgnoreCase("haze")) {
            prefix = "§8[§4E§8]";
         } else if(API.getUsername().equalsIgnoreCase("hazes_nigger")) {
            prefix = "§8[§4AX§8]";
         }

         getMC().getPlayer().addChatMessage(new ChatComponentText(prefix + " [§c" + name + "§8]§e " + message));
      }
   }

   public static Random getRandom() {
      return getInstance().random;
   }
}
