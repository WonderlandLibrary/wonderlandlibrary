/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.visuals;

import java.awt.Color;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.render.EventFogColor;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.misc.ClientHelper;
import org.celestial.client.helpers.palette.PaletteHelper;
import org.celestial.client.settings.impl.ColorSetting;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class FogColor
extends Feature {
    public ListSetting colorMode = new ListSetting("Fog Color", "Rainbow", () -> true, "Rainbow", "Client", "Custom");
    public static NumberSetting distance;
    public ColorSetting customColor;

    public FogColor() {
        super("FogColor", "\u0414\u0435\u043b\u0430\u0435\u0442 \u0446\u0432\u0435\u0442 \u0442\u0443\u043c\u0430\u043d\u0430 \u0434\u0440\u0443\u0433\u0438\u043c", Type.Visuals);
        distance = new NumberSetting("Fog Distance", 0.7f, 0.1f, 2.0f, 0.01f, () -> true);
        this.customColor = new ColorSetting("Custom Fog", new Color(0xFFFFFF).getRGB(), () -> this.colorMode.currentMode.equals("Custom"));
        this.addSettings(this.colorMode, distance, this.customColor);
    }

    @EventTarget
    public void onFogColor(EventFogColor event) {
        String colorModeValue = this.colorMode.getOptions();
        if (colorModeValue.equalsIgnoreCase("Rainbow")) {
            Color color = PaletteHelper.rainbow(1, 1.0f, 1.0f);
            event.setRed(color.getRed());
            event.setGreen(color.getGreen());
            event.setBlue(color.getBlue());
        } else if (colorModeValue.equalsIgnoreCase("Client")) {
            Color clientColor = ClientHelper.getClientColor();
            event.setRed(clientColor.getRed());
            event.setGreen(clientColor.getGreen());
            event.setBlue(clientColor.getBlue());
        } else if (colorModeValue.equalsIgnoreCase("Custom")) {
            Color customColorValue = new Color(this.customColor.getColor());
            event.setRed(customColorValue.getRed());
            event.setGreen(customColorValue.getGreen());
            event.setBlue(customColorValue.getBlue());
        }
    }
}

