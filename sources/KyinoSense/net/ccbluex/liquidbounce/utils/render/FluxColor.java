/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.render;

import java.awt.Color;

public enum FluxColor {
    BLACK(-16711423),
    BLUE(-12028161),
    DARKBLUE(-12621684),
    GREEN(-9830551),
    DARKGREEN(-9320847),
    WHITE(-65794),
    AQUA(-7820064),
    DARKAQUA(-12621684),
    GREY(-9868951),
    DARKGREY(-14342875),
    RED(-65536),
    DARKRED(-8388608),
    ORANGE(-29696),
    DARKORANGE(-2263808),
    YELLOW(-256),
    DARKYELLOW(-2702025),
    MAGENTA(-18751),
    DARKMAGENTA(-2252579);

    public int c;

    private FluxColor(int co) {
        this.c = co;
    }

    public static int getColor(Color color) {
        return FluxColor.getColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static int getColor(int brightness) {
        return FluxColor.getColor(brightness, brightness, brightness, 255);
    }

    public static int getColor(int brightness, int alpha) {
        return FluxColor.getColor(brightness, brightness, brightness, alpha);
    }

    public static int getColor(int red, int green, int blue) {
        return FluxColor.getColor(red, green, blue, 255);
    }

    public static int getColor(int red, int green, int blue, int alpha) {
        int color = 0;
        int color1 = color | alpha << 24;
        color1 |= red << 16;
        color1 |= green << 8;
        return color1 |= blue;
    }

    public static int blendColours(int[] colours, double progress) {
        int size = colours.length;
        if (progress == 1.0) {
            return colours[0];
        }
        if (progress == 0.0) {
            return colours[size - 1];
        }
        double mulProgress = Math.max(0.0, (1.0 - progress) * (double)(size - 1));
        int index = (int)mulProgress;
        return FluxColor.fadeBetween(colours[index], colours[index + 1], mulProgress - (double)index);
    }

    public static int fadeBetween(int startColour, int endColour, double progress) {
        if (progress > 1.0) {
            progress = 1.0 - progress % 1.0;
        }
        return FluxColor.fadeTo(startColour, endColour, progress);
    }

    public static int fadeBetween(int startColour, int endColour) {
        return FluxColor.fadeBetween(startColour, endColour, 0.0);
    }

    public static int fadeTo(int startColour, int endColour, double progress) {
        double invert = 1.0 - progress;
        int r = (int)((double)(startColour >> 16 & 0xFF) * invert + (double)(endColour >> 16 & 0xFF) * progress);
        int g = (int)((double)(startColour >> 8 & 0xFF) * invert + (double)(endColour >> 8 & 0xFF) * progress);
        int b = (int)((double)(startColour & 0xFF) * invert + (double)(endColour & 0xFF) * progress);
        int a = (int)((double)(startColour >> 24 & 0xFF) * invert + (double)(endColour >> 24 & 0xFF) * progress);
        return (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
    }
}

