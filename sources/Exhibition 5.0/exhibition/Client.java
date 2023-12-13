// 
// Decompiled by Procyon v0.5.30
// 

package exhibition;

import net.minecraft.client.gui.GuiMainMenu;
import java.io.InputStream;
import java.awt.Font;
import exhibition.management.friend.FriendManager;
import exhibition.module.Module;
import exhibition.gui.screen.impl.mainmenu.ClientMainMenu;
import exhibition.util.render.TTFFontRenderer;
import net.minecraft.client.gui.GuiScreen;
import java.io.File;
import exhibition.management.command.CommandManager;
import exhibition.gui.click.ClickGui;
import exhibition.gui.altmanager.FileManager;
import exhibition.module.ModuleManager;
import exhibition.management.ColorManager;

public class Client
{
    public static Client instance;
    public static final String author = "Arithmo";
    public static final String version = "5.0";
    public static final String clientName = "Exhibition";
    public static ColorManager cm;
    private final ModuleManager moduleManager;
    private static FileManager fileManager;
    private static ClickGui clickGui;
    public static CommandManager commandManager;
    private File dataDirectory;
    private GuiScreen mainMenu;
    private boolean isHidden;
    public static TTFFontRenderer f;
    public static TTFFontRenderer fs;
    public static TTFFontRenderer fss;
    public static TTFFontRenderer fsmallbold;
    public static TTFFontRenderer header;
    public static TTFFontRenderer subHeader;
    public static TTFFontRenderer badCache;
    public static TTFFontRenderer bindText;
    
    public static FileManager getFileManager() {
        return Client.fileManager;
    }
    
    public Client() {
        this.mainMenu = new ClientMainMenu();
        Client.commandManager = new CommandManager();
        Client.instance = this;
        this.moduleManager = new ModuleManager(Module.class);
        FriendManager.start();
    }
    
    public static ClickGui getClickGui() {
        return Client.clickGui;
    }
    
    public void setup() {
        Client.commandManager.setup();
        this.dataDirectory = new File("ArthimoWare");
        this.moduleManager.setup();
        Module.loadSettings();
        (Client.fileManager = new FileManager()).loadFiles();
        Client.clickGui = new ClickGui();
        this.setupFonts();
    }
    
    public void setupFonts() {
        Client.f = new TTFFontRenderer(new Font("Impact", 0, 24), true);
        Client.fs = new TTFFontRenderer(new Font("Tahoma Bold", 0, 11), true);
        Client.fss = new TTFFontRenderer(new Font("Tahoma", 0, 10), false);
        Client.bindText = new TTFFontRenderer(new Font("Tahoma", 0, 8), false);
        Client.fsmallbold = new TTFFontRenderer(new Font("Tahoma Bold", 0, 10), true);
        Client.header = new TTFFontRenderer(new Font("Myriad Pro", 0, 24), true);
        Client.subHeader = new TTFFontRenderer(new Font("Myriad Pro", 0, 18), true);
        final InputStream istream = this.getClass().getResourceAsStream("/assets/minecraft/font.ttf");
        Font myFont = null;
        try {
            myFont = Font.createFont(0, istream);
            myFont = myFont.deriveFont(0, 36.0f);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Client.badCache = new TTFFontRenderer(myFont, true);
    }
    
    public static ModuleManager<Module> getModuleManager() {
        return (ModuleManager<Module>)Client.instance.moduleManager;
    }
    
    public static File getDataDir() {
        return Client.instance.dataDirectory;
    }
    
    public static boolean isHidden() {
        return Client.instance.isHidden;
    }
    
    public static void setHidden(final boolean hidden) {
        Client.instance.isHidden = hidden;
        if (hidden) {
            Client.instance.mainMenu = new GuiMainMenu();
        }
        else {
            Client.instance.mainMenu = new ClientMainMenu();
        }
    }
    
    public static void resetClickGui() {
        Client.clickGui = new ClickGui();
    }
    
    static {
        Client.cm = new ColorManager();
    }
}
