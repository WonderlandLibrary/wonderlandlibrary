package fun.expensive.client.feature.impl.hud;

import fun.rich.client.Rich;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.ui.settings.impl.BooleanSetting;
import fun.rich.client.ui.settings.impl.ColorSetting;
import fun.rich.client.ui.settings.impl.ListSetting;
import fun.rich.client.ui.settings.impl.NumberSetting;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class ClickGUI extends Feature {
    public static BooleanSetting girl = new BooleanSetting("Anime", true, () -> true);
    public static BooleanSetting potato_mode = new BooleanSetting("Potato Mode", false, () -> true);
    public static ListSetting backGroundColor = new ListSetting("Background Color", "Static", () -> !potato_mode.getBoolValue(), "Astolfo", "Rainbow", "Static");
    public static ListSetting girlmode = new ListSetting("Anime Mode", "Girl", () -> girl.getBoolValue(), "Girl", "Rem", "Kaneki", "Violet", "Kirshtein", "002");
    public static ListSetting panelMode = new ListSetting("Panel Mode", "Rect", () -> girl.getBoolValue(), "Rect", "Blur");
    public static BooleanSetting glow = new BooleanSetting("Glow", true, () -> !potato_mode.getBoolValue());
    public static BooleanSetting particles = new BooleanSetting("Particles", true, () -> !potato_mode.getBoolValue());

    public static BooleanSetting blur = new BooleanSetting("Blur", true, () -> !potato_mode.getBoolValue());
    public static NumberSetting blurInt = new NumberSetting("Blur Amount", 5, 5, 10, 1, () -> blur.getBoolValue() && !potato_mode.getBoolValue());
    public static ColorSetting color;
    public static ColorSetting bgcolor;
    public static NumberSetting speed = new NumberSetting("Speed", 35, 10, 100, 1, () -> true);
    public static NumberSetting glowRadius2 = new NumberSetting("Glow Radius", 10, 5, 55, 1, () -> !potato_mode.getBoolValue());

    public ClickGUI() {
        super("ClickGUI", "Меню чита", FeatureCategory.Hud);
        setBind(Keyboard.KEY_RSHIFT);
        color = new ColorSetting("Gui Color", new Color(34, 179, 255, 255).getRGB(), () -> true);
        bgcolor = new ColorSetting("Color One", new Color(34, 179, 255, 255).getRGB(), () -> backGroundColor.currentMode.equals("Static"));
        addSettings(panelMode, potato_mode, color, glow, glowRadius2, speed, backGroundColor, bgcolor, blur, blurInt, particles, girl, girlmode);

    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(Rich.instance.clickGui);
        Rich.instance.featureManager.getFeature(ClickGUI.class).setEnabled(false);
        super.onEnable();
    }
}