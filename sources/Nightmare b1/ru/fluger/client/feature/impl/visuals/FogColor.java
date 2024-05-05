// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.visuals;

import ru.fluger.client.event.EventTarget;
import ru.fluger.client.helpers.misc.ClientHelper;
import ru.fluger.client.helpers.palette.PaletteHelper;
import ru.fluger.client.event.events.impl.render.EventFogColor;
import ru.fluger.client.settings.Setting;
import java.awt.Color;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.ColorSetting;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.settings.impl.ListSetting;
import ru.fluger.client.feature.Feature;

public class FogColor extends Feature
{
    public ListSetting colorMode;
    public static NumberSetting distance;
    public ColorSetting customColor;
    
    public FogColor() {
        super("FogColor", "\u0414\u0435\u043b\u0430\u0435\u0442 \u0446\u0432\u0435\u0442 \u0442\u0443\u043c\u0430\u043d\u0430 \u0434\u0440\u0443\u0433\u0438\u043c", Type.Visuals);
        this.colorMode = new ListSetting("Fog Color", "Rainbow", () -> true, new String[] { "Rainbow", "Client", "Custom" });
        FogColor.distance = new NumberSetting("Fog Distance", 0.7f, 0.1f, 2.0f, 0.01f, () -> true);
        this.customColor = new ColorSetting("Custom Fog", new Color(16777215).getRGB(), () -> this.colorMode.currentMode.equals("Custom"));
        this.addSettings(this.colorMode, FogColor.distance, this.customColor);
    }
    
    @EventTarget
    public void onFogColor(final EventFogColor event) {
        final String colorModeValue = this.colorMode.getOptions();
        if (colorModeValue.equalsIgnoreCase("Rainbow")) {
            final Color color = PaletteHelper.rainbow(1, 1.0f, 1.0f);
            event.setRed((float)color.getRed());
            event.setGreen((float)color.getGreen());
            event.setBlue((float)color.getBlue());
        }
        else if (colorModeValue.equalsIgnoreCase("Client")) {
            final Color clientColor = ClientHelper.getClientColor();
            event.setRed((float)clientColor.getRed());
            event.setGreen((float)clientColor.getGreen());
            event.setBlue((float)clientColor.getBlue());
        }
        else if (colorModeValue.equalsIgnoreCase("Custom")) {
            final Color customColorValue = new Color(this.customColor.getColor());
            event.setRed((float)customColorValue.getRed());
            event.setGreen((float)customColorValue.getGreen());
            event.setBlue((float)customColorValue.getBlue());
        }
    }
}
