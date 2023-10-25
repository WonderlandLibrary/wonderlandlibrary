package org.dreamcore.client;

import baritone.api.BaritoneAPI;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.dreamcore.client.cmd.CommandManager;
import org.dreamcore.client.components.DiscordRPC;
import org.dreamcore.client.components.SplashProgress;
import org.dreamcore.client.event.EventManager;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.game.EventShutdownClient;
import org.dreamcore.client.event.events.impl.input.EventInputKey;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.FeatureManager;
import org.dreamcore.client.files.FileManager;
import org.dreamcore.client.files.impl.FriendConfig;
import org.dreamcore.client.files.impl.HudConfig;
import org.dreamcore.client.files.impl.MacroConfig;
import org.dreamcore.client.files.impl.XrayConfig;
import org.dreamcore.client.friend.FriendManager;
import org.dreamcore.client.helpers.Helper;
import org.dreamcore.client.helpers.math.RotationHelper;
import org.dreamcore.client.helpers.render.BlurUtil;
import org.dreamcore.client.macro.Macro;
import org.dreamcore.client.macro.MacroManager;
import org.dreamcore.client.settings.config.ConfigManager;
import org.dreamcore.client.ui.GuiCapeSelector;
import org.dreamcore.client.ui.components.changelog.ChangeManager;
import org.dreamcore.client.ui.components.draggable.DraggableManager;
import org.dreamcore.client.ui.newclickgui.ClickGuiScreen;

import java.io.IOException;

public class dreamcore implements Helper {

    public static dreamcore instance = new dreamcore();

    public String name = "dreamcore", type = "GOVNO", version = "1.0", status = "Release", build = "161021";
    public FeatureManager featureManager = new FeatureManager();
    public org.dreamcore.client.ui.clickgui.ClickGuiScreen clickGui;
    public ClickGuiScreen newClickGui;
    public CommandManager commandManager;
    public ConfigManager configManager;
    public MacroManager macroManager;
    public FileManager fileManager;
    public DraggableManager draggableManager;
    public FriendManager friendManager;
    public RotationHelper.Rotation rotation;
    public BlurUtil blurUtil;
    public ChangeManager changeManager;

    public void load() throws IOException {

        SplashProgress.setProgress(1);

        Display.setTitle(name + " " + type + " " + version);
        new DiscordRPC().init();

        GuiCapeSelector.Selector.setCapeName("dreamcorecape3");
        (fileManager = new FileManager()).loadFiles();
        featureManager = new FeatureManager();
        clickGui = new org.dreamcore.client.ui.clickgui.ClickGuiScreen();
        newClickGui = new ClickGuiScreen();
        commandManager = new CommandManager();
        configManager = new ConfigManager();
        macroManager = new MacroManager();
        draggableManager = new DraggableManager();
        friendManager = new FriendManager();
        rotation = new RotationHelper.Rotation();
        blurUtil = new BlurUtil();
        changeManager = new ChangeManager();

        BaritoneAPI.getProvider().getPrimaryBaritone();

        try {
            viamcp.ViaMCP.getInstance().start();
        } catch (Exception e) {

        }

        try {
            fileManager.getFile(FriendConfig.class).loadFile();
        } catch (Exception e) {
        }

        try {
            fileManager.getFile(MacroConfig.class).loadFile();
        } catch (Exception e) {

        }

        try {
            fileManager.getFile(HudConfig.class).loadFile();
        } catch (Exception e) {
        }

        try {
            fileManager.getFile(XrayConfig.class).loadFile();
        } catch (Exception e) {
        }

        EventManager.register(rotation);
        EventManager.register(this);
    }

    @EventTarget
    public void shutDown(EventShutdownClient event) {
        EventManager.unregister(this);
        (fileManager = new FileManager()).saveFiles();
        new DiscordRPC().shutdown();
    }

    @EventTarget
    public void onInputKey(EventInputKey event) {
        for (Feature feature : featureManager.getFeatureList()) {
            if (feature.getBind() == event.getKey()) {
                feature.toggle();
            }
        }
        for (Macro macro : macroManager.getMacros()) {
            if (macro.getKey() == Keyboard.getEventKey()) {
                if (mc.player.getHealth() > 0 && mc.player != null) {
                    mc.player.sendChatMessage(macro.getValue());
                }
            }
        }
    }
}
