/*
 * Decompiled with CFR 0.152.
 */
package me.report.liquidware.utils.ui.utils;

import java.awt.Color;

public class ColorManager {
    public static int getRainbow(int speed, int offset) {
        float hue = (System.currentTimeMillis() + (long)offset) % (long)speed;
        return Color.getHSBColor(hue /= (float)speed, 0.8f, 1.0f).getRGB();
    }

    public static int rainbow(int delay) {
        double rainbowState = Math.ceil((double)(System.currentTimeMillis() + (long)delay) / 20.0);
        return Color.getHSBColor((float)((rainbowState %= 360.0) / 360.0), 0.8f, 0.7f).brighter().getRGB();
    }

    public static Color getChromaRainbow(double x, double y) {
        float v = 2000.0f;
        return new Color(Color.HSBtoRGB((float)(((double)System.currentTimeMillis() - x * 10.0 * 1.0 - y * 10.0 * 1.0) % (double)v) / v, 0.8f, 0.8f));
    }

    public static int getRainbow2(int speed, int offset) {
        float hue = (System.currentTimeMillis() + (long)offset) % (long)speed;
        return Color.getHSBColor(hue /= (float)speed, 0.8f, 0.8f).getRGB();
    }

    public static int fluxRainbow(int delay, long timeOffset, float sa) {
        double rainbowState = Math.ceil((double)((System.currentTimeMillis() + timeOffset) / 8L) + (double)delay / 20.0);
        return Color.getHSBColor((float)((rainbowState %= 360.0) / 360.0), sa, 1.0f).getRGB();
    }

    public static int as() {
        int[] counter = new int[]{0};
        int[] arrn = counter;
        arrn[0] = arrn[0] + 1;
        return ColorManager.getRainbow3(counter[0] * 20);
    }

    public static int getRainbow3(int tick) {
        double d = 0.0;
        double delay = Math.ceil((System.currentTimeMillis() + (long)(tick * 2)) / 5L);
        float rainbow = (double)((float)(d / 360.0)) < 0.5 ? -((float)(delay / 360.0)) : (float)((delay %= 360.0) / 360.0);
        return Color.getHSBColor(rainbow, 0.5f, 1.0f).getRGB();
    }

    public static int astolfoRainbow(int delay, int offset, int index) {
        double d;
        double rainbowDelay = Math.ceil(System.currentTimeMillis() + (long)(delay * index)) / (double)offset;
        return Color.getHSBColor((double)((float)(d / 360.0)) < 0.5 ? -((float)(rainbowDelay / 360.0)) : (float)((rainbowDelay %= 360.0) / 360.0), 0.5f, 1.0f).getRGB();
    }

    public static Color rainbow(long time, float count, float fade) {
        float hue = ((float)time + (1.0f + count) * 2.0E8f) / 1.0E10f % 1.0f;
        long color = Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, 1.0f, 1.0f)), 16);
        Color c = new Color((int)color);
        return new Color((float)c.getRed() / 255.0f * fade, (float)c.getGreen() / 255.0f * fade, (float)c.getBlue() / 255.0f * fade, (float)c.getAlpha() / 255.0f);
    }
}

