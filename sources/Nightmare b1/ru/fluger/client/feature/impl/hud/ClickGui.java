// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.hud;

import ru.fluger.client.Fluger;
import ru.fluger.client.settings.Setting;
import java.awt.Color;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.ListSetting;
import ru.fluger.client.settings.impl.ColorSetting;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.feature.Feature;

public class ClickGui extends Feature
{
    public static BooleanSetting gradientBackground;
    public static BooleanSetting glow;
    public static NumberSetting glowRadius;
    public static BooleanSetting backGroundBlur;
    public static NumberSetting backGroundBlurStrength;
    public static NumberSetting scale;
    public static ColorSetting color;
    public static ListSetting panelMode;
    public static BooleanSetting girl;
    public static ListSetting girlMode;
    
    public ClickGui() {
        super("ClickGui", "\u041e\u0442\u043a\u0440\u044b\u0432\u0430\u0435\u0442 \u043a\u043b\u0438\u043a \u0433\u0443\u0439 \u0447\u0438\u0442\u0430", Type.Hud);
        this.setBind(54);
        ClickGui.scale = new NumberSetting("Scale", 1.0f, 0.5f, 1.0f, 0.1f, () -> true);
        ClickGui.panelMode = new ListSetting("Panel Mode", "Rect", new String[] { "Rect", "Blur" });
        ClickGui.color = new ColorSetting("Gui Color", new Color(1, 182, 83, 25).getRGB(), () -> true);
        ClickGui.gradientBackground = new BooleanSetting("Gradient Background", true, () -> true);
        ClickGui.glow = new BooleanSetting("Glow", true, () -> true);
        ClickGui.glowRadius = new NumberSetting("Glow Radius", 18.0f, 1.0f, 100.0f, 1.0f, () -> ClickGui.glow.getCurrentValue());
        ClickGui.backGroundBlur = new BooleanSetting("Background Blur", true, () -> true);
        ClickGui.backGroundBlurStrength = new NumberSetting("Blur Strength", 15.0f, 1.0f, 30.0f, 1.0f, () -> ClickGui.backGroundBlur.getCurrentValue());
        ClickGui.girl = new BooleanSetting("Girl", true, () -> true);
        this.addSettings(ClickGui.color, ClickGui.glow, ClickGui.glowRadius, ClickGui.panelMode, ClickGui.scale, ClickGui.gradientBackground, ClickGui.backGroundBlur, ClickGui.backGroundBlurStrength, ClickGui.girl, ClickGui.girlMode);
    }
    
    @Override
    public void onEnable() {
        ClickGui.mc.a(Fluger.instance.newGui);
        Fluger.instance.featureManager.getFeatureByClass(ClickGui.class).setState(false);
        super.onEnable();
    }
    
    static {
        ClickGui.girlMode = new ListSetting("Girl Mode", "Girl1", () -> ClickGui.girl.getCurrentValue(), new String[] { "Girl1", "Girl2", "Girl3", "Girl4", "Girl5", "Girl6", "Girl7" });
    }
}
