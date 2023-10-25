package org.dreamcore.client.feature.impl.hud;

import org.lwjgl.input.Keyboard;
import org.dreamcore.client.dreamcore;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.settings.impl.BooleanSetting;
import org.dreamcore.client.settings.impl.ColorSetting;
import org.dreamcore.client.settings.impl.ListSetting;
import org.dreamcore.client.settings.impl.NumberSetting;

import java.awt.*;

public class ClickGui extends Feature {

    public static BooleanSetting background;
    public static BooleanSetting blur = new BooleanSetting("Blur", true, () -> true);
    public static ListSetting clickGuiColor = new ListSetting("ClickGui Color", "Fade", () -> true, "Astolfo", "Rainbow", "Static", "Color Two", "Client", "Fade");
    public static ColorSetting color;
    public static ColorSetting colorTwo;
    public static NumberSetting speed = new NumberSetting("Speed", 35, 10, 100, 1, () -> true);
    public static NumberSetting scrollSpeed = new NumberSetting("ScrollSpeed", 20, 1, 100, 1, () -> true);
    public static BooleanSetting glow = new BooleanSetting("Glow Effect", false, () -> true);
    public static BooleanSetting shadow = new BooleanSetting("Shadow Effect", false, () -> true);
    public static BooleanSetting sliderGlow = new BooleanSetting("Slider Glow", false, () -> glow.getBoolValue());
    public static BooleanSetting checkBoxGlow = new BooleanSetting("CheckBox Effect", false, () -> glow.getBoolValue());
    public static BooleanSetting searchGlow = new BooleanSetting("Search Effect", false, () -> glow.getBoolValue());
    public ListSetting mode = new ListSetting("ClickGui Mode", "New", () -> true, "Old", "New");

    public ClickGui() {
        super("ClickGui", "Открывает клик гуй чита", Type.Hud);
        setBind(Keyboard.KEY_RSHIFT);
        color = new ColorSetting("Color One", new Color(0, 21, 255, 120).getRGB(), () -> clickGuiColor.currentMode.equals("Fade") || clickGuiColor.currentMode.equals("Color Two") || clickGuiColor.currentMode.equals("Static"));
        colorTwo = new ColorSetting("Color Two", new Color(132, 0, 255, 120).getRGB(), () -> clickGuiColor.currentMode.equals("Color Two"));
        background = new BooleanSetting("Background", true, () -> true);
        addSettings(mode, scrollSpeed, clickGuiColor, color, colorTwo, speed, background, blur, glow, shadow, sliderGlow, checkBoxGlow, searchGlow);
    }

    @Override
    public void onEnable() {
        if (mode.currentMode.equals("New")) {
            mc.displayGuiScreen(dreamcore.instance.newClickGui);
        } else if (mode.currentMode.equals("Old")) {
            mc.displayGuiScreen(dreamcore.instance.clickGui);
        }
        dreamcore.instance.featureManager.getFeatureByClass(ClickGui.class).setState(false);
        super.onEnable();
    }
}
