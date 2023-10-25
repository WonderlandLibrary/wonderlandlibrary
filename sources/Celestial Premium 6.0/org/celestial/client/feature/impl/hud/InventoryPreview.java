/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.hud;

import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class InventoryPreview
extends Feature {
    public static BooleanSetting glow;
    public static BooleanSetting shadow;
    public static NumberSetting shadowRadius;

    public InventoryPreview() {
        super("InventoryPreview", "\u041e\u0442\u043e\u0431\u0440\u043e\u0436\u0430\u0435\u0442 \u0438\u043d\u0432\u0435\u043d\u0442\u0430\u0440\u044c \u043d\u0435 \u0437\u0430\u0445\u043e\u0434\u044f \u0432 \u043d\u0435\u0433\u043e", Type.Hud);
        glow = new BooleanSetting("Glow", true, () -> true);
        shadow = new BooleanSetting("Shadow", false, () -> true);
        shadowRadius = new NumberSetting("Shadow Radius", 50.0f, 25.0f, 100.0f, 5.0f, () -> shadow.getCurrentValue());
        this.addSettings(glow, shadow, shadowRadius);
    }
}

