// 
// Decompiled by Procyon v0.5.36
// 

package Lavish;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiChat;
import java.util.Iterator;
import Lavish.modules.Module;
import Lavish.event.Event;
import net.minecraft.client.Minecraft;
import Lavish.ui.NotWhitelisted;
import org.lwjgl.opengl.Display;
import Lavish.utils.fonts.FontUtil;
import Lavish.config.impl.AccountsFile;
import Lavish.config.impl.ModulesFile;
import Lavish.config.IFile;
import Lavish.cmd.CommandManager;
import Lavish.ClickGUI.RiseClickGUI.ClickGui;
import Lavish.utils.misc.GlyphPageFontRenderer;
import Lavish.utils.misc.Timer;
import Lavish.config.ConfigManager;
import Lavish.config.FileFactory;
import Lavish.ClickGUI.settings.SettingsManager;
import Lavish.modules.ModuleManager;

public class Client
{
    public static final Client instance;
    public ModuleManager moduleManager;
    public SettingsManager setmgr;
    public FileFactory fileFactory;
    public ConfigManager configManager;
    public String name;
    public String HUDName;
    public String uid;
    public boolean inGui;
    public long startTime;
    public int kills;
    Timer timer;
    public int version;
    public GlyphPageFontRenderer font12;
    public GlyphPageFontRenderer font14;
    public GlyphPageFontRenderer font16;
    public GlyphPageFontRenderer font18;
    public GlyphPageFontRenderer font20;
    public GlyphPageFontRenderer font22;
    public GlyphPageFontRenderer font24;
    public GlyphPageFontRenderer font26;
    public GlyphPageFontRenderer font28;
    public GlyphPageFontRenderer font30;
    public GlyphPageFontRenderer font32;
    public GlyphPageFontRenderer font34;
    public GlyphPageFontRenderer font36;
    public GlyphPageFontRenderer font38;
    public GlyphPageFontRenderer font40;
    public GlyphPageFontRenderer font42;
    public GlyphPageFontRenderer font44;
    public GlyphPageFontRenderer font46;
    public GlyphPageFontRenderer font48;
    public GlyphPageFontRenderer font50;
    public GlyphPageFontRenderer font52;
    public GlyphPageFontRenderer font54;
    public GlyphPageFontRenderer font56;
    public ClickGui RiseGUI;
    public Lavish.ClickGUI.MoonClickGUI.ClickGui MoonGUI;
    
    static {
        instance = new Client();
    }
    
    public Client() {
        this.name = "Lavish";
        this.timer = new Timer();
        this.version = 1;
    }
    
    public void startup() {
        this.setmgr = new SettingsManager();
        this.moduleManager = new ModuleManager();
        this.fileFactory = new FileFactory();
        (this.fileFactory = new FileFactory()).setupRoot();
        this.configManager = new ConfigManager();
        CommandManager.loadCommands();
        this.fileFactory.add(new ModulesFile(), new AccountsFile());
        this.fileFactory.load();
        System.out.println("Starting " + this.name + " | B" + this.version);
        this.RiseGUI = new ClickGui();
        this.MoonGUI = new Lavish.ClickGUI.MoonClickGUI.ClickGui();
        FontUtil.bootstrap();
        this.font12 = GlyphPageFontRenderer.create("Consolas", 12, false, false, false);
        this.font14 = GlyphPageFontRenderer.create("Consolas", 14, false, false, false);
        this.font16 = GlyphPageFontRenderer.create("Consolas", 16, false, false, false);
        this.font18 = GlyphPageFontRenderer.create("Consolas", 18, false, false, false);
        this.font20 = GlyphPageFontRenderer.create("Consolas", 20, false, false, false);
        this.font22 = GlyphPageFontRenderer.create("Consolas", 22, false, false, false);
        this.font24 = GlyphPageFontRenderer.create("Consolas", 24, false, false, false);
        this.font26 = GlyphPageFontRenderer.create("Consolas", 26, false, false, false);
        this.font28 = GlyphPageFontRenderer.create("Consolas", 28, false, false, false);
        this.font30 = GlyphPageFontRenderer.create("Consolas", 30, false, false, false);
        this.font32 = GlyphPageFontRenderer.create("Consolas", 32, false, false, false);
        this.font34 = GlyphPageFontRenderer.create("Consolas", 34, false, false, false);
        this.font36 = GlyphPageFontRenderer.create("Consolas", 36, false, false, false);
        this.font38 = GlyphPageFontRenderer.create("Consolas", 38, false, false, false);
        this.font40 = GlyphPageFontRenderer.create("Consolas", 40, false, false, false);
        this.font42 = GlyphPageFontRenderer.create("Consolas", 42, false, false, false);
        this.font44 = GlyphPageFontRenderer.create("Consolas", 44, false, false, false);
        this.font46 = GlyphPageFontRenderer.create("Consolas", 46, false, false, false);
        this.font48 = GlyphPageFontRenderer.create("Consolas", 48, false, false, false);
        this.font50 = GlyphPageFontRenderer.create("Consolas", 50, false, false, false);
        this.font52 = GlyphPageFontRenderer.create("Consolas", 52, false, false, false);
        this.font54 = GlyphPageFontRenderer.create("Consolas", 54, false, false, false);
        this.font56 = GlyphPageFontRenderer.create("Consolas", 56, false, false, false);
        Display.setTitle(this.name);
        if (NotWhitelisted.AuthURL != "https://pastebin.com/raw/2xpDhMHA") {
            Minecraft.getMinecraft().shutdownMinecraftApplet();
        }
    }
    
    public void shutdown() {
        this.fileFactory.save();
    }
    
    public void onEvent(final Event e) {
        for (final Module m : Client.instance.moduleManager.modules) {
            if (!m.isEnabled()) {
                continue;
            }
            m.onEvent(e);
        }
    }
    
    public void onKeyPress(final int key) {
        for (final Module m : Client.instance.moduleManager.getModules()) {
            if (m.getKey() == key) {
                m.toggle();
            }
        }
        if (key == 52) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiChat("."));
        }
    }
    
    public void onGameLoopTick() {
        if (!NotWhitelisted.isVerified && !(Minecraft.getMinecraft().currentScreen instanceof NotWhitelisted)) {
            Minecraft.getMinecraft().displayGuiScreen(new NotWhitelisted());
        }
    }
}
