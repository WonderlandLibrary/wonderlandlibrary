package summer;

import com.thealtening.auth.service.ServiceSwitcher;

import summer.base.manager.CheatManager;
import summer.cheat.guiutil.SM;
import summer.cheat.eventsystem.EventManager;
import summer.base.file.FileFactory;
import summer.base.file.impl.AccountsFile;
import summer.base.file.impl.ModulesFile;
import summer.base.font.FontManager;
import summer.base.manager.CommandManager;
import summer.base.manager.FriendManager;
import summer.base.manager.config.ConfigManager;
import summer.ui.altmanager.AltManager;
import summer.ui.clickui.Interface;
import summer.base.utilities.Manager;

public class Summer {

    public static Summer INSTANCE = new Summer();
    public static String NAME = "Summer Reborn";
    public static String VERSION = "3";
    public String APIKey = "";
    public ServiceSwitcher serviceSwitcher;
    public CheatManager cheatManager;
    public EventManager eventManager;
    public FriendManager friendManager;
    public SM settingsManager;
    public ConfigManager configManager;
    public FileFactory fileFactory;
    public Manager manager;
    public CommandManager commands;
    public AltManager altManager;
    public FontManager fontManager;
    public Interface clickGui1;
    public String theAltening = "";

    public void onStart() {

        this.commands = new CommandManager();
        this.eventManager = new EventManager();
        this.manager = new Manager();
        this.eventManager.register(this.manager);
        this.settingsManager = new SM();
        this.cheatManager = new CheatManager();
        this.friendManager = new FriendManager();
        this.fileFactory = new FileFactory();
        this.fileFactory.setupRoot(NAME);
        this.fileFactory.add(
                new ModulesFile(),
                new AccountsFile()
        );
        configManager = new ConfigManager();
        this.eventManager.register(this.cheatManager);
        this.clickGui1 = new Interface();
        this.clickGui1.setup();
        serviceSwitcher = new ServiceSwitcher();
        fontManager = new FontManager();
        fontManager.setup();
        altManager = new AltManager();
        altManager.init();
        this.fileFactory.load();
    }

    public FontManager getFontManager() {
        return fontManager;
    }

    public Interface getClickGui() {
        return clickGui1;
    }

    public void onStop() {
        fileFactory.save();
    }
}
