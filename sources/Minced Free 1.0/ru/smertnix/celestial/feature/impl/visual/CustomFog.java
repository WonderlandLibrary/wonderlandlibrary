package ru.smertnix.celestial.feature.impl.visual;

import java.awt.*;

import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.render.EventFogColor;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.ColorSetting;
import ru.smertnix.celestial.ui.settings.impl.ListSetting;
import ru.smertnix.celestial.ui.settings.impl.NumberSetting;
import ru.smertnix.celestial.utils.render.ClientHelper;
import ru.smertnix.celestial.utils.render.ColorUtils;

public class CustomFog extends Feature {

    public static NumberSetting distance;
    public ListSetting colorMode;
    public ColorSetting customColor;

    public CustomFog() {
        super("Custom Fog", "Меняет цвет тумана", FeatureCategory.Render);
        customColor = new ColorSetting("Custom Fog", new Color(158, 13, 239, 255).getRGB(), () -> true);
        distance = new NumberSetting("Distance", 0.10F, 0.01F, 2, 0.01F, () -> true);
        addSettings(colorMode, distance, customColor);
    }

    @EventTarget
    public void onFogColor(EventFogColor event) {
            Color customColorValue = new Color(customColor.getColorValue());
            event.setRed(customColorValue.getRed());
            event.setGreen(customColorValue.getGreen());
            event.setBlue(customColorValue.getBlue());
    }
}
