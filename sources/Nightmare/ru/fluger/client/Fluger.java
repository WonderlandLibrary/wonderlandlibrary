// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client;

import java.util.Random;
import ru.fluger.client.event.EventTarget;
import java.util.Iterator;
import ru.fluger.client.feature.Feature;
import ru.fluger.client.event.events.impl.input.EventInputKey;
import ru.fluger.client.event.EventManager;
import ru.fluger.client.files.impl.MacroConfig;
import ru.fluger.client.files.impl.HudConfig;
import java.io.IOException;
import ru.fluger.client.files.impl.FriendConfig;
import ru.fluger.client.helpers.misc.TpsHelper;
import org.lwjgl.opengl.Display;
import ru.fluger.client.prot.Client;
import ru.fluger.client.helpers.hwid.HwidUtil;
import ru.fluger.client.helpers.math.RotationHelper;
import ru.fluger.client.ui.clickgui.ClickGuiScreen;
import ru.fluger.client.settings.config.ConfigManager;
import ru.fluger.client.cmd.CommandManager;
import ru.fluger.client.files.FileManager;
import ru.fluger.client.cmd.macro.MacroManager;
import ru.fluger.client.friend.FriendManager;
import ru.fluger.client.ui.components.draggable.DraggableHUD;
import ru.fluger.client.feature.FeatureManager;
import ru.fluger.client.helpers.request.RequstUtil;

public class Fluger
{
    public static Fluger instance;
    public Long time;
    public static String name;
    public static String type;
    public static String version;
    public static String status;
    public RequstUtil requst;
    public FeatureManager featureManager;
    public DraggableHUD draggableHUD;
    public FriendManager friendManager;
    public MacroManager macroManager;
    public FileManager fileManager;
    public CommandManager commandManager;
    public ConfigManager configManager;
    public ClickGuiScreen newGui;
    public RotationHelper.Rotation rotation;
    public HwidUtil hwidUtil;
    public String hwid;
    public int a;
    public Client client;
    
    public Fluger() {
        this.a = 0;
        this.client = new Client();
    }
    
    public static double deltaTime() {
        return (bib.af() > 0) ? (1.0 / bib.af()) : 1.0;
    }
    
    public void loadClient() {
        Display.setTitle(Fluger.name + " " + Fluger.status + " @Draconix.today | https://discord.gg/8BUReKrErq");
        this.time = System.currentTimeMillis();
        this.draggableHUD = new DraggableHUD();
        this.hwidUtil = new HwidUtil();
        this.requst = new RequstUtil();
        final TpsHelper tpsUtils = new TpsHelper();
        this.hwid = this.hwidUtil.getHwid();
        (this.fileManager = new FileManager()).loadFiles();
        this.friendManager = new FriendManager();
        this.featureManager = new FeatureManager();
        this.commandManager = new CommandManager();
        this.featureManager = new FeatureManager();
        this.macroManager = new MacroManager();
        this.configManager = new ConfigManager();
        this.newGui = new ClickGuiScreen();
        this.rotation = new RotationHelper.Rotation();
        try {
            this.fileManager.getFile(FriendConfig.class).loadFile();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.fileManager.getFile(HudConfig.class).loadFile();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.fileManager.getFile(MacroConfig.class).loadFile();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        EventManager.register(this.rotation);
        EventManager.register(this);
    }
    
    public void shutDown() {
        Fluger.instance.configManager.saveConfig("default");
        (this.fileManager = new FileManager()).saveFiles();
        EventManager.unregister(this);
    }
    
    @EventTarget
    public void onInputKey(final EventInputKey event) {
        for (final Feature feature : this.featureManager.getFeatureList()) {
            if (feature.getBind() != event.getKey()) {
                continue;
            }
            feature.toggle();
        }
    }
    
    public int getRandomInRange(final int min, final int max) {
        final Random rand = new Random();
        final int randomNum = rand.nextInt(max - min + 1) + min;
        return randomNum;
    }
    
    static {
        Fluger.instance = new Fluger();
        Fluger.name = "VanePremium";
        Fluger.type = "Rage";
        Fluger.version = "b1";
        Fluger.status = "Client";
    }
}
