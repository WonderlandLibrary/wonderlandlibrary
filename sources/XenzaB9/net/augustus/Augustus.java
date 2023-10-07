// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus;

import viamcp.ViaMCP;
import net.lenni0451.eventapi.manager.EventManager;
import net.augustus.ui.augustusmanager.AugustusSounds;
import net.augustus.clickgui.buttons.CategoryButton;
import net.augustus.font.testfontbase.FontUtil;
import java.io.IOException;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.ArrayList;
import net.augustus.utils.ColorUtil;
import net.augustus.notify.rise5.NotificationManager;
import java.net.Proxy;
import net.augustus.utils.BlockUtil;
import net.augustus.utils.YawPitchHelper;
import net.augustus.clickgui.SettingSorter;
import net.augustus.utils.shader.BackgroundShaderUtil;
import me.jDev.xenza.files.Converter;
import net.augustus.clickgui.ClickGui;
import net.augustus.commands.CommandManager;
import net.augustus.settings.SettingsManager;
import net.augustus.planeGui.Clickgui;
import net.augustus.modules.ModuleManager;
import net.augustus.modules.Manager;
import java.util.List;
import net.augustus.csgoGui.CSGOGui;
import java.awt.Color;

public class Augustus
{
    private static final Augustus instance;
    private final String name = "xenza";
    private final String version = "recode b9";
    private final String dev = "jDev + Cookie";
    private final Color clientColor;
    public CSGOGui csgogui;
    private List<String> lastAlts;
    private final Manager manager;
    private ModuleManager moduleManager;
    private Clickgui planeGui;
    private SettingsManager settingsManager;
    private CommandManager commandManager;
    private ClickGui clickGui;
    private Converter converter;
    private BackgroundShaderUtil backgroundShaderUtil;
    private float shaderSpeed;
    private SettingSorter settingSorter;
    private YawPitchHelper yawPitchHelper;
    public String uid;
    private BlockUtil blockUtil;
    private Proxy proxy;
    private NotificationManager rise5notifyManager;
    private ColorUtil colorUtil;
    
    public Augustus() {
        this.clientColor = new Color(41, 146, 222);
        this.lastAlts = new ArrayList<String>();
        this.manager = new Manager();
        this.shaderSpeed = 1800.0f;
        this.uid = "";
    }
    
    public static Augustus getInstance() {
        return Augustus.instance;
    }
    
    public void preStart() {
        if (!Files.exists(Paths.get("xenzarecode/configs", new String[0]), new LinkOption[0])) {
            try {
                Files.createDirectories(Paths.get("xenzarecode/configs", new String[0]), (FileAttribute<?>[])new FileAttribute[0]);
            }
            catch (final IOException var2) {
                var2.printStackTrace();
            }
        }
    }
    
    public void start() {
        System.out.println("Starting Client...");
        FontUtil.bootstrap();
        this.colorUtil = new ColorUtil();
        this.yawPitchHelper = new YawPitchHelper();
        this.settingsManager = new SettingsManager();
        this.moduleManager = new ModuleManager();
        this.commandManager = new CommandManager();
        this.rise5notifyManager = new NotificationManager();
        this.clickGui = new ClickGui("ClickGui");
        this.planeGui = new Clickgui();
        this.csgogui = new CSGOGui();
        (this.converter = new Converter()).settingReader(this.settingsManager.getStgs());
        this.converter.settingSaver(this.settingsManager.getStgs());
        this.converter.moduleReader(this.moduleManager.getModules());
        this.converter.readLastAlts();
        this.converter.clickGuiLoader(this.clickGui.getCategoryButtons());
        this.backgroundShaderUtil = new BackgroundShaderUtil();
        this.settingSorter = new SettingSorter();
        AugustusSounds.currentSound = this.converter.readSound();
        this.blockUtil = new BlockUtil();
        EventManager.register((Object)this.settingSorter);
        EventManager.register((Object)this);
        try {
            ViaMCP.getInstance().start();
        }
        catch (final Exception var2) {
            var2.printStackTrace();
        }
    }
    
    public String getName() {
        return "xenza";
    }
    
    public String getVersion() {
        return "recode b9";
    }
    
    public String getDev() {
        return "jDev + Cookie";
    }
    
    public Color getClientColor() {
        return this.clientColor;
    }
    
    public ModuleManager getModuleManager() {
        return this.moduleManager;
    }
    
    public SettingsManager getSettingsManager() {
        return this.settingsManager;
    }
    
    public CommandManager getCommandManager() {
        return this.commandManager;
    }
    
    public ClickGui getClickGui() {
        return this.clickGui;
    }
    
    public NotificationManager getRise5notifyManager() {
        return this.rise5notifyManager;
    }
    
    public Clickgui getPlaneGui() {
        return this.planeGui;
    }
    
    public Converter getConverter() {
        return this.converter;
    }
    
    public List<String> getLastAlts() {
        return this.lastAlts;
    }
    
    public void setLastAlts(final List<String> lastAlts) {
        this.lastAlts = lastAlts;
    }
    
    public BackgroundShaderUtil getBackgroundShaderUtil() {
        return this.backgroundShaderUtil;
    }
    
    public float getShaderSpeed() {
        return this.shaderSpeed;
    }
    
    public void setShaderSpeed(final float shaderSpeed) {
        this.shaderSpeed = shaderSpeed;
    }
    
    public YawPitchHelper getYawPitchHelper() {
        return this.yawPitchHelper;
    }
    
    public BlockUtil getBlockUtil() {
        return this.blockUtil;
    }
    
    public Proxy getProxy() {
        return this.proxy;
    }
    
    public void setProxy(final Proxy proxy) {
        this.proxy = proxy;
    }
    
    public Manager getManager() {
        return this.manager;
    }
    
    public ColorUtil getColorUtil() {
        return this.colorUtil;
    }
    
    static {
        instance = new Augustus();
    }
}
