package fun.expensive.client;


import fun.expensive.client.command.CommandManager;
import fun.expensive.client.command.macro.MacroManager;
import fun.expensive.client.draggable.DraggableHUD;
import fun.expensive.client.event.EventManager;
import fun.expensive.client.event.EventTarget;
import fun.expensive.client.event.events.impl.input.EventInputKey;
import fun.expensive.client.feature.Feature;
import fun.expensive.client.feature.FeatureManager;
import fun.expensive.client.files.FileManager;
import fun.expensive.client.files.impl.FriendConfig;
import fun.expensive.client.files.impl.HudConfig;
import fun.expensive.client.files.impl.MacroConfig;
import fun.expensive.client.friend.FriendManager;
import ViaMCP.ViaMCP;
import fun.expensive.client.ui.clickgui.ClickGuiScreen;
import fun.expensive.client.ui.config.ConfigManager;
import fun.expensive.client.utils.math.RotationHelper;
import fun.expensive.client.utils.math.TPSUtils;
import fun.expensive.client.utils.other.DiscordHelper;
import fun.expensive.client.utils.render.cosmetic.CosmeticRender;
import fun.expensive.client.utils.render.cosmetic.impl.DragonWing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.lwjgl.opengl.Display;

import java.io.IOException;

public class Expensive {
    public Long time;

    public FeatureManager featureManager;
    public FileManager fileManager;
    public static long playTimeStart = 0;
    public DraggableHUD draggableHUD;

    public MacroManager macroManager;
    public ConfigManager configManager;

    public CommandManager commandManager;

    public FriendManager friendManager;
    public ClickGuiScreen clickGui;
    public static Expensive instance = new Expensive();

    public static double deltaTime() {
        return Minecraft.getDebugFPS() > 0 ? (1.0000 / Minecraft.getDebugFPS()) : 1;
    }

    public String name = "Capybara", type = "Premium", version = "Release";

    public void init() {

        time = System.currentTimeMillis();
        Display.setTitle(name + " " + type + " - https://vk.com/capybaraclient");
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
            ViaMCP.getInstance().start();
            ViaMCP.getInstance().initAsyncSlider();
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
        EventManager.register(this);
    }

    public void stop() {
        expensive.instance.configManager.saveConfig("default");
        fileManager.saveFiles();
        DiscordHelper.stopRPC();
        EventManager.unregister(this);
    }

    @EventTarget
    public void onInputKey(EventInputKey event) {
        featureManager.getAllFeatures().stream().filter(module -> module.getBind() == event.getKey()).forEach(Feature::toggle);
        macroManager.getMacros().stream().filter(macros -> macros.getKey() == event.getKey()).forEach(macros -> Minecraft.getMinecraft().player.sendChatMessage(macros.getValue()));
    }
}
