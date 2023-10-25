/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.visuals;

import java.awt.Color;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.ColorSetting;

public class HitColor
extends Feature {
    public static ColorSetting hitColor;

    public HitColor() {
        super("HitColor", "\u0418\u0437\u043c\u0435\u043d\u044f\u0435\u0442 \u0446\u0432\u0435\u0442 \u043e\u0432\u0435\u0440\u043b\u0435\u044f \u0443\u0434\u0430\u0440\u0430", Type.Visuals);
        hitColor = new ColorSetting("Color", Color.RED.getRGB(), () -> true);
        this.addSettings(hitColor);
    }
}

