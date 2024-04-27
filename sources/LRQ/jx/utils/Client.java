/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package jx.utils;

import java.awt.Color;
import jx.utils.ScaleUtils;
import jx.utils.novoline.api.FontManager;
import jx.utils.novoline.impl.SimpleFontManager;
import net.minecraft.client.Minecraft;

public class Client {
    public static double fontScaleOffset = 1.0;
    public static FontManager fontManager = SimpleFontManager.create();
    public static String name = "MoralWin";
    public static String version = "221228";
    public static int THEME_RGB_COLOR = new Color(36, 240, 0).getRGB();
    public static Client instance = new Client();
    public static ScaleUtils scaleUtils = new ScaleUtils(2);

    public static FontManager getFontManager() {
        return fontManager;
    }

    public static double deltaTime() {
        return Minecraft.func_175610_ah() > 0 ? 1.0 / (double)Minecraft.func_175610_ah() : 1.0;
    }
}

