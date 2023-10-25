/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.visuals;

import java.awt.Color;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.ColorSetting;

public class Weather
extends Feature {
    public static ColorSetting snowColor;

    public Weather() {
        super("Weather", "\u0412\u044b\u0437\u044b\u0432\u0430\u0435\u0442 \u0441\u043d\u0435\u0436\u043d\u0443\u044e \u043f\u043e\u0433\u043e\u0434\u0443", Type.Visuals);
        snowColor = new ColorSetting("Snow Color", Color.WHITE.getRGB(), () -> true);
        this.addSettings(snowColor);
    }
}

