package org.dreamcore.client.feature.impl.hud;

import org.dreamcore.client.dreamcore;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.render.EventRender2D;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.settings.impl.BooleanSetting;
import org.dreamcore.client.settings.impl.ColorSetting;
import org.dreamcore.client.settings.impl.ListSetting;
import org.dreamcore.client.ui.components.draggable.DraggableModule;

import java.awt.*;

public class WaterMark extends Feature {

    public static ListSetting logoMode;
    public static ListSetting logoColor;
    public static ListSetting colorRectPosition = new ListSetting("ColorRect Position", "Top", () -> !logoMode.currentMode.equals("Skeet") && !logoMode.currentMode.equals("NeverLose") && !logoMode.currentMode.equals("Default"), "Bottom", "Top");
    public static ColorSetting customRect;
    public static ColorSetting customRectTwo;

    public static BooleanSetting glowEffect = new BooleanSetting("Glow Effect", false, () -> logoMode.currentMode.equals("OneTap v3") || logoMode.currentMode.equals("OneTap v2"));
    public static BooleanSetting shadowEffect = new BooleanSetting("Shadow Effect", false, () -> logoMode.currentMode.equals("NeverLose") || logoMode.currentMode.equals("OneTap v3") || logoMode.currentMode.equals("OneTap v2"));

    public WaterMark() {
        super("WaterMark", "Ватермарк чита", Type.Hud);
        logoMode = new ListSetting("Logo Mode", "Default", () -> true, "Default", "Skeet", "OneTap v2", "OneTap v3", "NeverLose", "NoRender");
        logoColor = new ListSetting("Logo Color", "Default", () -> !WaterMark.logoMode.currentMode.equals("NeverLose"), "Client", "Rainbow", "Gradient", "Static", "Default");
        customRect = new ColorSetting("Custom Rect Color", Color.PINK.getRGB(), () -> !WaterMark.logoMode.currentMode.equals("NeverLose"));
        customRectTwo = new ColorSetting("Custom Rect Color Two", Color.BLUE.getRGB(), () -> !WaterMark.logoMode.currentMode.equals("NeverLose") && !WaterMark.logoMode.currentMode.equals("Default"));
        this.addSettings(logoMode, colorRectPosition, logoColor, glowEffect, shadowEffect, customRect, customRectTwo);
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        for (DraggableModule draggableModule : dreamcore.instance.draggableManager.getMods()) {
            if (getState() && !draggableModule.name.equals("ClientInfoComponent") && !draggableModule.name.equals("InfoComponent") && !draggableModule.name.equals("PotionComponent") && !draggableModule.name.equals("ArmorComponent") && !draggableModule.name.equals("TargetHUDComponent")) {
                draggableModule.draw();
            }
        }
    }
}
