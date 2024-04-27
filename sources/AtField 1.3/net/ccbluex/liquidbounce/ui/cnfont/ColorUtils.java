/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.ui.cnfont;

public class ColorUtils {
    public static final int NO_COLOR;
    public static final int WHITE;
    public static final int RED;
    public static final int BLUE;
    public static final int GREED;
    public static final int BLACK;

    public static int getRed(int n) {
        return n >> 16 & 0xFF;
    }

    public static int getRGB(int n, int n2, int n3) {
        return ColorUtils.getRGB(n, n2, n3, 255);
    }

    public static int getRGB(int n) {
        return 0xFF000000 | n;
    }

    static {
        RED = ColorUtils.getRGB(255, 0, 0);
        GREED = ColorUtils.getRGB(0, 255, 0);
        BLUE = ColorUtils.getRGB(0, 0, 255);
        WHITE = ColorUtils.getRGB(255, 255, 255);
        BLACK = ColorUtils.getRGB(0, 0, 0);
        NO_COLOR = ColorUtils.getRGB(0, 0, 0, 0);
    }

    public static int getGreen(int n) {
        return n >> 8 & 0xFF;
    }

    public static int getBlue(int n) {
        return n & 0xFF;
    }

    public static int reAlpha(int n, int n2) {
        return ColorUtils.getRGB(ColorUtils.getRed(n), ColorUtils.getGreen(n), ColorUtils.getBlue(n), n2);
    }

    public static int getAlpha(int n) {
        return n >> 24 & 0xFF;
    }

    public static int[] splitRGB(int n) {
        return new int[]{n >> 16 & 0xFF, n >> 8 & 0xFF, n & 0xFF};
    }

    public static int getRGB(int n, int n2, int n3, int n4) {
        return (n4 & 0xFF) << 24 | (n & 0xFF) << 16 | (n2 & 0xFF) << 8 | n3 & 0xFF;
    }
}

