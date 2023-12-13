package cn.hanabi;

import cn.hanabi.altmanager.AltFileManager;
import cn.hanabi.command.CommandManager;
import cn.hanabi.events.EventLoop;
import cn.hanabi.events.EventPacket;
import cn.hanabi.gui.cloudmusic.MusicManager;
import cn.hanabi.gui.cloudmusic.ui.MusicPlayerUI;
import cn.hanabi.gui.font.noway.ttfr.FontLoaders;
import cn.hanabi.modules.ModManager;
import cn.hanabi.utils.ClientUtil;
import cn.hanabi.utils.CrashUtils;
import cn.hanabi.utils.NukerUtil;
import cn.hanabi.utils.TimeHelper;
import cn.hanabi.utils.bypass.AESUtil;
import cn.hanabi.utils.fileSystem.FileManager;
import cn.hanabi.utils.waypoints.WaypointManager;
import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import java.awt.AWTException;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.imageio.ImageIO;
import me.yarukon.DiscordThread;
import me.yarukon.Yarukon;
import me.yarukon.hud.window.HudWindowManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S07PacketRespawn;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.Display;

public class Hanabi {
   public static final @NotNull String CLIENT_NAME = "Hanabi";
   public static final double CLIENT_VERSION_NUMBER = 3.1;
   public static final @NotNull String CLIENT_VERSION = "3.1";
   public static final @NotNull String CLIENT_INITIALS;
   public static Hanabi INSTANCE;
   public boolean disble;
   public final boolean windows = System.getProperties().getProperty("os.name").toLowerCase().contains("windows");
   public ArrayList debugUtils = new ArrayList();
   public AESUtil aesUtil = new AESUtil(1);
   public ModManager moduleManager;
   public CommandManager commandManager;
   public FileManager fileManager;
   public FontLoaders fontManager;
   public AltFileManager altFileMgr;
   public TrayIcon trayIcon;
   public WaypointManager waypointManager;
   public MusicPlayerUI mpui;
   public String location;
   public boolean hypixelBypass = false;
   public boolean mslogin = false;
   public HudWindowManager hudWindowMgr;
   public boolean customScoreboard = false;
   public boolean hasOptifine = false;
   public Field ofFastRenderField;
   public Queue packetQueue;
   TimeHelper ms = new TimeHelper();
   public long timing;

   public Hanabi() {
      INSTANCE = this;
      EventManager.register(this);
   }

   public void log(String message) {
      String prefix = "[Hanabi] ";
      INSTANCE.println(prefix + message);
   }

   public void startClient() {
      Display.setTitle("Hanabi 3.1");
      this.location = Locale.getDefault().getCountry();
      this.fileManager = new FileManager();
      this.commandManager = new CommandManager();
      this.moduleManager = new ModManager();
      new Yarukon();
      if (Client.username == null || Client.rank == null) {
         CrashUtils.doCrash();
      }

      this.fontManager = new FontLoaders();
      EventManager.register(new NukerUtil());
      (this.altFileMgr = new AltFileManager()).loadFiles();
      ClientUtil.notifications.clear();
      this.moduleManager.addModules();
      this.hudWindowMgr = new HudWindowManager();
      this.commandManager.addCommands();
      this.waypointManager = new WaypointManager();
      new MusicManager();
      this.mpui = new MusicPlayerUI();
      this.fileManager.load();
      if (this.windows && SystemTray.isSupported()) {
         try {
            this.trayIcon = new TrayIcon(ImageIO.read((InputStream)Objects.requireNonNull(this.getClass().getResourceAsStream("/assets/minecraft/Client/icon128.png"))));
         } catch (Exception var4) {
            var4.printStackTrace();
         }

         this.trayIcon.setImageAutoSize(true);
         this.trayIcon.setToolTip("Hanabi Client  ~ " + Client.username);

         try {
            SystemTray.getSystemTray().add(this.trayIcon);
         } catch (AWTException var3) {
            this.log("Unable to add tray icon.");
         }

         this.trayIcon.displayMessage("HanabiClient", "Thank you for using Hanabi", MessageType.NONE);
         Wrapper.notificationsAllowed(true);
      }

      try {
         this.ofFastRenderField = GameSettings.class.getDeclaredField("ofFastRender");
         this.hasOptifine = true;
      } catch (Exception var2) {
      }

      this.packetQueue = new ConcurrentLinkedQueue();
      this.ms.reset();
      this.timing = 100L;
      (new DiscordThread()).start();
   }

   public boolean fastRenderDisabled(GameSettings gameSettingsIn) {
      try {
         return !(Boolean)this.ofFastRenderField.get(gameSettingsIn);
      } catch (Exception var3) {
         return true;
      }
   }

   public void stopClient() {
      try {
         if (this.windows && SystemTray.isSupported()) {
            INSTANCE.trayIcon.displayMessage("HanabiClient - Notification", "See you soon.", MessageType.ERROR);
         }

         this.fileManager.save();
      } catch (Exception var2) {
         System.err.println("Failed to save settings:");
         var2.printStackTrace();
      }

   }

   @EventTarget
   public void onTick(EventLoop e) {
      if (!this.packetQueue.isEmpty()) {
         if (this.ms.isDelayComplete(this.timing)) {
            Wrapper.sendPacketNoEvent((Packet)this.packetQueue.poll());
            this.ms.reset();
         }

      }
   }

   @EventTarget
   public void onWorldChange(EventPacket e) {
      if (e.getPacket() instanceof S07PacketRespawn || e.getPacket() instanceof S01PacketJoinGame) {
         this.packetQueue.clear();
         this.ms.reset();
      }

   }

   public void println(String obj) {
      Class systemClass = null;

      try {
         systemClass = Class.forName("java.lang.System");
         Field outField = null;

         try {
            outField = systemClass.getDeclaredField("out");
         } catch (NoSuchFieldException var7) {
            var7.printStackTrace();
         }

         Class printStreamClass = ((Field)Objects.requireNonNull(outField)).getType();
         Method printlnMethod = printStreamClass.getDeclaredMethod("println", String.class);
         Object object = outField.get((Object)null);
         printlnMethod.invoke(object, obj);
      } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | ClassNotFoundException var8) {
         var8.printStackTrace();
      }

   }

   static {
      List chars = new ArrayList();
      char[] c = "Hanabi".toCharArray();
      int i = c.length;

      for(int var3 = 0; var3 < i; ++var3) {
         char c = c[var3];
         if (Character.toUpperCase(c) == c) {
            chars.add(c);
         }
      }

      c = new char[chars.size()];

      for(i = 0; i < chars.size(); ++i) {
         c[i] = (Character)chars.get(i);
      }

      CLIENT_INITIALS = new String(c);
   }
}
