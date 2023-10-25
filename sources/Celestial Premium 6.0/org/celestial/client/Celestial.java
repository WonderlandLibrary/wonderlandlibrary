/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client;

import java.io.IOException;
import org.celestial.client.cmd.CommandManager;
import org.celestial.client.event.EventManager;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.game.EventShutdownClient;
import org.celestial.client.event.events.impl.input.EventInputKey;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.FeatureManager;
import org.celestial.client.files.FileManager;
import org.celestial.client.files.impl.FriendConfig;
import org.celestial.client.files.impl.HudConfig;
import org.celestial.client.files.impl.MacroConfig;
import org.celestial.client.files.impl.XrayConfig;
import org.celestial.client.friend.FriendManager;
import org.celestial.client.helpers.Helper;
import org.celestial.client.helpers.math.RotationHelper;
import org.celestial.client.helpers.misc.TpsHelper;
import org.celestial.client.macro.Macro;
import org.celestial.client.macro.MacroManager;
import org.celestial.client.settings.config.ConfigManager;
import org.celestial.client.ui.GuiCapeSelector;
import org.celestial.client.ui.clickgui.ClickGuiScreen;
import org.celestial.client.ui.components.changelog.ChangeLogManager;
import org.celestial.client.ui.components.draggable.DraggableManager;
import org.lwjgl.input.Keyboard;
import viamcp.ViaMCP;

public class Celestial
implements Helper {
    public static Celestial instance = new Celestial();
    public static String name = "Celestial";
    public static String type = "Rage";
    public static String version = "0006827 (b6)";
    public static String status = "Premium";
    private String licenseDate = "0.0.1990";
    private String licenseName = "Developer";
    public FeatureManager featureManager = new FeatureManager();
    public ClickGuiScreen newGui;
    public CommandManager commandManager;
    public ChangeLogManager changeLogManager;
    public ConfigManager configManager;
    public MacroManager macroManager;
    public FileManager fileManager;
    public DraggableManager draggableManager;
    public FriendManager friendManager;
    public RotationHelper.Rotation rotation;

    public void loadClient() {
        GuiCapeSelector.Selector.setCapeName("celestial1");
        this.changeLogManager = new ChangeLogManager();
        this.changeLogManager.setChangeLogs();
        this.fileManager = new FileManager();
        this.fileManager.loadFiles();
        this.featureManager = new FeatureManager();
        this.newGui = new ClickGuiScreen();
        this.commandManager = new CommandManager();
        this.configManager = new ConfigManager();
        this.macroManager = new MacroManager();
        this.draggableManager = new DraggableManager();
        this.friendManager = new FriendManager();
        this.rotation = new RotationHelper.Rotation();
        TpsHelper tpsUtils = new TpsHelper();
        try {
            ViaMCP.getInstance().start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.fileManager.getFile(FriendConfig.class).loadFile();
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
        try {
            this.fileManager.getFile(HudConfig.class).loadFile();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.fileManager.getFile(XrayConfig.class).loadFile();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        EventManager.register(this.rotation);
        EventManager.register(this);
    }

    @EventTarget
    public void shutDown(EventShutdownClient event) {
        Celestial.instance.configManager.saveConfig("default");
        EventManager.unregister(this);
        this.fileManager = new FileManager();
        this.fileManager.saveFiles();
    }

    @EventTarget
    public void onInputKey(EventInputKey event) {
        for (Feature feature : this.featureManager.getFeatureList()) {
            if (feature.getBind() != event.getKey()) continue;
            feature.toggle();
        }
        for (Macro macro : this.macroManager.getMacros()) {
            if (macro.getKey() != Keyboard.getEventKey() || !(Celestial.mc.player.getHealth() > 0.0f) || Celestial.mc.player == null) continue;
            Celestial.mc.player.sendChatMessage(macro.getValue());
        }
    }

    public String getLicenseDate() {
        return this.licenseDate;
    }

    public void setLicenseDate(String licenseDate) {
        this.licenseDate = licenseDate;
    }

    public String getLicenseName() {
        return this.licenseName;
    }

    public void setLicenseName(String licenseName) {
        this.licenseName = licenseName;
    }
}

