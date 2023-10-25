package org.dreamcore.client.feature.impl.visual;

import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.settings.impl.ColorSetting;
import org.dreamcore.client.settings.impl.ListSetting;

import java.awt.*;

public class EnchantmentColor extends Feature {

    public static ListSetting colorMode;
    public static ColorSetting customColor;

    public EnchantmentColor() {
        super("EnchantmentColor", "Изменяет цвет зачарований", Type.Visuals);
        colorMode = new ListSetting("Crumbs Color", "Rainbow", () -> true, "Rainbow", "Client", "Custom");
        customColor = new ColorSetting("Custom Enchantment", new Color(0xFFFFFF).getRGB(), () -> colorMode.currentMode.equals("Custom"));
        addSettings(colorMode, customColor);
    }
}
