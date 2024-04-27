/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils;

import java.awt.Color;

public final class HanaBiColors
extends Enum<HanaBiColors> {
    public static final /* enum */ HanaBiColors BLACK;
    public static final /* enum */ HanaBiColors BLUE;
    public static final /* enum */ HanaBiColors DARKBLUE;
    public static final /* enum */ HanaBiColors GREEN;
    public static final /* enum */ HanaBiColors DARKGREEN;
    public static final /* enum */ HanaBiColors WHITE;
    public static final /* enum */ HanaBiColors AQUA;
    public static final /* enum */ HanaBiColors DARKAQUA;
    public static final /* enum */ HanaBiColors GREY;
    public static final /* enum */ HanaBiColors DARKGREY;
    public static final /* enum */ HanaBiColors RED;
    public static final /* enum */ HanaBiColors DARKRED;
    public static final /* enum */ HanaBiColors ORANGE;
    public static final /* enum */ HanaBiColors DARKORANGE;
    public static final /* enum */ HanaBiColors YELLOW;
    public static final /* enum */ HanaBiColors DARKYELLOW;
    public static final /* enum */ HanaBiColors MAGENTA;
    public static final /* enum */ HanaBiColors DARKMAGENTA;
    public int c;
    private static final /* synthetic */ HanaBiColors[] $VALUES;

    public static HanaBiColors[] values() {
        return (HanaBiColors[])$VALUES.clone();
    }

    public static HanaBiColors valueOf(String name) {
        return Enum.valueOf(HanaBiColors.class, name);
    }

    private HanaBiColors(int co) {
        this.c = co;
    }

    public static int getColor(Color color) {
        return HanaBiColors.getColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static int getColor(int brightness) {
        return HanaBiColors.getColor(brightness, brightness, brightness, 255);
    }

    public static int getColor(int brightness, int alpha) {
        return HanaBiColors.getColor(brightness, brightness, brightness, alpha);
    }

    public static int getColor(int red, int green, int blue) {
        return HanaBiColors.getColor(red, green, blue, 255);
    }

    public static int getColor(int red, int green, int blue, int alpha) {
        int color = 0;
        color |= alpha << 24;
        color |= red << 16;
        color |= green << 8;
        return color |= blue;
    }

    static {
        HanaBiColors hanaBiColors;
        HanaBiColors hanaBiColors2;
        HanaBiColors hanaBiColors3;
        HanaBiColors hanaBiColors4;
        HanaBiColors hanaBiColors5;
        HanaBiColors hanaBiColors6;
        HanaBiColors hanaBiColors7;
        HanaBiColors hanaBiColors8;
        HanaBiColors hanaBiColors9;
        HanaBiColors hanaBiColors10;
        HanaBiColors hanaBiColors11;
        HanaBiColors hanaBiColors12;
        HanaBiColors hanaBiColors13;
        HanaBiColors hanaBiColors14;
        HanaBiColors hanaBiColors15;
        HanaBiColors hanaBiColors16;
        HanaBiColors hanaBiColors17;
        HanaBiColors hanaBiColors18;
        HanaBiColors hanaBiColors19 = hanaBiColors18;
        HanaBiColors hanaBiColors20 = hanaBiColors18;
        String string = "BLACK";
        BLACK = (HanaBiColors)0;
        HanaBiColors hanaBiColors21 = hanaBiColors17;
        HanaBiColors hanaBiColors22 = hanaBiColors17;
        String string2 = "BLUE";
        BLUE = (HanaBiColors)0;
        HanaBiColors hanaBiColors23 = hanaBiColors16;
        HanaBiColors hanaBiColors24 = hanaBiColors16;
        String string3 = "DARKBLUE";
        DARKBLUE = (HanaBiColors)0;
        HanaBiColors hanaBiColors25 = hanaBiColors15;
        HanaBiColors hanaBiColors26 = hanaBiColors15;
        String string4 = "GREEN";
        GREEN = (HanaBiColors)0;
        HanaBiColors hanaBiColors27 = hanaBiColors14;
        HanaBiColors hanaBiColors28 = hanaBiColors14;
        String string5 = "DARKGREEN";
        DARKGREEN = (HanaBiColors)0;
        HanaBiColors hanaBiColors29 = hanaBiColors13;
        HanaBiColors hanaBiColors30 = hanaBiColors13;
        String string6 = "WHITE";
        WHITE = (HanaBiColors)0;
        HanaBiColors hanaBiColors31 = hanaBiColors12;
        HanaBiColors hanaBiColors32 = hanaBiColors12;
        String string7 = "AQUA";
        AQUA = (HanaBiColors)0;
        HanaBiColors hanaBiColors33 = hanaBiColors11;
        HanaBiColors hanaBiColors34 = hanaBiColors11;
        String string8 = "DARKAQUA";
        DARKAQUA = (HanaBiColors)0;
        HanaBiColors hanaBiColors35 = hanaBiColors10;
        HanaBiColors hanaBiColors36 = hanaBiColors10;
        String string9 = "GREY";
        GREY = (HanaBiColors)0;
        HanaBiColors hanaBiColors37 = hanaBiColors9;
        HanaBiColors hanaBiColors38 = hanaBiColors9;
        String string10 = "DARKGREY";
        DARKGREY = (HanaBiColors)0;
        HanaBiColors hanaBiColors39 = hanaBiColors8;
        HanaBiColors hanaBiColors40 = hanaBiColors8;
        String string11 = "RED";
        RED = (HanaBiColors)0;
        HanaBiColors hanaBiColors41 = hanaBiColors7;
        HanaBiColors hanaBiColors42 = hanaBiColors7;
        String string12 = "DARKRED";
        DARKRED = (HanaBiColors)0;
        HanaBiColors hanaBiColors43 = hanaBiColors6;
        HanaBiColors hanaBiColors44 = hanaBiColors6;
        String string13 = "ORANGE";
        ORANGE = (HanaBiColors)0;
        HanaBiColors hanaBiColors45 = hanaBiColors5;
        HanaBiColors hanaBiColors46 = hanaBiColors5;
        String string14 = "DARKORANGE";
        DARKORANGE = (HanaBiColors)0;
        HanaBiColors hanaBiColors47 = hanaBiColors4;
        HanaBiColors hanaBiColors48 = hanaBiColors4;
        String string15 = "YELLOW";
        YELLOW = (HanaBiColors)0;
        HanaBiColors hanaBiColors49 = hanaBiColors3;
        HanaBiColors hanaBiColors50 = hanaBiColors3;
        String string16 = "DARKYELLOW";
        DARKYELLOW = (HanaBiColors)0;
        HanaBiColors hanaBiColors51 = hanaBiColors2;
        HanaBiColors hanaBiColors52 = hanaBiColors2;
        String string17 = "MAGENTA";
        MAGENTA = (HanaBiColors)0;
        HanaBiColors hanaBiColors53 = hanaBiColors;
        HanaBiColors hanaBiColors54 = hanaBiColors;
        String string18 = "DARKMAGENTA";
        DARKMAGENTA = (HanaBiColors)0;
        $VALUES = new HanaBiColors[]{BLACK, BLUE, DARKBLUE, GREEN, DARKGREEN, WHITE, AQUA, DARKAQUA, GREY, DARKGREY, RED, DARKRED, ORANGE, DARKORANGE, YELLOW, DARKYELLOW, MAGENTA, DARKMAGENTA};
    }
}

