package ru.smertnix.celestial.feature.impl.hud;

import net.minecraft.potion.PotionEffect;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.render.EventRender2D;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.BooleanSetting;
import ru.smertnix.celestial.ui.settings.impl.ColorSetting;
import ru.smertnix.celestial.ui.settings.impl.ListSetting;
import ru.smertnix.celestial.ui.settings.impl.NumberSetting;
import ru.smertnix.celestial.utils.Helper;
import ru.smertnix.celestial.utils.math.AnimationHelper;
import ru.smertnix.celestial.utils.render.ClientHelper;
import ru.smertnix.celestial.utils.render.GLUtils;
import ru.smertnix.celestial.utils.render.RenderUtils;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import java.awt.*;
import java.util.Comparator;
import java.util.List;

public class ClientColor extends Feature {
	
	public static ListSetting fontMode;
    public static BooleanSetting minecraftfont;
    public static ListSetting arrayColor;
    public static NumberSetting time;
    public static ColorSetting onecolor;
    public static ColorSetting twocolor;

    public ClientColor() {
        super("ClientSettings", FeatureCategory.Render);
        arrayColor = new ListSetting("ClientColor", "Custom", () -> true, new String[] { "Custom", "Rainbow", "Pulse", "Astolfo", "None" });
        time = new NumberSetting("ClientColor Time", 10.0f, 1.0f, 100.0f, 1.0f, () -> true);
        onecolor = new ColorSetting("One Color", new Color(200, 0, 255).getRGB(), () -> arrayColor.currentMode.equals("Custom"));
        twocolor = new ColorSetting("Two Color", new Color(1671168).getRGB(), () -> arrayColor.currentMode.equals("Custom"));

        fontMode = new ListSetting("ClientFont", "WexSide", () -> !minecraftfont.getBoolValue(), "URWGeometric", "Myseo", "SFUI", "Lato", "Roboto Regular", "WexSide", "NeverLose", "Comic Sans", "TenacityBold", "Tenacity", "TahomaBold", "Tahoma", "RubikBold", "Rubik");
        minecraftfont = new BooleanSetting("Minecraft Font", false, () -> true);

        addSettings(arrayColor, time, onecolor, twocolor, fontMode,minecraftfont);
    }

@Override
public void onEnable() {
    toggle();
    super.onEnable();
}
}
