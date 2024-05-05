package space.lunaclient.luna;

import java.awt.TrayIcon.MessageType;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.arikia.dev.drpc.callbacks.ReadyCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.main.Main;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.Session;
import org.apache.commons.io.FileUtils;
import org.lwjgl.opengl.Display;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.impl.alert.DisplayTray;
import space.lunaclient.luna.impl.elements.combat.KillAura;
import space.lunaclient.luna.impl.elements.movement.speed.Speed;
import space.lunaclient.luna.impl.gui.alt.AltManager;
import space.lunaclient.luna.impl.irc.IrcManager;
import space.lunaclient.luna.impl.managers.CommandManager;
import space.lunaclient.luna.impl.managers.ConfigManager;
import space.lunaclient.luna.impl.managers.CustomFileManager;
import space.lunaclient.luna.impl.managers.ElementManager;
import space.lunaclient.luna.impl.managers.EventManager;
import space.lunaclient.luna.impl.managers.FriendManager;
import space.lunaclient.luna.impl.managers.SettingManager;
import space.lunaclient.luna.impl.managers.WaypointManager;
import space.lunaclient.luna.util.web.WebHelper;

public enum Luna
{
  INSTANCE;
  
  public String NAME = "Luna";
  public String BUILD = "13.0.7";
  public String[] FORMAT = { "v", "b", "" };
  public String CURRENT_FORMAT = this.FORMAT[2];
  public String TITLE = "| " + this.NAME + " " + this.CURRENT_FORMAT + this.BUILD + " |";
  public static String BACKGROUND = "luna/LunaBGC.jpg";
  public static String BACKGROUND_MAIN = "luna/bg.png";
  public static String LOGO_INGAME = "luna/logoi.png";
  public static String[] sponsorList = { "https://selly.gg/@Proxima_Alts" };
  public EventManager EVENT_MANAGER;
  public ElementManager ELEMENT_MANAGER;
  public IrcManager IRC_MANAGER;
  public SettingManager SETTING_MANAGER;
  public CommandManager COMMAND_MANAGER;
  public CustomFileManager FILE_MANAGER;
  public AltManager ALT_MANAGER;
  public FriendManager FRIEND_MANAGER;
  public ConfigManager CONFIG_MANAGER;
  public WaypointManager WAYPOINT_MANAGER;
  public boolean isLoading;
  public static int taskbarprogress;
  public static String name;
  public boolean lock;
  public static boolean l = false;
  
  private Luna() {}
  
  public void onLaunch()
  {
    this.isLoading = true;
    Main.startWhitelist();
    
    Display.setTitle(this.TITLE);
    
    this.EVENT_MANAGER = new EventManager();
    this.ELEMENT_MANAGER = new ElementManager();
    if (this.ELEMENT_MANAGER.getElement(Speed.class).isToggled()) {
      this.ELEMENT_MANAGER.getElement(Speed.class).toggle();
    }
    if (this.ELEMENT_MANAGER.getElement(KillAura.class).isToggled()) {
      this.ELEMENT_MANAGER.getElement(KillAura.class).toggle();
    }
    this.SETTING_MANAGER = new SettingManager();
    this.COMMAND_MANAGER = new CommandManager();
    this.FRIEND_MANAGER = new FriendManager();
    this.CONFIG_MANAGER = new ConfigManager();
    this.WAYPOINT_MANAGER = new WaypointManager();
    this.ALT_MANAGER = new AltManager();
    this.ALT_MANAGER.setupAlts();
    
    this.FILE_MANAGER = new CustomFileManager();
    this.FILE_MANAGER.loadFiles();
    
    this.IRC_MANAGER = new IrcManager(Minecraft.getMinecraft().getSession().getUsername());
    this.IRC_MANAGER.connect();
    try
    {
      try
      {
        File file = new File("C:\\Users\\" + System.getProperty("user.name") + "\\AppData\\Local\\hardware.txt");
        boolean var = file.createNewFile();
        if (var) {
          System.out.println("CPU IP Has been logged, incase if the user dosen't follow the TOS.");
        } else {
          System.out.println("CPU IP Has been logged, incase if the user dosen't follow the TOS.");
        }
      }
      catch (IOException e)
      {
        System.out.println("Exception Occurred:");
        e.printStackTrace();
      }
      if (WebHelper.getViaBuffered("https://lunaclient.app/wbpanel/hardwareBlocker.lun").contains(Main.getLicense()))
      {
        DisplayTray.displayTray("Luna", "Your machine has been blocked.", TrayIcon.MessageType.ERROR);
        System.out.println("Your Machine's Serial ID Number(s) have been blocked.");
        Main.psa(true);
        this.lock = true;
      }
      if (this.lock) {
        FileUtils.writeStringToFile(new File("C:\\Users\\" + System.getProperty("user.name") + "\\AppData\\Local\\hardware.txt"), Main.getLicenseSHA1());
      }
      if (FileUtils.readFileToString(new File("C:\\Users\\" + System.getProperty("user.name") + "\\AppData\\Local\\hardware.txt")).contains(Main.getLicenseSHA1()))
      {
        DisplayTray.displayTray("Luna", "CPU ID Blocked.", TrayIcon.MessageType.ERROR);
        Main.psa(true);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    loadPresence();
    
    this.isLoading = false;
  }
  
  public static void loadPresence()
  {
    initDiscord();
    
    Minecraft mc = Minecraft.getMinecraft();
    
    DiscordRPC.discordRunCallbacks();
    
    DiscordRichPresence rich = new DiscordRichPresence();
    if ((mc.getCurrentServerData() != null) && (mc.getCurrentServerData().serverIP != null) && (!(mc.currentScreen instanceof GuiMainMenu))) {
      rich.state = ("Last seen on: " + mc.getCurrentServerData().serverIP);
    }
    rich.details = ("Using Luna " + INSTANCE.CURRENT_FORMAT + "" + INSTANCE.BUILD + "");
    rich.largeImageKey = "logolarge";
    rich.largeImageText = ("Running Luna " + INSTANCE.CURRENT_FORMAT + "" + INSTANCE.BUILD + "");
    DiscordRPC.discordUpdatePresence(rich);
  }
  
  private static void initDiscord()
  {
    DiscordEventHandlers handlers = new DiscordEventHandlers();
    handlers.ready = new DiscordReadyEvent(null);
    DiscordRPC.discordInitialize("465929473028784149", handlers, true);
  }
  
  private static class DiscordReadyEvent
    implements ReadyCallback
  {
    private DiscordReadyEvent() {}
    
    public void apply()
    {
      Minecraft mc = Minecraft.getMinecraft();
      
      DiscordRichPresence rich = new DiscordRichPresence();
      if ((mc.getCurrentServerData() != null) && (mc.getCurrentServerData().serverIP != null) && (!(mc.currentScreen instanceof GuiMainMenu))) {
        rich.state = ("Last seen on: " + mc.getCurrentServerData().serverIP);
      }
      rich.details = ("Using Luna " + Luna.INSTANCE.CURRENT_FORMAT + "" + Luna.INSTANCE.BUILD + "");
      rich.largeImageKey = "logolarge";
      rich.largeImageText = ("Running Luna " + Luna.INSTANCE.CURRENT_FORMAT + "" + Luna.INSTANCE.BUILD + "");
      rich.startTimestamp = (System.currentTimeMillis() / 1000L);
      DiscordRPC.discordUpdatePresence(rich);
    }
  }
  
  public void onClose()
  {
    this.FILE_MANAGER.saveFiles();
  }
}
