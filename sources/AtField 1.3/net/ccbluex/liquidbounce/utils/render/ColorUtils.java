/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.JvmField
 *  kotlin.jvm.JvmStatic
 *  kotlin.text.StringsKt
 *  net.minecraft.client.Minecraft
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.utils.render;

import java.awt.Color;
import java.util.Random;
import java.util.regex.Pattern;
import kotlin.jvm.JvmField;
import kotlin.jvm.JvmStatic;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.Nullable;

public final class ColorUtils {
    private static final char[] allowedCharactersArray;
    @JvmField
    public static final int[] hexColors;
    private static final Pattern COLOR_PATTERN;
    public static final ColorUtils INSTANCE;
    private static final long startTime;

    @JvmStatic
    public static final Color ALLColor(long l) {
        Color color = new Color(Color.HSBtoRGB((float)((double)Minecraft.func_71410_x().field_71439_g.field_70173_aa / 50.0 + Math.sin(1.6) % 1.0), 0.4f, 0.9f));
        return new Color(color.getRGB());
    }

    @JvmStatic
    public static final Color rainbow(long l, int n) {
        return ColorUtils.rainbow(l, (float)n / (float)255);
    }

    @JvmStatic
    public static final Color rainbow(float f) {
        return ColorUtils.rainbow(400000L, f);
    }

    public final Color interpolateColorsBackAndForth(int n, int n2, @Nullable Color color, @Nullable Color color2, boolean bl) {
        int n3 = (int)((System.currentTimeMillis() / (long)n + (long)n2) % 360L);
        n3 = (n3 >= 180 ? 360 - n3 : n3) * 2;
        return bl ? RenderUtils.interpolateColorHue(color, color2, (float)n3 / 360.0f) : RenderUtils.interpolateColorC(color, color2, (float)n3 / 360.0f);
    }

    public final Color fade3(Color color, Color color2, double d, double d2) {
        double d3 = (double)System.currentTimeMillis() * d + d2;
        boolean bl = false;
        double d4 = Math.abs(Math.sin(d3));
        return this.mix(color, color2, (float)d4);
    }

    @JvmStatic
    public static final Color hslRainbow(int n, float f, float f2, int n2, int n3) {
        return Color.getHSBColor(Math.abs((float)((int)(System.currentTimeMillis() - startTime) + n * n2) / (float)n3 % (float)2 - 1.0f) * (f2 - f) + f, 0.7f, 1.0f);
    }

    public final int getColor(float f, float f2, float f3) {
        float f4 = 4500.0f;
        float f5 = (float)(System.currentTimeMillis() % (long)((int)f4)) / f4;
        return Color.HSBtoRGB(f5 - f / (float)54, f2, f3);
    }

    @JvmStatic
    public static final Color fade(Color color, int n, int n2) {
        float[] fArray = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), fArray);
        float f = Math.abs(((float)(System.currentTimeMillis() % 2000L) / 1000.0f + (float)n / (float)n2 * 2.0f) % 2.0f - 1.0f);
        f = 0.5f + 0.5f * f;
        fArray[2] = f % 2.0f;
        return new Color(Color.HSBtoRGB(fArray[0], fArray[1], fArray[2]));
    }

    public final Color mix(Color color, Color color2, float f) {
        float f2 = f;
        boolean bl = f2 >= 0.0f && f2 <= 1.0f;
        boolean bl2 = false;
        boolean bl3 = false;
        if (!bl) {
            boolean bl4 = false;
            String string = "Color factor should be between 0 and 1";
            throw (Throwable)new IllegalArgumentException(string.toString());
        }
        return new Color((int)((float)color.getRed() * f + (float)color2.getRed() * (1.0f - f)), (int)((float)color.getGreen() * f + (float)color2.getGreen() * (1.0f - f)), (int)((float)color.getBlue() * f + (float)color2.getBlue() * (1.0f - f)));
    }

    public final char[] getAllowedCharactersArray() {
        return allowedCharactersArray;
    }

    @JvmStatic
    public static final Color rainbow() {
        Color color = new Color(Color.HSBtoRGB((float)(System.nanoTime() + 400000L) / 1.0E10f % 1.0f, 1.0f, 1.0f));
        return new Color((float)color.getRed() / 255.0f * 1.0f, (float)color.getGreen() / 255.0f * 1.0f, (float)color.getBlue() / 255.0f * 1.0f, (float)color.getAlpha() / 255.0f);
    }

    @JvmStatic
    public static final String translateAlternateColorCodes(String string) {
        int n;
        String string2 = string;
        int n2 = 0;
        char[] cArray = string2.toCharArray();
        n2 = cArray.length - 1;
        for (n = 0; n < n2; ++n) {
            if (cArray[n] != '&' || !StringsKt.contains((CharSequence)"0123456789AaBbCcDdEeFfKkLlMmNnOoRr", (char)cArray[n + 1], (boolean)true)) continue;
            cArray[n] = 167;
            cArray[n + 1] = Character.toLowerCase(cArray[n + 1]);
        }
        n = 0;
        return new String(cArray);
    }

    @JvmStatic
    public static final Color rainbow(long l, float f) {
        Color color = new Color(Color.HSBtoRGB((float)(System.nanoTime() + l) / 1.0E10f % 1.0f, 1.0f, 1.0f));
        return new Color((float)color.getRed() / 255.0f * 1.0f, (float)color.getGreen() / 255.0f * 1.0f, (float)color.getBlue() / 255.0f * 1.0f, f);
    }

    public static Color hslRainbow$default(int n, float f, float f2, int n2, int n3, int n4, Object object) {
        if ((n4 & 2) != 0) {
            f = 0.41f;
        }
        if ((n4 & 4) != 0) {
            f2 = 0.58f;
        }
        if ((n4 & 8) != 0) {
            n2 = 300;
        }
        if ((n4 & 0x10) != 0) {
            n3 = 1500;
        }
        return ColorUtils.hslRainbow(n, f, f2, n2, n3);
    }

    @JvmStatic
    public static final Color rainbow(int n) {
        return ColorUtils.rainbow(400000L, n / 255);
    }

    private ColorUtils() {
    }

    @JvmStatic
    public static final String stripColor(@Nullable String string) {
        String string2 = string;
        if (string2 == null) {
            return null;
        }
        return COLOR_PATTERN.matcher(string2).replaceAll("");
    }

    @JvmStatic
    public static final Color mixColors(Color color, Color color2, double d) {
        double d2 = 1.0 - d;
        int n = (int)((double)color.getRed() * d + (double)color2.getRed() * d2);
        int n2 = (int)((double)color.getGreen() * d + (double)color2.getGreen() * d2);
        int n3 = (int)((double)color.getBlue() * d + (double)color2.getBlue() * d2);
        return new Color(n, n2, n3);
    }

    @JvmStatic
    public static final Color LiquidSlowly(long l, int n, float f, float f2) {
        Color color = new Color(Color.HSBtoRGB(((float)l + (float)n * -3000000.0f) / (float)2 / 1.0E9f, f, f2));
        return new Color((float)color.getRed() / 255.0f * 1.0f, (float)color.getGreen() / 255.0f * 1.0f, (float)color.getBlue() / 255.0f * 1.0f, (float)color.getAlpha() / 255.0f);
    }

    public static Color fade3$default(ColorUtils colorUtils, Color color, Color color2, double d, double d2, int n, Object object) {
        if ((n & 4) != 0) {
            d = 0.001;
        }
        if ((n & 8) != 0) {
            d2 = 0.0;
        }
        return colorUtils.fade3(color, color2, d, d2);
    }

    public static Color hsbTransition$default(ColorUtils colorUtils, float f, float f2, int n, float f3, float f4, int n2, Object object) {
        if ((n2 & 8) != 0) {
            f3 = 1.0f;
        }
        if ((n2 & 0x10) != 0) {
            f4 = 1.0f;
        }
        return colorUtils.hsbTransition(f, f2, n, f3, f4);
    }

    static {
        ColorUtils colorUtils;
        INSTANCE = colorUtils = new ColorUtils();
        allowedCharactersArray = new char[]{'/', '\n', '\r', '\t', '\u0000', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':'};
        COLOR_PATTERN = Pattern.compile("(?i)\u00a7[0-9A-FK-OR]");
        hexColors = new int[16];
        int n = 16;
        boolean bl = false;
        int n2 = 0;
        int n3 = n;
        for (n2 = 0; n2 < n3; ++n2) {
            int n4 = n2;
            boolean bl2 = false;
            int n5 = (n4 >> 3 & 1) * 85;
            int n6 = (n4 >> 2 & 1) * 170 + n5 + (n4 == 6 ? 85 : 0);
            int n7 = (n4 >> 1 & 1) * 170 + n5;
            int n8 = (n4 & 1) * 170 + n5;
            ColorUtils.hexColors[n4] = (n6 & 0xFF) << 16 | (n7 & 0xFF) << 8 | n8 & 0xFF;
        }
        startTime = System.currentTimeMillis();
    }

    @JvmStatic
    public static final Color rainbow(long l) {
        Color color = new Color(Color.HSBtoRGB((float)(System.nanoTime() + l) / 1.0E10f % 1.0f, 1.0f, 1.0f));
        return new Color((float)color.getRed() / 255.0f * 1.0f, (float)color.getGreen() / 255.0f * 1.0f, (float)color.getBlue() / 255.0f * 1.0f, (float)color.getAlpha() / 255.0f);
    }

    public final Color hsbTransition(float f, float f2, int n, float f3, float f4) {
        return Color.getHSBColor(n < 180 ? f + (f2 - f) * ((float)n / 180.0f) : f + (f2 - f) * ((float)(-(n - 360)) / 180.0f), f3, f4);
    }

    @JvmStatic
    public static final Color originalrainbow(long l) {
        Color color = new Color(Color.HSBtoRGB((float)(System.nanoTime() + l) / 1.0E10f % 1.0f, 1.0f, 1.0f));
        return new Color((float)color.getRed() / 255.0f * 1.0f, (float)color.getGreen() / 255.0f * 1.0f, (float)color.getBlue() / 255.0f * 1.0f, (float)color.getAlpha() / 255.0f);
    }

    @JvmStatic
    public static final Color TwoRainbow(long l, float f) {
        Color color = new Color(Color.HSBtoRGB((float)(System.nanoTime() + l) / 8.9999999E10f % 1.0f, 0.75f, 0.8f));
        return new Color((float)color.getRed() / 255.0f * 1.0f, (float)color.getGreen() / 255.0f * 1.0f, (float)color.getBlue() / 255.0f * 1.0f, f);
    }

    public final String randomMagicText(String string) {
        StringBuilder stringBuilder = new StringBuilder();
        String string2 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000";
        String string3 = string;
        int n = 0;
        for (char c : string3.toCharArray()) {
            if (this == c) continue;
            n = new Random().nextInt(string2.length());
            String string4 = string2;
            StringBuilder stringBuilder2 = stringBuilder;
            boolean bl = false;
            char[] cArray = string4.toCharArray();
            stringBuilder2.append(cArray[n]);
        }
        return stringBuilder.toString();
    }
}

