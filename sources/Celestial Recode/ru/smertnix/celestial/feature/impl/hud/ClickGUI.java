package ru.smertnix.celestial.feature.impl.hud;

import org.lwjgl.input.Keyboard;

import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.BooleanSetting;
import ru.smertnix.celestial.ui.settings.impl.ColorSetting;
import ru.smertnix.celestial.ui.settings.impl.ListSetting;
import ru.smertnix.celestial.ui.settings.impl.NumberSetting;

import java.awt.*;

public class ClickGUI extends Feature {
    public static BooleanSetting shadow = new BooleanSetting("Shadow", true, () -> true);

    public static BooleanSetting blur = new BooleanSetting("Blur", true, () -> true);
    public static ColorSetting color;
    public static ColorSetting color2;
    public ClickGUI() {
        super("Click GUI", "Клик Гуи", FeatureCategory.Render);
        setBind(Keyboard.KEY_RSHIFT);
        color = new ColorSetting("Gui Color", new Color(158, 13, 239, 255).getRGB(), () -> true);
        color2 = new ColorSetting("Gui Color 2 ", new Color(255, 0, 0, 255).getRGB(), () -> true);
        addSettings(color,color2, blur, shadow);

    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(Celestial.instance.clickGui);
        Celestial.instance.featureManager.getFeature(ClickGUI.class).setEnabled(false);
        super.onEnable();
    }
}