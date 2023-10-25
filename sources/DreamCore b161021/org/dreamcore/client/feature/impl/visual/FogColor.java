package org.dreamcore.client.feature.impl.visual;

import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.render.EventFogColor;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.misc.ClientHelper;
import org.dreamcore.client.helpers.palette.PaletteHelper;
import org.dreamcore.client.settings.impl.ColorSetting;
import org.dreamcore.client.settings.impl.ListSetting;
import org.dreamcore.client.settings.impl.NumberSetting;

import java.awt.*;

public class FogColor extends Feature {

    public static NumberSetting distance;
    public ListSetting colorMode;
    public ColorSetting customColor;

    public FogColor() {
        super("FogColor", "Делает цвет тумана другим", Type.Visuals);
        colorMode = new ListSetting("Fog Color", "Rainbow", () -> true, "Rainbow", "Client", "Custom");
        distance = new NumberSetting("Distance", 0.10F, 0.001F, 2, 0.01F, () -> true);
        customColor = new ColorSetting("Custom Fog", new Color(0xFFFFFF).getRGB(), () -> colorMode.currentMode.equals("Custom"));
        addSettings(colorMode, distance, customColor);
    }

    @EventTarget
    public void onFogColor(EventFogColor event) {
        String colorModeValue = colorMode.getOptions();
        if (colorModeValue.equalsIgnoreCase("Rainbow")) {
            Color color = PaletteHelper.rainbow(1, 1, 1);
            event.setRed(color.getRed());
            event.setGreen(color.getGreen());
            event.setBlue(color.getBlue());
        } else if (colorModeValue.equalsIgnoreCase("Client")) {
            Color clientColor = ClientHelper.getClientColor();
            event.setRed(clientColor.getRed());
            event.setGreen(clientColor.getGreen());
            event.setBlue(clientColor.getBlue());
        } else if (colorModeValue.equalsIgnoreCase("Custom")) {
            Color customColorValue = new Color(customColor.getColorValue());
            event.setRed(customColorValue.getRed());
            event.setGreen(customColorValue.getGreen());
            event.setBlue(customColorValue.getBlue());
        }
    }
}