package ru.smertnix.celestial;


import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderPlayer;
import ru.smertnix.celestial.command.CommandManager;
import ru.smertnix.celestial.command.macro.MacroManager;
import ru.smertnix.celestial.draggable.DraggableHUD;
import ru.smertnix.celestial.event.EventManager;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.input.EventInputKey;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.FeatureManager;
import ru.smertnix.celestial.files.FileManager;
import ru.smertnix.celestial.files.impl.ClientConfig;
import ru.smertnix.celestial.files.impl.FriendConfig;
import ru.smertnix.celestial.files.impl.HudConfig;
import ru.smertnix.celestial.files.impl.MacroConfig;
import ru.smertnix.celestial.files.impl.QuickJoin;
import ru.smertnix.celestial.friend.FriendManager;
import ru.smertnix.celestial.ui.altmanager.alt.Server;
import ru.smertnix.celestial.ui.altmanager.alt.ServerManager;
import ru.smertnix.celestial.ui.changelog.ChangeManager;
import ru.smertnix.celestial.ui.clickgui.ClickGuiScreen;
import ru.smertnix.celestial.ui.config.ConfigManager;
import ru.smertnix.celestial.utils.math.RotationHelper;
import ru.smertnix.celestial.utils.math.TPSUtils;
import ru.smertnix.celestial.utils.render.ScaleUtils;
import ru.smertnix.celestial.utils.render.cosmetic.CosmeticRender;
import ru.smertnix.celestial.utils.render.cosmetic.impl.DragonWing;
import org.lwjgl.opengl.Display;

import java.io.IOException;

public class Celestial {
    public Long time;

    public FeatureManager featureManager;
    public FileManager fileManager;
    public static long playTimeStart = 0;
    public DraggableHUD draggableHUD;
    public static ScaleUtils scale = new ScaleUtils(2);
    public MacroManager macroManager;
    public ConfigManager configManager;

    public CommandManager commandManager;
    public ChangeManager c;
    public FriendManager friendManager;
    public ClickGuiScreen clickGui;
    public static String nick = "";
    public static String uid = "";
    public static Celestial instance = new Celestial();

    public static double deltaTime() {
        return Minecraft.getDebugFPS() > 0 ? (1.0000 / Minecraft.getDebugFPS()) : 1;
    }
    public static boolean shaders = true;
    public static boolean hi;
    public static boolean pin;
    public String name = "Minced", type = "Free(28.02.2023 update)";

	public static String version = "1.0";

    public void init() {

    	c.addLogs();
        time = System.currentTimeMillis();
        Display.setTitle(name + " " + type);
        (fileManager = new FileManager()).loadFiles();

        friendManager = new FriendManager();
        featureManager = new FeatureManager();

        macroManager = new MacroManager();
        configManager = new ConfigManager();
        draggableHUD = new DraggableHUD();
        commandManager = new CommandManager();
        clickGui = new ClickGuiScreen();
        new DragonWing();
        for (RenderPlayer render : Minecraft.getMinecraft().getRenderManager().getSkinMap().values()) {
            render.addLayer(new CosmeticRender(render));
        }
        TPSUtils tpsUtils = new TPSUtils();
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.fileManager.getFile(FriendConfig.class).loadFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.fileManager.getFile(MacroConfig.class).loadFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.fileManager.getFile(HudConfig.class).loadFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.fileManager.getFile(ClientConfig.class).loadFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.fileManager.getFile(QuickJoin.class).loadFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        EventManager.register(this);
        if (!ServerManager.inRegistry("SunRise")) {
       		ServerManager.registry.add(new Server("MST Network","mc.mstnw.net"));
           	ServerManager.registry.add(new Server("Really World","mc.reallyworld.ru"));
           	ServerManager.registry.add(new Server("Nexus Grief","ngrief.su"));
           	ServerManager.registry.add(new Server("MineBlaze","mineblaze.ru"));
           	ServerManager.registry.add(new Server("SunRise","play.sunmc.ru"));
       	}
    }

    public void stop() {
        Celestial.instance.configManager.saveConfig("default");
        fileManager.saveFiles();
        EventManager.unregister(this);
    }

    @EventTarget
    public void onInputKey(EventInputKey event) {
        featureManager.getAllFeatures().stream().filter(module -> module.getBind() == event.getKey()).forEach(Feature::toggle);
        macroManager.getMacros().stream().filter(macros -> macros.getKey() == event.getKey()).forEach(macros -> Minecraft.getMinecraft().player.sendChatMessage(macros.getValue()));
    }
}
