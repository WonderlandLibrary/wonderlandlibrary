/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.ui;

import java.awt.Color;

public class realpha {
    public static int rainbow(int n) {
        return Color.getHSBColor((float)(Math.ceil((double)(System.currentTimeMillis() + (long)n) / 10.0) % 360.0 / 360.0), 0.5f, 1.0f).getRGB();
    }

    public static int reAlpha(int n, float n2) {
        Color color = new Color(n);
        return new Color(0.003921569f * (float)color.getRed(), 0.003921569f * (float)color.getGreen(), 0.003921569f * (float)color.getBlue(), n2).getRGB();
    }
}

