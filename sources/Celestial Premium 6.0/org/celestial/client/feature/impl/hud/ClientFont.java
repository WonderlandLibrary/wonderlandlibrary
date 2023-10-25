/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.hud;

import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.ListSetting;

public class ClientFont
extends Feature {
    public static BooleanSetting minecraftFont;
    public static ListSetting font;

    public ClientFont() {
        super("Client Font", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0432\u044b\u0431\u0440\u0430\u0442\u044c \u043d\u0430\u0441\u0442\u0440\u043e\u0439\u043a\u0443 \u0448\u0440\u0438\u0444\u0442\u0430 \u0443 \u0447\u0438\u0442\u0430", Type.Hud);
        font = new ListSetting("Font Type", "RobotoRegular", () -> !minecraftFont.getCurrentValue(), "RobotoRegular", "SF UI", "Luxora", "Calibri", "Verdana", "Comfortaa", "LucidaConsole", "Lato", "RaleWay", "Product Sans", "Open Sans", "Kollektif", "Ubuntu", "Bebas Book");
        minecraftFont = new BooleanSetting("Minecraft Font", false, () -> true);
        this.addSettings(minecraftFont, font);
    }

    @Override
    public void onEnable() {
        this.toggle();
        super.onEnable();
    }
}

