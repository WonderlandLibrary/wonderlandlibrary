/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.render;

import java.awt.Color;
import java.util.function.Supplier;

public enum Palette {
    GREEN(() -> new Color(0, 255, 138)),
    WHITE(() -> Color.WHITE),
    PURPLE(() -> new Color(198, 139, 255)),
    DARK_PURPLE(() -> new Color(133, 46, 215)),
    BLUE(() -> new Color(116, 202, 255));

    private final Supplier<Color> colorSupplier;

    private Palette(Supplier<Color> colorSupplier) {
        this.colorSupplier = colorSupplier;
    }

    public static Color fade(Color color) {
        return Palette.fade(color, 2, 100, 2.0f);
    }

    public static Color fade(Color color, int index, int count, float customValue) {
        float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        float brightness = Math.abs(((float)(System.currentTimeMillis() % 2000L) / 1000.0f + (float)index / (float)count * 2.0f) % customValue - 1.0f);
        brightness = 0.5f + 0.5f * brightness;
        hsb[2] = brightness % 2.0f;
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
    }

    public static Color fade1(Color color) {
        return Palette.fade1(color, 2, 100);
    }

    public static Color fade1(Color color, int index, int count) {
        float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        float brightness = Math.abs(((float)(System.currentTimeMillis() % 2000L) / 1000.0f + (float)index / (float)count * 2.0f) % 2.0f - 1.0f);
        brightness = 0.5f + 0.5f * brightness;
        hsb[2] = brightness % 2.0f;
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
    }

    public static Color fade2(Color color, int index, int count) {
        float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        float brightness = Math.abs(((float)(System.currentTimeMillis() % 10000L) / 1000.0f + (float)index / (float)count * 2.0f) % 2.0f - 1.0f);
        brightness = 0.5f + 0.5f * brightness;
        hsb[2] = brightness % 2.0f;
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
    }

    public Color getColor() {
        return this.colorSupplier.get();
    }

    public static Color fade2(int color, int indexOf, int fontHeight) {
        return null;
    }

    public static Object fade2(int color, String displayText, int fontHeight) {
        return null;
    }
}

