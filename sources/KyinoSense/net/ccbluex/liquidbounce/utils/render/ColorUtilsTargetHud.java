/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.ChatAllowedCharacters
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.utils.render;

import java.awt.Color;
import java.util.Random;
import java.util.regex.Pattern;
import kotlin.Metadata;
import kotlin.jvm.JvmField;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import net.minecraft.util.ChatAllowedCharacters;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0015\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\b\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0010\u0006\n\u0002\b\u0012\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J*\u0010\b\u001a\u0004\u0018\u00010\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000fH\u0007J\u0018\u0010\u0011\u001a\u00020\t2\u0006\u0010\u0012\u001a\u00020\u000b2\u0006\u0010\u0013\u001a\u00020\u000fH\u0007J \u0010\u0014\u001a\u00020\t2\u0006\u0010\u0015\u001a\u00020\t2\u0006\u0010\u0016\u001a\u00020\r2\u0006\u0010\f\u001a\u00020\rH\u0007J\u0012\u0010\u0017\u001a\u0004\u0018\u00010\u00182\u0006\u0010\u0019\u001a\u00020\rH\u0007J\u0010\u0010\u001a\u001a\u00020\t2\u0006\u0010\u0015\u001a\u00020\tH\u0007J2\u0010\u001b\u001a\u00020\t2\u0006\u0010\u001c\u001a\u00020\u000f2\u0006\u0010\u001d\u001a\u00020\u000f2\u0006\u0010\u001e\u001a\u00020\r2\b\b\u0002\u0010\u001f\u001a\u00020\u000f2\b\b\u0002\u0010 \u001a\u00020\u000fJ%\u0010!\u001a\u0004\u0018\u00010\"2\u0006\u0010#\u001a\u00020\"2\u0006\u0010$\u001a\u00020\"2\u0006\u0010%\u001a\u00020\"\u00a2\u0006\u0002\u0010&J\"\u0010'\u001a\u0004\u0018\u00010\t2\u0006\u0010(\u001a\u00020\t2\u0006\u0010)\u001a\u00020\t2\u0006\u0010*\u001a\u00020\u000fH\u0007J \u0010+\u001a\u00020\r2\u0006\u0010#\u001a\u00020\r2\u0006\u0010$\u001a\u00020\r2\u0006\u0010%\u001a\u00020\"H\u0007J\b\u0010,\u001a\u00020\tH\u0007J\u0010\u0010,\u001a\u00020\t2\u0006\u0010\u0013\u001a\u00020\u000fH\u0007J\u0010\u0010,\u001a\u00020\t2\u0006\u0010\u0013\u001a\u00020\rH\u0007J\u0010\u0010,\u001a\u00020\t2\u0006\u0010\u0012\u001a\u00020\u000bH\u0007J\u0018\u0010,\u001a\u00020\t2\u0006\u0010\u0012\u001a\u00020\u000b2\u0006\u0010\u0013\u001a\u00020\u000fH\u0007J\u0018\u0010,\u001a\u00020\t2\u0006\u0010\u0012\u001a\u00020\u000b2\u0006\u0010\u0013\u001a\u00020\rH\u0007J\u000e\u0010-\u001a\u00020\u00182\u0006\u0010.\u001a\u00020\u0018J\u0018\u0010/\u001a\u00020\t2\u0006\u0010\u0015\u001a\u00020\t2\u0006\u0010\u0013\u001a\u00020\u000fH\u0007J\u0018\u0010/\u001a\u00020\t2\u0006\u0010\u0015\u001a\u00020\t2\u0006\u0010\u0013\u001a\u00020\rH\u0007J\u0014\u00100\u001a\u0004\u0018\u00010\u00182\b\u00101\u001a\u0004\u0018\u00010\u0018H\u0007J\u0010\u00102\u001a\u00020\u00182\u0006\u00103\u001a\u00020\u0018H\u0007R\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u00020\u00078\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00064"}, d2={"Lnet/ccbluex/liquidbounce/utils/render/ColorUtilsTargetHud;", "", "()V", "COLOR_PATTERN", "Ljava/util/regex/Pattern;", "kotlin.jvm.PlatformType", "hexColors", "", "LiquidSlowly", "Ljava/awt/Color;", "time", "", "count", "", "qd", "", "sq", "TwoRainbow", "offset", "alpha", "fade", "color", "index", "getColor", "", "n", "getOppositeColor", "hsbTransition", "from", "to", "angle", "s", "b", "interpolate", "", "oldValue", "newValue", "interpolationValue", "(DDD)Ljava/lang/Double;", "interpolateColorC", "color1", "color2", "amount", "interpolateInt", "rainbow", "randomMagicText", "text", "reAlpha", "stripColor", "input", "translateAlternateColorCodes", "textToTranslate", "KyinoClient"})
public final class ColorUtilsTargetHud {
    private static final Pattern COLOR_PATTERN;
    @JvmField
    @NotNull
    public static final int[] hexColors;
    public static final ColorUtilsTargetHud INSTANCE;

    @JvmStatic
    @Nullable
    public static final String stripColor(@Nullable String input) {
        String string = input;
        if (string == null) {
            return null;
        }
        return COLOR_PATTERN.matcher(string).replaceAll("");
    }

    /*
     * WARNING - void declaration
     */
    @JvmStatic
    @NotNull
    public static final String translateAlternateColorCodes(@NotNull String textToTranslate) {
        Intrinsics.checkParameterIsNotNull(textToTranslate, "textToTranslate");
        String string = textToTranslate;
        int n = 0;
        char[] cArray = string.toCharArray();
        Intrinsics.checkExpressionValueIsNotNull(cArray, "(this as java.lang.String).toCharArray()");
        char[] chars = cArray;
        int n2 = 0;
        n = chars.length - 1;
        while (n2 < n) {
            void i;
            if (chars[i] == '&' && StringsKt.contains((CharSequence)"0123456789AaBbCcDdEeFfKkLlMmNnOoRr", chars[i + true], true)) {
                chars[i] = 167;
                chars[i + true] = Character.toLowerCase(chars[i + true]);
            }
            ++i;
        }
        n2 = 0;
        return new String(chars);
    }

    @Nullable
    public final Double interpolate(double oldValue, double newValue, double interpolationValue) {
        return oldValue + (newValue - oldValue) * interpolationValue;
    }

    @JvmStatic
    public static final int interpolateInt(int oldValue, int newValue, double interpolationValue) {
        Double d = INSTANCE.interpolate(oldValue, newValue, (float)interpolationValue);
        if (d == null) {
            Intrinsics.throwNpe();
        }
        return (int)d.doubleValue();
    }

    @JvmStatic
    @Nullable
    public static final String getColor(int n) {
        if (n != 1) {
            if (n == 2) {
                return "\u00a7a";
            }
            if (n == 3) {
                return "\u00a73";
            }
            if (n == 4) {
                return "\u00a74";
            }
            if (n >= 5) {
                return "\u00a7e";
            }
        }
        return "\u00a7f";
    }

    @JvmStatic
    @Nullable
    public static final Color interpolateColorC(@NotNull Color color1, @NotNull Color color2, float amount) {
        Intrinsics.checkParameterIsNotNull(color1, "color1");
        Intrinsics.checkParameterIsNotNull(color2, "color2");
        float amount2 = amount;
        amount2 = Math.min(1.0f, Math.max(0.0f, amount2));
        return new Color(ColorUtilsTargetHud.interpolateInt(color1.getRed(), color2.getRed(), amount2), ColorUtilsTargetHud.interpolateInt(color1.getGreen(), color2.getGreen(), amount2), ColorUtilsTargetHud.interpolateInt(color1.getBlue(), color2.getBlue(), amount2), ColorUtilsTargetHud.interpolateInt(color1.getAlpha(), color2.getAlpha(), amount2));
    }

    @NotNull
    public final Color hsbTransition(float from, float to, int angle, float s, float b) {
        Color color = Color.getHSBColor(angle < 180 ? from + (to - from) * ((float)angle / 180.0f) : from + (to - from) * ((float)(-(angle - 360)) / 180.0f), s, b);
        Intrinsics.checkExpressionValueIsNotNull(color, "Color.getHSBColor(\n     \u2026gle - 360) / 180f), s, b)");
        return color;
    }

    public static /* synthetic */ Color hsbTransition$default(ColorUtilsTargetHud colorUtilsTargetHud, float f, float f2, int n, float f3, float f4, int n2, Object object) {
        if ((n2 & 8) != 0) {
            f3 = 1.0f;
        }
        if ((n2 & 0x10) != 0) {
            f4 = 1.0f;
        }
        return colorUtilsTargetHud.hsbTransition(f, f2, n, f3, f4);
    }

    @NotNull
    public final String randomMagicText(@NotNull String text) {
        Intrinsics.checkParameterIsNotNull(text, "text");
        StringBuilder stringBuilder = new StringBuilder();
        String allowedCharacters = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000";
        String string = text;
        boolean bl = false;
        char[] cArray = string.toCharArray();
        Intrinsics.checkExpressionValueIsNotNull(cArray, "(this as java.lang.String).toCharArray()");
        for (char c : cArray) {
            char[] cArray2;
            if (!ChatAllowedCharacters.func_71566_a((char)c)) continue;
            int index = new Random().nextInt(allowedCharacters.length());
            String string2 = allowedCharacters;
            StringBuilder stringBuilder2 = stringBuilder;
            boolean bl2 = false;
            Intrinsics.checkExpressionValueIsNotNull(string2.toCharArray(), "(this as java.lang.String).toCharArray()");
            stringBuilder2.append(cArray2[index]);
        }
        String string3 = stringBuilder.toString();
        Intrinsics.checkExpressionValueIsNotNull(string3, "stringBuilder.toString()");
        return string3;
    }

    @JvmStatic
    @NotNull
    public static final Color rainbow() {
        Color currentColor = new Color(Color.HSBtoRGB((float)(System.nanoTime() + 400000L) / 1.0E10f % 1.0f, 1.0f, 1.0f));
        return new Color((float)currentColor.getRed() / 255.0f * 1.0f, (float)currentColor.getGreen() / 255.0f * 1.0f, (float)currentColor.getBlue() / 255.0f * 1.0f, (float)currentColor.getAlpha() / 255.0f);
    }

    @JvmStatic
    @NotNull
    public static final Color rainbow(long offset) {
        Color currentColor = new Color(Color.HSBtoRGB((float)(System.nanoTime() + offset) / 1.0E10f % 1.0f, 1.0f, 1.0f));
        return new Color((float)currentColor.getRed() / 255.0f * 1.0f, (float)currentColor.getGreen() / 255.0f * 1.0f, (float)currentColor.getBlue() / 255.0f * 1.0f, (float)currentColor.getAlpha() / 255.0f);
    }

    @JvmStatic
    @NotNull
    public static final Color rainbow(float alpha) {
        return ColorUtilsTargetHud.rainbow(400000L, alpha);
    }

    @JvmStatic
    @NotNull
    public static final Color rainbow(int alpha) {
        return ColorUtilsTargetHud.rainbow(400000L, alpha / 255);
    }

    @JvmStatic
    @NotNull
    public static final Color rainbow(long offset, int alpha) {
        return ColorUtilsTargetHud.rainbow(offset, (float)alpha / (float)255);
    }

    @JvmStatic
    @NotNull
    public static final Color rainbow(long offset, float alpha) {
        Color currentColor = new Color(Color.HSBtoRGB((float)(System.nanoTime() + offset) / 1.0E10f % 1.0f, 1.0f, 1.0f));
        return new Color((float)currentColor.getRed() / 255.0f * 1.0f, (float)currentColor.getGreen() / 255.0f * 1.0f, (float)currentColor.getBlue() / 255.0f * 1.0f, alpha);
    }

    @JvmStatic
    @Nullable
    public static final Color LiquidSlowly(long time, int count, float qd, float sq) {
        Color color = new Color(Color.HSBtoRGB(((float)time + (float)count * 3000000.0f) / (float)2 / 1.0E9f, qd, sq));
        return new Color((float)color.getRed() / 255.0f * 1.0f, (float)color.getGreen() / 255.0f * 1.0f, (float)color.getBlue() / 255.0f * 1.0f, (float)color.getAlpha() / 255.0f);
    }

    @JvmStatic
    @NotNull
    public static final Color TwoRainbow(long offset, float alpha) {
        Color currentColor = new Color(Color.HSBtoRGB((float)(System.nanoTime() + offset) / 9.0E10f % 1.0f, 0.75f, 0.8f));
        return new Color((float)currentColor.getRed() / 255.0f * 1.0f, (float)currentColor.getGreen() / 255.0f * 1.0f, (float)currentColor.getBlue() / 255.0f * 1.0f, alpha);
    }

    @JvmStatic
    @NotNull
    public static final Color fade(@NotNull Color color, int index, int count) {
        Intrinsics.checkParameterIsNotNull(color, "color");
        float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        float f = ((float)(System.currentTimeMillis() % 2000L) / 1000.0f + (float)index / (float)count * 2.0f) % 2.0f - 1.0f;
        boolean bl = false;
        float brightness = Math.abs(f);
        brightness = 0.5f + 0.5f * brightness;
        hsb[2] = brightness % 2.0f;
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
    }

    @JvmStatic
    @NotNull
    public static final Color reAlpha(@NotNull Color color, int alpha) {
        Intrinsics.checkParameterIsNotNull(color, "color");
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), RangesKt.coerceIn(alpha, 0, 255));
    }

    @JvmStatic
    @NotNull
    public static final Color reAlpha(@NotNull Color color, float alpha) {
        Intrinsics.checkParameterIsNotNull(color, "color");
        return new Color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, RangesKt.coerceIn(alpha, 0.0f, 1.0f));
    }

    @JvmStatic
    @NotNull
    public static final Color getOppositeColor(@NotNull Color color) {
        Intrinsics.checkParameterIsNotNull(color, "color");
        return new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue(), color.getAlpha());
    }

    private ColorUtilsTargetHud() {
    }

    static {
        ColorUtilsTargetHud colorUtilsTargetHud;
        INSTANCE = colorUtilsTargetHud = new ColorUtilsTargetHud();
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
            ColorUtilsTargetHud.hexColors[i] = (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
        }
    }
}

