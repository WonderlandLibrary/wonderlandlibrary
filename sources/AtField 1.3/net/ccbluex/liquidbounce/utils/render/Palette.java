/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.render;

import java.awt.Color;
import java.util.function.Supplier;

public enum Palette {
    GREEN(Palette::lambda$static$0),
    WHITE(Palette::lambda$static$1),
    PURPLE(Palette::lambda$static$2),
    DARK_PURPLE(Palette::lambda$static$3),
    BLUE(Palette::lambda$static$4);

    private final Supplier colorSupplier;

    public static Object fade2(int n, String string, int n2) {
        return null;
    }

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    private Palette() {
        void var3_1;
        this.colorSupplier = var3_1;
    }

    private static Color lambda$static$4() {
        return new Color(116, 202, 255);
    }

    private static Color lambda$static$3() {
        return new Color(133, 46, 215);
    }

    private static Color lambda$static$1() {
        return Color.WHITE;
    }

    public static Color fade1(Color color) {
        return Palette.fade1(color, 2, 100);
    }

    public static Color fade2(Color color, int n, int n2) {
        float[] fArray = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), fArray);
        float f = Math.abs(((float)(System.currentTimeMillis() % 10000L) / 1000.0f + (float)n / (float)n2 * 2.0f) % 2.0f - 1.0f);
        f = 0.5f + 0.5f * f;
        fArray[2] = f % 2.0f;
        return new Color(Color.HSBtoRGB(fArray[0], fArray[1], fArray[2]));
    }

    public static Color fade1(Color color, int n, int n2) {
        float[] fArray = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), fArray);
        float f = Math.abs(((float)(System.currentTimeMillis() % 2000L) / 1000.0f + (float)n / (float)n2 * 2.0f) % 2.0f - 1.0f);
        f = 0.5f + 0.5f * f;
        fArray[2] = f % 2.0f;
        return new Color(Color.HSBtoRGB(fArray[0], fArray[1], fArray[2]));
    }

    public static Color fade(Color color) {
        return Palette.fade(color, 2, 100, 2.0f);
    }

    public static Color fade(Color color, int n, int n2, float f) {
        float[] fArray = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), fArray);
        float f2 = Math.abs(((float)(System.currentTimeMillis() % 2000L) / 1000.0f + (float)n / (float)n2 * 2.0f) % f - 1.0f);
        f2 = 0.5f + 0.5f * f2;
        fArray[2] = f2 % 2.0f;
        return new Color(Color.HSBtoRGB(fArray[0], fArray[1], fArray[2]));
    }

    private static Color lambda$static$2() {
        return new Color(198, 139, 255);
    }

    public static Color fade2(int n, int n2, int n3) {
        return null;
    }

    private static Color lambda$static$0() {
        return new Color(0, 255, 138);
    }

    public Color getColor() {
        return (Color)this.colorSupplier.get();
    }
}

