package com.masterof13fps.features.modules.impl.gui;

import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.manager.settingsmanager.Setting;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.features.modules.Category;

@ModuleInfo(name = "HUD", category = Category.GUI, description = "Shows a interface with all visual features of the Client")
public class HUD extends Module {

    Setting design = new Setting("Design", this, "Ambien Newest", new String[] {"Ambien Old", "Ambien " +
            "New",
            "Ambien" +
            " " +
            "Newest", "Vortex", "Suicide", "Apinity", "Huzuni",
    "Wurst", "Nodus", "Saint", "Icarus Old", "Icarus New", "Hero", "Klientus", "Koks"});
    Setting chatMode = new Setting("Chat Mode", this, "Custom", new String[] {"Normal", "Custom"});
    Setting chatFont = new Setting("Chat Font", this, "Comfortaa", new String[] {"Comfortaa", "Bauhaus",
            "Exo"});
    Setting hitAnimation = new Setting("Hit Animation", this, "Stoned", new String[] {"Normal", "Stoned"});
    Setting zoomMode = new Setting("Zoom Mode", this, "Smooth", new String[]{"Smooth", "OptiFine"});
    Setting hotbarMode = new Setting("Hotbar Mode", this, "Rainbow", new String[]{"Rainbow", "Static " +
            "Color"});
    Setting arrayListMode = new Setting("ArrayList Mode", this, "Cycle", new String[]{"Cycle", "Koks"});
    Setting itemHeight = new Setting("Item Height", this, 0.0F, -0.35F, 1.5F, false);
    Setting rainbowOffset = new Setting("Rainbow Offset", this, 200, 50, 1000, true);
    Setting rainbowSpeed = new Setting("Rainbow Speed", this, 10000, 500, 20000, true);
    Setting rainbowSaturation = new Setting("Rainbow Saturation", this, 1, 1, 5, true);
    Setting rainbowBrightness = new Setting("Rainbow Brightness", this, 1, 1, 5, true);
    Setting hotbar = new Setting("Hotbar", this, true);
    Setting arrayList = new Setting("ArrayList", this, true);
    Setting tabGui = new Setting("TabGUI", this, true);
    Setting watermark = new Setting("Watermark", this, true);
    Setting keyStrokes = new Setting("KeyStrokes", this, true);
    Setting guiAnimation = new Setting("GUI Animation", this, true);
    Setting guiBlur = new Setting("GUI Blur", this, true);
    Setting blockhitAnimation = new Setting("Blockhit Animation", this, true);
    Setting toggleNotifications = new Setting("Toggle Notifications", this, false);
    Setting customScoreboard = new Setting("Custom Scoreboard", this, true);
    Setting developerMode = new Setting("Developer Mode", this, false);

    @Override
    public void onToggle() {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onEvent(Event event) {
    }
}
