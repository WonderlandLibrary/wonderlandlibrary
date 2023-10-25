/*
 * Decompiled with CFR 0.152.
 */
package de.Hero.clickgui.util;

import java.awt.Color;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.module.render.ClickGUI;

public class ColorUtil {
    static ClickGUI hud = new ClickGUI();

    public static Color getClickGUIColor() {
        return new Color((int)Exodus.INSTANCE.settingsManager.getSettingByName("GuiRed").getValDouble(), (int)Exodus.INSTANCE.settingsManager.getSettingByName("GuiGreen").getValDouble(), (int)Exodus.INSTANCE.settingsManager.getSettingByName("GuiBlue").getValDouble());
    }
}

