/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.visuals;

import java.awt.Color;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.ColorSetting;
import org.celestial.client.settings.impl.ListSetting;

public class WorldColor
extends Feature {
    public static ListSetting worldColorMode = new ListSetting("LightMap Mode", "Custom", () -> true, "Astolfo", "Rainbow", "Client", "Custom");
    public static ColorSetting lightMapColor;

    public WorldColor() {
        super("WorldColor", "\u0418\u0437\u043c\u0435\u043d\u044f\u0435\u0442 \u0446\u0432\u0435\u0442 \u0432\u043e\u043a\u0440\u0443\u0433", Type.Visuals);
        lightMapColor = new ColorSetting("LightMap Color", Color.WHITE.getRGB(), () -> WorldColor.worldColorMode.currentMode.equals("Custom"));
        this.addSettings(worldColorMode, lightMapColor);
    }
}

