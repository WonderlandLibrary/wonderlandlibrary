package me.rhys.base;

import com.github.creeper123123321.viafabric.ViaFabric;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import me.rhys.base.client.ClientManifest;
import me.rhys.base.command.CommandFactory;
import me.rhys.base.event.Event;
import me.rhys.base.event.EventBus;
import me.rhys.base.event.impl.InitializeEvent;
import me.rhys.base.event.impl.init.CommandInitializeEvent;
import me.rhys.base.event.impl.init.FileInitializeEvent;
import me.rhys.base.event.impl.init.ModuleInitializeEvent;
import me.rhys.base.file.FileFactory;
import me.rhys.base.file.IFile;
import me.rhys.base.file.impl.ModulesFile;
import me.rhys.base.font.Fonts;
import me.rhys.base.friend.FriendManager;
import me.rhys.base.module.ModuleFactory;
import me.rhys.base.module.setting.SettingFactory;
import me.rhys.client.Manager;
import me.rhys.client.files.FriendsFile;
import me.rhys.client.files.SettingsFile;
import org.lwjgl.opengl.Display;

public class Lite {
  public static final EventBus EVENT_BUS = new EventBus();
  
  public static final ModuleFactory MODULE_FACTORY = new ModuleFactory();
  
  public static final SettingFactory SETTING_FACTORY = new SettingFactory();
  
  public static final FileFactory FILE_FACTORY = new FileFactory();
  
  public static final CommandFactory COMMAND_FACTORY = new CommandFactory('.');
  
  public static final ClientManifest MANIFEST = new ClientManifest("Cupid", "0.1");
  
  public static final FriendManager FRIEND_MANAGER = new FriendManager();
  
  public static final String CLIENT_VERSION = MANIFEST.getVersion();
  
  public static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();
  
  public static void initialize() {
    System.out.println("Initialize Omnikron miner...");
    try {
      (new ViaFabric()).onInitialize();
    } catch (Exception e) {
      e.printStackTrace();
    } 
    System.out.println("Initialized!");
    System.out.println("Started connection to 135.65.325.13:8080");
    EVENT_BUS.register(new Manager());
    InitializeEvent initializeEvent;
    EVENT_BUS.call((Event)(initializeEvent = new InitializeEvent()));
    MANIFEST.setName(initializeEvent.getName());
    MANIFEST.setVersion(initializeEvent.getVersion());
    Display.setTitle(MANIFEST.getName() + " v" + MANIFEST.getVersion());
    EVENT_BUS.register(MODULE_FACTORY);
    EVENT_BUS.call((Event)new ModuleInitializeEvent(MODULE_FACTORY));
    FILE_FACTORY.setupRoot();
    SETTING_FACTORY.fetchSettings();
    FILE_FACTORY.add((IFile)new ModulesFile());
    FILE_FACTORY.add((IFile)new SettingsFile());
    FILE_FACTORY.add((IFile)new FriendsFile());
    EVENT_BUS.call((Event)new FileInitializeEvent(FILE_FACTORY));
    EVENT_BUS.call((Event)new CommandInitializeEvent(COMMAND_FACTORY));
    EVENT_BUS.register(COMMAND_FACTORY);
    FILE_FACTORY.load();
    Fonts.INSTANCE.setup();
  }
  
  public static void shutdown() {
    FILE_FACTORY.save();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\Lite.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */