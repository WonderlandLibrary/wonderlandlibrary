package client;

import client.bot.BotManager;
import client.command.CommandManager;
import client.event.EventBus;
import client.module.ModuleManager;
import client.util.MamaUtil;
import client.util.file.FileManager;
import client.util.file.config.ConfigFile;
import client.util.file.config.ConfigManager;
import client.util.liquidbounce.InventoryUtils;
import lombok.Getter;
import org.lwjgl.opengl.Display;
import viamcp.ViaMCP;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@Getter
public enum Client {

    INSTANCE;

    public static final String NAME = "Client", VERSION = "1.0";

    public static final boolean DEVELOPMENT_SWITCH = true;

    private EventBus eventBus;
    private ModuleManager moduleManager;
    private CommandManager commandManager;
    private BotManager botManager;
    private FileManager fileManager;
    private ConfigManager configManager;
    private ConfigFile configFile;

    public void init() {
        /*
        new Thread(() -> {
            try {
                if (!MamaUtil.isWhitelisted()) Runtime.getRuntime().exit(0);
            } catch (IOException | NoSuchAlgorithmException e) {
                Runtime.getRuntime().exit(0);
            }
        }).start();
        
         */
        eventBus = new EventBus();
        moduleManager = new ModuleManager();
        commandManager = new CommandManager();
        botManager = new BotManager();
        fileManager = new FileManager();
        configManager = new ConfigManager();
        configFile = new ConfigFile(new File(ConfigManager.CONFIG_DIRECTORY, "latest.json"));
        moduleManager.init();
        commandManager.init();
        botManager.init();
        fileManager.init();
        configManager.init();
        configFile.allowKeyCodeLoading();
        configFile.read();
        Display.setTitle(NAME + " " + VERSION);
        try
        {
            ViaMCP.getInstance().start();


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        configFile.write();
    }
}
