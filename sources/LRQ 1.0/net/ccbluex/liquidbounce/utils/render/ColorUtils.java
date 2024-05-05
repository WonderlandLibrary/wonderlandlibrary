/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.JvmField
 *  kotlin.jvm.JvmStatic
 *  kotlin.ranges.RangesKt
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
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.Nullable;

public final class ColorUtils {
    private static final char[] allowedCharactersArray;
    private static final Pattern COLOR_PATTERN;
    @JvmField
    public static final int[] hexColors;
    private static final long startTime;
    public static final ColorUtils INSTANCE;

    public final char[] getAllowedCharactersArray() {
        return allowedCharactersArray;
    }

    public final boolean isAllowedCharacter(char character) {
        return character != '\u00a7' && character >= ' ' && character != '\u007f';
    }

    public final Color mix(Color a, Color b, float factor) {
        float f = factor;
        boolean bl = f >= 0.0f && f <= 1.0f;
        boolean bl2 = false;
        boolean bl3 = false;
        if (!bl) {
            boolean bl4 = false;
            String string = "Color factor should be between 0 and 1";
            throw (Throwable)new IllegalArgumentException(string.toString());
        }
        return new Color((int)((float)a.getRed() * factor + (float)b.getRed() * (1.0f - factor)), (int)((float)a.getGreen() * factor + (float)b.getGreen() * (1.0f - factor)), (int)((float)a.getBlue() * factor + (float)b.getBlue() * (1.0f - factor)));
    }

    public final Color fade3(Color a, Color b, double speed, double offset) {
        double time = (double)System.currentTimeMillis() * speed + offset;
        boolean bl = false;
        double factor = Math.abs(Math.sin(time));
        return this.mix(a, b, (float)factor);
    }

    public static /* synthetic */ Color fade3$default(ColorUtils colorUtils, Color color, Color color2, double d, double d2, int n, Object object) {
        if ((n & 4) != 0) {
            d = 0.001;
        }
        if ((n & 8) != 0) {
            d2 = 0.0;
        }
        return colorUtils.fade3(color, color2, d, d2);
    }

    public final int getColor(float hueoffset, float saturation, float brightness) {
        float speed = 4500.0f;
        float hue = (float)(System.currentTimeMillis() % (long)((int)speed)) / speed;
        return Color.HSBtoRGB(hue - hueoffset / (float)54, saturation, brightness);
    }

    @JvmStatic
    public static final String stripColor(@Nullable String input) {
        String string = input;
        if (string == null) {
            return null;
        }
        return COLOR_PATTERN.matcher(string).replaceAll("");
    }

    public final Color healthColor(float hp, float maxHP, int alpha) {
        int n;
        int n2;
        int pct = (int)(hp / maxHP * 255.0f);
        int n3 = 255 - pct;
        int n4 = 255;
        boolean bl = false;
        n3 = n2 = Math.min(n3, n4);
        n4 = 0;
        bl = false;
        n2 = Math.max(n3, n4);
        n3 = 255;
        n4 = 0;
        n3 = n = Math.min(pct, n3);
        n4 = 0;
        bl = false;
        n = Math.max(n3, n4);
        int n5 = alpha;
        int n6 = 0;
        int n7 = n;
        int n8 = n2;
        return new Color(n8, n7, n6, n5);
    }

    public static /* synthetic */ Color healthColor$default(ColorUtils colorUtils, float f, float f2, int n, int n2, Object object) {
        if ((n2 & 4) != 0) {
            n = 255;
        }
        return colorUtils.healthColor(f, f2, n);
    }

    public final Color rainbowWithAlpha(int alpha) {
        return this.reAlpha(ColorUtils.hslRainbow$default(1, 0.0f, 0.0f, 0, 0, 30, null), alpha);
    }

    public final Color darker(Color color, float percentage) {
        return new Color((int)((float)color.getRed() * percentage), (int)((float)color.getGreen() * percentage), (int)((float)color.getBlue() * percentage), (int)((float)color.getAlpha() * percentage));
    }

    @JvmStatic
    public static final Color hslRainbow(int index, float lowest, float bigest, int indexOffset, int timeSplit) {
        return Color.getHSBColor(Math.abs((float)((int)(System.currentTimeMillis() - startTime) + index * indexOffset) / (float)timeSplit % (float)2 - 1.0f) * (bigest - lowest) + lowest, 0.7f, 1.0f);
    }

    public static /* synthetic */ Color hslRainbow$default(int n, float f, float f2, int n2, int n3, int n4, Object object) {
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
    public static final Color fade(Color color, int index, int count) {
        float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        float brightness = Math.abs(((float)(System.currentTimeMillis() % 2000L) / 1000.0f + (float)index / (float)count * 2.0f) % 2.0f - 1.0f);
        brightness = 0.5f + 0.5f * brightness;
        hsb[2] = brightness % 2.0f;
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
    }

    /*
     * WARNING - void declaration
     */
    @JvmStatic
    public static final String translateAlternateColorCodes(String textToTranslate) {
        String string = textToTranslate;
        int n = 0;
        char[] chars = string.toCharArray();
        int n2 = 0;
        n = chars.length - 1;
        while (n2 < n) {
            void i;
            if (chars[i] == '&' && StringsKt.contains((CharSequence)"0123456789AaBbCcDdEeFfKkLlMmNnOoRr", (char)chars[i + true], (boolean)true)) {
                chars[i] = 167;
                chars[i + true] = Character.toLowerCase(chars[i + true]);
            }
            ++i;
        }
        n2 = 0;
        return new String(chars);
    }

    public final String randomMagicText(String text) {
        StringBuilder stringBuilder = new StringBuilder();
        String allowedCharacters = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000";
        String string = text;
        boolean bl = false;
        for (char c : string.toCharArray()) {
            if (!this.isAllowedCharacter(c)) continue;
            int index = new Random().nextInt(allowedCharacters.length());
            String string2 = allowedCharacters;
            StringBuilder stringBuilder2 = stringBuilder;
            boolean bl2 = false;
            char[] cArray = string2.toCharArray();
            stringBuilder2.append(cArray[index]);
        }
        return stringBuilder.toString();
    }

    @JvmStatic
    public static final Color rainbow() {
        Color currentColor = new Color(Color.HSBtoRGB((float)(System.nanoTime() + 400000L) / 1.0E10f % 1.0f, 1.0f, 1.0f));
        return new Color((float)currentColor.getRed() / 255.0f * 1.0f, (float)currentColor.getGreen() / 255.0f * 1.0f, (float)currentColor.getBlue() / 255.0f * 1.0f, (float)currentColor.getAlpha() / 255.0f);
    }

    @JvmStatic
    public static final Color rainbow(long offset) {
        Color currentColor = new Color(Color.HSBtoRGB((float)(System.nanoTime() + offset) / 1.0E10f % 1.0f, 1.0f, 1.0f));
        return new Color((float)currentColor.getRed() / 255.0f * 1.0f, (float)currentColor.getGreen() / 255.0f * 1.0f, (float)currentColor.getBlue() / 255.0f * 1.0f, (float)currentColor.getAlpha() / 255.0f);
    }

    @JvmStatic
    public static final Color rainbow(float alpha) {
        return ColorUtils.rainbow(400000L, alpha);
    }

    @JvmStatic
    public static final Color rainbow(int alpha) {
        return ColorUtils.rainbow(400000L, alpha / 255);
    }

    @JvmStatic
    public static final Color rainbow(long offset, int alpha) {
        return ColorUtils.rainbow(offset, (float)alpha / (float)255);
    }

    @JvmStatic
    public static final Color ALLColor(long offset) {
        Color currentColor = new Color(Color.HSBtoRGB((float)((double)Minecraft.func_71410_x().field_71439_g.field_70173_aa / 50.0 + Math.sin(1.6) % 1.0), 0.4f, 0.9f));
        return new Color(currentColor.getRGB());
    }

    @JvmStatic
    public static final Color originalrainbow(long offset) {
        Color currentColor = new Color(Color.HSBtoRGB((float)(System.nanoTime() + offset) / 1.0E10f % 1.0f, 1.0f, 1.0f));
        return new Color((float)currentColor.getRed() / 255.0f * 1.0f, (float)currentColor.getGreen() / 255.0f * 1.0f, (float)currentColor.getBlue() / 255.0f * 1.0f, (float)currentColor.getAlpha() / 255.0f);
    }

    public final Color reAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    @JvmStatic
    public static final Color LiquidSlowly(long time, int count, float qd, float sq) {
        Color color = new Color(Color.HSBtoRGB(((float)time + (float)count * -3000000.0f) / (float)2 / 1.0E9f, qd, sq));
        return new Color((float)color.getRed() / 255.0f * 1.0f, (float)color.getGreen() / 255.0f * 1.0f, (float)color.getBlue() / 255.0f * 1.0f, (float)color.getAlpha() / 255.0f);
    }

    @JvmStatic
    public static final Color rainbow(long offset, float alpha) {
        Color currentColor = new Color(Color.HSBtoRGB((float)(System.nanoTime() + offset) / 1.0E10f % 1.0f, 1.0f, 1.0f));
        return new Color((float)currentColor.getRed() / 255.0f * 1.0f, (float)currentColor.getGreen() / 255.0f * 1.0f, (float)currentColor.getBlue() / 255.0f * 1.0f, alpha);
    }

    @JvmStatic
    public static final Color reAlpha(Color color, float alpha) {
        return new Color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, RangesKt.coerceIn((float)alpha, (float)0.0f, (float)1.0f));
    }

    @JvmStatic
    public static final Color TwoRainbow(long offset, float alpha) {
        Color currentColor = new Color(Color.HSBtoRGB((float)(System.nanoTime() + offset) / 8.9999999E10f % 1.0f, 0.75f, 0.8f));
        return new Color((float)currentColor.getRed() / 255.0f * 1.0f, (float)currentColor.getGreen() / 255.0f * 1.0f, (float)currentColor.getBlue() / 255.0f * 1.0f, alpha);
    }

    private ColorUtils() {
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
            int i = n2;
            boolean bl2 = false;
            int baseColor = (i >> 3 & 1) * 85;
            int red = (i >> 2 & 1) * 170 + baseColor + (i == 6 ? 85 : 0);
            int green = (i >> 1 & 1) * 170 + baseColor;
            int blue = (i & 1) * 170 + baseColor;
            ColorUtils.hexColors[i] = (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
        }
        startTime = System.currentTimeMillis();
    }
}

