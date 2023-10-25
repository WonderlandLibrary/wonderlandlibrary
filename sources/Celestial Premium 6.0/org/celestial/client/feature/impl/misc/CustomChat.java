/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.misc;

import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.ListSetting;

public class CustomChat
extends Feature {
    public static BooleanSetting noBackground = new BooleanSetting("No Background", false, () -> true);
    public static BooleanSetting customFont = new BooleanSetting("Custom Font", true, () -> true);
    public static ListSetting fontType = new ListSetting("Font Type", "RobotoRegular", () -> customFont.getCurrentValue(), "Comfortaa", "SF UI", "Luxora", "Calibri", "Verdana", "RobotoRegular", "LucidaConsole", "Lato", "RaleWay", "Product Sans", "Open Sans", "Kollektif", "Ubuntu", "Bebas Book");
    public static BooleanSetting infinityLengths = new BooleanSetting("Infinity Lengths", false, () -> true);

    public CustomChat() {
        super("CustomChat", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0432\u0430\u043c \u043d\u0430\u0441\u0442\u0440\u043e\u0438\u0442\u044c \u0447\u0430\u0442 \u043f\u043e\u0434 \u0441\u0435\u0431\u044f", Type.Misc);
        this.addSettings(customFont, fontType, noBackground, infinityLengths);
    }
}

