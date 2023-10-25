/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.hud;

import java.awt.Color;
import org.celestial.client.Celestial;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.ColorSetting;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class ClickGui
extends Feature {
    public static ListSetting scrollMode;
    public static NumberSetting scrollSpeed;
    public static BooleanSetting scrollInversion;
    public static BooleanSetting gradientBackground;
    public static BooleanSetting glow;
    public static NumberSetting glowRadius;
    public static BooleanSetting backGroundBlur;
    public static NumberSetting backGroundBlurStrength;
    public static NumberSetting scale;
    public static ColorSetting color;
    public static BooleanSetting girl;
    public static BooleanSetting arrows;
    public static ListSetting panelMode;
    public static ListSetting girlMode;

    public ClickGui() {
        super("ClickGui", "\u041e\u0442\u043a\u0440\u044b\u0432\u0430\u0435\u0442 \u043a\u043b\u0438\u043a \u0433\u0443\u0439 \u0447\u0438\u0442\u0430", Type.Hud);
        this.setBind(54);
        scale = new NumberSetting("Scale", 1.0f, 0.5f, 1.0f, 0.1f, () -> true);
        scrollMode = new ListSetting("Scroll Mode", "All Panels", "All Panels", "One Panel");
        scrollSpeed = new NumberSetting("Scroll Speed", 15.0f, 5.0f, 30.0f, 1.0f, () -> true);
        scrollInversion = new BooleanSetting("Scroll Inversion", true, () -> true);
        color = new ColorSetting("Gui Color", new Color(200, 78, 205, 25).getRGB(), () -> true);
        gradientBackground = new BooleanSetting("Gradient Background", true, () -> true);
        glow = new BooleanSetting("Glow", true, () -> true);
        glowRadius = new NumberSetting("Glow Radius", 18.0f, 1.0f, 100.0f, 1.0f, () -> glow.getCurrentValue());
        backGroundBlur = new BooleanSetting("Background Blur", true, () -> true);
        backGroundBlurStrength = new NumberSetting("Blur Strength", 15.0f, 1.0f, 30.0f, 1.0f, () -> backGroundBlur.getCurrentValue());
        arrows = new BooleanSetting("Arrows", true, () -> true);
        girl = new BooleanSetting("Girl", true, () -> true);
        this.addSettings(color, glow, glowRadius, panelMode, scale, gradientBackground, backGroundBlur, backGroundBlurStrength, scrollMode, scrollSpeed, scrollInversion, arrows, girl, girlMode);
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(Celestial.instance.newGui);
        Celestial.instance.featureManager.getFeatureByClass(ClickGui.class).setState(false);
        super.onEnable();
    }

    static {
        panelMode = new ListSetting("Panel Mode", "Rect", "Rect", "Blur");
        girlMode = new ListSetting("Girl Mode", "Girl1", () -> girl.getCurrentValue(), "Girl1", "Girl2", "Girl3", "Girl4", "Girl5", "Girl6", "Random");
    }
}

