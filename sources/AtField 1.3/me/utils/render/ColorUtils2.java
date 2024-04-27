/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.JvmField
 *  kotlin.jvm.JvmStatic
 *  kotlin.text.StringsKt
 *  net.minecraft.util.ChatAllowedCharacters
 */
package me.utils.render;

import java.awt.Color;
import java.util.Random;
import java.util.regex.Pattern;
import kotlin.jvm.JvmField;
import kotlin.jvm.JvmStatic;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.features.module.modules.color.Rainbow;
import net.minecraft.util.ChatAllowedCharacters;

public final class ColorUtils2 {
    @JvmField
    public static final int[] hexColors;
    private static final long startTime;
    private static final Pattern COLOR_PATTERN;
    public static final ColorUtils2 INSTANCE;

    public final int getRainbow(int n, int n2) {
        float f = (System.currentTimeMillis() + (long)n2) % (long)n;
        float f2 = n;
        boolean bl = false;
        boolean bl2 = false;
        float f3 = f2;
        boolean bl3 = false;
        return Color.getHSBColor(f /= f3, 0.4f, 1.0f).getRGB();
    }

    @JvmStatic
    public static final int white(int n) {
        return new Color(255, 255, 255, n).getRGB();
    }

    public static Color colorCode$default(String string, int n, int n2, Object object) {
        if ((n2 & 2) != 0) {
            n = 255;
        }
        return ColorUtils2.colorCode(string, n);
    }

    @JvmStatic
    public static final Color colorCode(String string, int n) {
        String string2 = string;
        boolean bl = false;
        switch (string2.toLowerCase()) {
            case "0": {
                return new Color(0, 0, 0, n);
            }
            case "1": {
                return new Color(0, 0, 170, n);
            }
            case "2": {
                return new Color(0, 170, 0, n);
            }
            case "3": {
                return new Color(0, 170, 170, n);
            }
            case "4": {
                return new Color(170, 0, 0, n);
            }
            case "5": {
                return new Color(170, 0, 170, n);
            }
            case "6": {
                return new Color(255, 170, 0, n);
            }
            case "7": {
                return new Color(170, 170, 170, n);
            }
            case "8": {
                return new Color(85, 85, 85, n);
            }
            case "9": {
                return new Color(85, 85, 255, n);
            }
            case "a": {
                return new Color(85, 255, 85, n);
            }
            case "b": {
                return new Color(85, 255, 255, n);
            }
            case "c": {
                return new Color(255, 85, 85, n);
            }
            case "d": {
                return new Color(255, 85, 255, n);
            }
            case "e": {
                return new Color(255, 255, 85, n);
            }
        }
        return new Color(255, 255, 255, n);
    }

    public final String randomMagicText(String string) {
        StringBuilder stringBuilder = new StringBuilder();
        String string2 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000";
        String string3 = string;
        int n = 0;
        for (char c : string3.toCharArray()) {
            if (!ChatAllowedCharacters.func_71566_a((char)c)) continue;
            n = new Random().nextInt(string2.length());
            String string4 = string2;
            StringBuilder stringBuilder2 = stringBuilder;
            boolean bl = false;
            char[] cArray = string4.toCharArray();
            stringBuilder2.append(cArray[n]);
        }
        return stringBuilder.toString();
    }

    @JvmStatic
    public static final Color darker(Color color, float f) {
        return new Color((int)((float)color.getRed() * f), (int)((float)color.getGreen() * f), (int)((float)color.getBlue() * f), (int)((float)color.getAlpha() * f));
    }

    @JvmStatic
    public static final Color fade(Color color, int n, int n2) {
        float[] fArray = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), fArray);
        float f = ((float)(System.currentTimeMillis() % 2000L) / 1000.0f + (float)n / (float)n2 * 2.0f) % 2.0f - 1.0f;
        boolean bl = false;
        float f2 = Math.abs(f);
        f2 = 0.5f + 0.5f * f2;
        fArray[2] = f2 % 2.0f;
        return new Color(Color.HSBtoRGB(fArray[0], fArray[1], fArray[2]));
    }

    @JvmStatic
    public static final Color healthColor(float f, float f2, int n) {
        int n2;
        int n3;
        int n4 = (int)(f / f2 * 255.0f);
        int n5 = 255 - n4;
        int n6 = 255;
        boolean bl = false;
        n5 = n3 = Math.min(n5, n6);
        n6 = 0;
        bl = false;
        n3 = Math.max(n5, n6);
        n5 = 255;
        n6 = 0;
        n5 = n2 = Math.min(n4, n5);
        n6 = 0;
        bl = false;
        n2 = Math.max(n5, n6);
        int n7 = n;
        int n8 = 0;
        int n9 = n2;
        int n10 = n3;
        return new Color(n10, n9, n8, n7);
    }

    @JvmStatic
    public static final int black(int n) {
        return new Color(0, 0, 0, n).getRGB();
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
    public static final Color reAlpha(Color color, float f) {
        return new Color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, f);
    }

    @JvmStatic
    public static final Color reAlpha(Color color, int n) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), n);
    }

    @JvmStatic
    public static final Color rainbow() {
        return ColorUtils2.hslRainbow$default(1, 0.0f, 0.0f, 0, 0, 0.0f, 0.0f, 126, null);
    }

    public static Color hslRainbow$default(int n, float f, float f2, int n2, int n3, float f3, float f4, int n4, Object object) {
        if ((n4 & 2) != 0) {
            f = ((Number)Rainbow.rainbowStart.get()).floatValue();
        }
        if ((n4 & 4) != 0) {
            f2 = ((Number)Rainbow.rainbowStop.get()).floatValue();
        }
        if ((n4 & 8) != 0) {
            n2 = 300;
        }
        if ((n4 & 0x10) != 0) {
            n3 = ((Number)Rainbow.rainbowSpeed.get()).intValue();
        }
        if ((n4 & 0x20) != 0) {
            f3 = ((Number)Rainbow.rainbowSaturation.get()).floatValue();
        }
        if ((n4 & 0x40) != 0) {
            f4 = ((Number)Rainbow.rainbowBrightness.get()).floatValue();
        }
        return ColorUtils2.hslRainbow(n, f, f2, n2, n3, f3, f4);
    }

    @JvmStatic
    public static final Color twoRainbow(long l, float f) {
        Color color = new Color(Color.HSBtoRGB((float)(System.nanoTime() + l) / 8.9999999E10f % 1.0f, 0.75f, 0.8f));
        return new Color((float)color.getRed() / 255.0f * 1.0f, (float)color.getGreen() / 255.0f * 1.0f, (float)color.getBlue() / 255.0f * 1.0f, f);
    }

    @JvmStatic
    public static final Color slowlyRainbow(long l, int n, float f, float f2) {
        Color color = new Color(Color.HSBtoRGB(((float)l + (float)n * -3000000.0f) / (float)2 / 1.0E9f, f, f2));
        return new Color((float)color.getRed() / 255.0f * 1.0f, (float)color.getGreen() / 255.0f * 1.0f, (float)color.getBlue() / 255.0f * 1.0f, (float)color.getAlpha() / 255.0f);
    }

    public static Color healthColor$default(float f, float f2, int n, int n2, Object object) {
        if ((n2 & 4) != 0) {
            n = 255;
        }
        return ColorUtils2.healthColor(f, f2, n);
    }

    @JvmStatic
    public static final Color rainbowWithAlpha(int n) {
        return ColorUtils2.reAlpha(ColorUtils2.hslRainbow$default(1, 0.0f, 0.0f, 0, 0, 0.0f, 0.0f, 126, null), n);
    }

    @JvmStatic
    public static final Color skyRainbow(int n, float f, float f2, double d) {
        double d2 = (double)System.currentTimeMillis() / d + (double)((long)n * 109L);
        boolean bl = false;
        double d3 = Math.ceil(d2);
        double d4 = d3 / (double)5;
        d2 = 360.0;
        boolean bl2 = false;
        bl = false;
        double d5 = d2;
        boolean bl3 = false;
        return Color.getHSBColor(d2 / 360.0 < 0.5 ? -((float)(d4 / 360.0)) : (float)((d4 %= d5) / 360.0), f2, f);
    }

    @JvmStatic
    public static final Color hslRainbow(int n, float f, float f2, int n2, int n3, float f3, float f4) {
        float f5 = (float)((int)(System.currentTimeMillis() - startTime) + n * n2) / (float)n3 % (float)2 - 1.0f;
        boolean bl = false;
        return Color.getHSBColor(Math.abs(f5) * (f2 - f) + f, f3, f4);
    }

    static {
        ColorUtils2 colorUtils2;
        INSTANCE = colorUtils2 = new ColorUtils2();
        COLOR_PATTERN = Pattern.compile("(?i)\u00a7[0-9A-FK-OR]");
        startTime = System.currentTimeMillis();
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
            ColorUtils2.hexColors[n4] = (n6 & 0xFF) << 16 | (n7 & 0xFF) << 8 | n8 & 0xFF;
        }
    }

    @JvmStatic
    public static final Color rainbow(int n, float f) {
        return ColorUtils2.reAlpha(ColorUtils2.hslRainbow$default(n, 0.0f, 0.0f, 0, 0, 0.0f, 0.0f, 126, null), f);
    }

    @JvmStatic
    public static final Color skyRainbow2(int n, float f, float f2) {
        double d = System.currentTimeMillis() + (long)(n * 109);
        boolean bl = false;
        double d2 = Math.ceil(d);
        double d3 = d2 / (double)5;
        d = 360.0;
        boolean bl2 = false;
        bl = false;
        double d4 = d;
        boolean bl3 = false;
        return Color.getHSBColor((double)((float)(d / 360.0)) < 0.5 ? -((float)(d3 / 360.0)) : (float)((d3 %= d4) / 360.0), f, f2);
    }

    @JvmStatic
    public static final String stripColor(String string) {
        return COLOR_PATTERN.matcher(string).replaceAll("");
    }

    @JvmStatic
    public static final Color rainbow(int n, int n2) {
        return ColorUtils2.reAlpha(ColorUtils2.hslRainbow$default(n, 0.0f, 0.0f, 0, 0, 0.0f, 0.0f, 126, null), n2);
    }

    @JvmStatic
    public static final int normalRainbow(int n, float f, float f2) {
        double d = (double)(System.currentTimeMillis() + (long)n) / 20.0;
        boolean bl = false;
        double d2 = Math.ceil(d);
        return Color.getHSBColor((float)((d2 %= 360.0) / (double)360.0f), f, f2).getRGB();
    }

    public final Color mixColors(Color color, Color color2, float f) {
        return new Color(color.getRed() + (int)((float)(color2.getRed() - color.getRed()) * f), color.getGreen() + (int)((float)(color2.getGreen() - color.getGreen()) * f), color.getBlue() + (int)((float)(color2.getBlue() - color.getBlue()) * f), color.getAlpha() + (int)((float)(color2.getAlpha() - color.getAlpha()) * f));
    }

    @JvmStatic
    public static final Color reverseColor(Color color) {
        return new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue(), color.getAlpha());
    }

    @JvmStatic
    public static final Color rainbow(float f) {
        return ColorUtils2.reAlpha(ColorUtils2.hslRainbow$default(1, 0.0f, 0.0f, 0, 0, 0.0f, 0.0f, 126, null), f);
    }

    @JvmStatic
    public static final Color rainbow(int n) {
        return ColorUtils2.hslRainbow$default(n, 0.0f, 0.0f, 0, 0, 0.0f, 0.0f, 126, null);
    }

    private ColorUtils2() {
    }
}

