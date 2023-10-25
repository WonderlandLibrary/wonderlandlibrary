/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.visuals;

import java.awt.Color;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.ColorSetting;
import org.celestial.client.settings.impl.ListSetting;

public class EnchantmentColor
extends Feature {
    public static ListSetting colorMode;
    public static ColorSetting customColor;

    public EnchantmentColor() {
        super("EnchantmentColor", "\u0418\u0437\u043c\u0435\u043d\u044f\u0435\u0442 \u0446\u0432\u0435\u0442 \u0437\u0430\u0447\u0430\u0440\u043e\u0432\u0430\u043d\u0438\u0439", Type.Visuals);
        colorMode = new ListSetting("Crumbs Color", "Rainbow", () -> true, "Rainbow", "Client", "Custom");
        customColor = new ColorSetting("Custom Enchantment", new Color(0xFFFFFF).getRGB(), () -> EnchantmentColor.colorMode.currentMode.equals("Custom"));
        this.addSettings(colorMode, customColor);
    }
}

