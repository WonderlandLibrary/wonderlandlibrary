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
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;
import kotlin.Metadata;
import kotlin.jvm.JvmField;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.CharsKt;
import kotlin.text.StringsKt;
import net.minecraft.util.ChatAllowedCharacters;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000V\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0015\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\u0014\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0010\n\u0002\u0010\u000e\n\u0002\b\t\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\u000eJ)\u0010\u000f\u001a\u00020\n2\u0006\u0010\u0010\u001a\u00020\u00112\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\n0\u00132\u0006\u0010\u0014\u001a\u00020\u0015\u00a2\u0006\u0002\u0010\u0016J\u0010\u0010\u0017\u001a\u00020\n2\u0006\u0010\u0018\u001a\u00020\u0019H\u0007J\u0016\u0010\u001a\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\u0015J\u001a\u0010\u001b\u001a\u0004\u0018\u00010\n2\u0006\u0010\u001c\u001a\u00020\u00152\u0006\u0010\u001d\u001a\u00020\u0015H\u0007J\u0018\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u001f2\u0006\u0010\u0018\u001a\u00020\u001fH\u0007J\u0010\u0010!\u001a\u00020\n2\u0006\u0010\u0018\u001a\u00020\u0019H\u0007J \u0010\"\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u00152\u0006\u0010\u0018\u001a\u00020\u00152\u0006\u0010#\u001a\u00020\u0015H\u0007J\u0018\u0010$\u001a\u00020\u001f2\u0006\u0010\u0018\u001a\u00020\u00152\u0006\u0010%\u001a\u00020\u0015H\u0007J\b\u0010&\u001a\u00020\nH\u0007J\u0010\u0010&\u001a\u00020\n2\u0006\u0010'\u001a\u00020\u0015H\u0007J\u0010\u0010&\u001a\u00020\n2\u0006\u0010'\u001a\u00020\u001fH\u0007J\u0010\u0010&\u001a\u00020\n2\u0006\u0010\u0018\u001a\u00020\u0019H\u0007J \u0010&\u001a\u00020\n2\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010'\u001a\u00020\u00152\u0006\u0010(\u001a\u00020\u0015H\u0007J \u0010&\u001a\u00020\n2\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010'\u001a\u00020\u001f2\u0006\u0010(\u001a\u00020\u0015H\u0007J\u0018\u0010)\u001a\u0004\u0018\u00010\n2\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010*\u001a\u00020\u0015J\b\u0010+\u001a\u00020\nH\u0007J\u0010\u0010+\u001a\u00020\n2\u0006\u0010'\u001a\u00020\u0015H\u0007J\u0010\u0010+\u001a\u00020\n2\u0006\u0010'\u001a\u00020\u001fH\u0007J\u0010\u0010+\u001a\u00020\n2\u0006\u0010\u0018\u001a\u00020\u0019H\u0007J \u0010+\u001a\u00020\n2\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010'\u001a\u00020\u00152\u0006\u0010(\u001a\u00020\u0015H\u0007J \u0010+\u001a\u00020\n2\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010'\u001a\u00020\u001f2\u0006\u0010(\u001a\u00020\u0015H\u0007J \u0010,\u001a\u00020\n2\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010%\u001a\u00020\u00152\u0006\u0010-\u001a\u00020\u0015H\u0007J\u0010\u0010.\u001a\u00020\n2\u0006\u0010\u0018\u001a\u00020\u0019H\u0007J\u000e\u0010/\u001a\u0002002\u0006\u00101\u001a\u000200J\u0018\u00102\u001a\u00020\n2\u0006\u00103\u001a\u00020\n2\u0006\u0010'\u001a\u00020\u001fH\u0007J\u0010\u00104\u001a\u00020\n2\u0006\u0010\u0018\u001a\u00020\u0019H\u0007J\u0014\u00105\u001a\u0004\u0018\u0001002\b\u00106\u001a\u0004\u0018\u000100H\u0007J\u0010\u00107\u001a\u0002002\u0006\u00108\u001a\u000200H\u0007R\u001e\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u00048\u0002X\u0083\u0004\u00a2\u0006\b\n\u0000\u0012\u0004\b\u0006\u0010\u0002R\u0010\u0010\u0007\u001a\u00020\b8\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00069"}, d2={"Lnet/ccbluex/liquidbounce/utils/render/ColorUti;", "", "()V", "COLOR_PATTERN", "Ljava/util/regex/Pattern;", "kotlin.jvm.PlatformType", "getCOLOR_PATTERN$annotations", "hexColors", "", "blend", "Ljava/awt/Color;", "color1", "color2", "ratio", "", "blendColors", "fractions", "", "colors", "", "progress", "", "([F[Ljava/awt/Color;F)Ljava/awt/Color;", "blueRainbow", "offset", "", "getFractionIndices", "getHealthColor", "health", "maxHealth", "getRainbow", "", "speed", "greenRainbow", "newRainbow", "speed2", "otherRainbow", "rainbowSpeed", "rainbow", "alpha", "saturation", "rainbow2", "fade", "rainbow22", "rainbow3", "rainbowBright", "rainbowW", "randomMagicText", "", "text", "reAlpha", "color", "redRainbow", "stripColor", "input", "translateAlternateColorCodes", "textToTranslate", "KyinoClient"})
public final class ColorUti {
    private static final Pattern COLOR_PATTERN;
    @JvmField
    @NotNull
    public static final int[] hexColors;
    public static final ColorUti INSTANCE;

    @JvmStatic
    private static /* synthetic */ void getCOLOR_PATTERN$annotations() {
    }

    @Nullable
    public final Color rainbow2(long offset, float fade) {
        float hue = (float)(System.nanoTime() + offset) / 1.0E10f % 1.0f;
        String string = Integer.toHexString(Color.HSBtoRGB(hue, 1.0f, 1.0f));
        Intrinsics.checkExpressionValueIsNotNull(string, "Integer.toHexString(Colo\u2026SBtoRGB(hue, 1.0f, 1.0f))");
        String string2 = string;
        int n = 16;
        boolean bl = false;
        long color = Long.parseLong(string2, CharsKt.checkRadix(n));
        Color c = new Color((int)color);
        return new Color((float)c.getRed() / 255.0f * fade, (float)c.getGreen() / 255.0f * fade, (float)c.getBlue() / 255.0f * fade, (float)c.getAlpha() / 255.0f);
    }

    @JvmStatic
    @Nullable
    public static final Color getHealthColor(float health, float maxHealth) {
        float[] fractions = new float[]{0.0f, 0.5f, 1.0f};
        Color[] colors = new Color[]{new Color(108, 0, 0), new Color(255, 51, 0), Color.GREEN};
        float progress = health / maxHealth;
        return INSTANCE.blendColors(fractions, colors, progress).brighter();
    }

    @JvmStatic
    @Nullable
    public static final String stripColor(@Nullable String input) {
        String string = input;
        if (string == null) {
            return null;
        }
        return COLOR_PATTERN.matcher(string).replaceAll("");
    }

    @JvmStatic
    public static final int getRainbow(int speed, int offset) {
        float hue = (System.currentTimeMillis() + (long)offset) % (long)speed;
        Color color = Color.getHSBColor(hue /= (float)speed, 1.0f, 1.0f);
        Intrinsics.checkExpressionValueIsNotNull(color, "Color.getHSBColor(hue, 1.0f, 1.0f)");
        return color.getRGB();
    }

    @NotNull
    public final Color blend(@NotNull Color color1, @NotNull Color color2, double ratio) {
        Intrinsics.checkParameterIsNotNull(color1, "color1");
        Intrinsics.checkParameterIsNotNull(color2, "color2");
        float r = (float)ratio;
        float ir = 1.0f - r;
        float[] rgb1 = new float[3];
        float[] rgb2 = new float[3];
        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);
        return new Color(rgb1[0] * r + rgb2[0] * ir, rgb1[1] * r + rgb2[1] * ir, rgb1[2] * r + rgb2[2] * ir);
    }

    @NotNull
    public final Color blendColors(@NotNull float[] fractions, @NotNull Color[] colors, float progress) {
        Intrinsics.checkParameterIsNotNull(fractions, "fractions");
        Intrinsics.checkParameterIsNotNull(colors, "colors");
        if (fractions.length == colors.length) {
            int[] indices = this.getFractionIndices(fractions, progress);
            float[] range = new float[]{fractions[indices[0]], fractions[indices[1]]};
            Color[] colorRange = new Color[]{colors[indices[0]], colors[indices[1]]};
            float max = range[1] - range[0];
            float value = progress - range[0];
            float weight = value / max;
            return this.blend(colorRange[0], colorRange[1], 1.0 - (double)weight);
        }
        throw (Throwable)new IllegalArgumentException("Fractions and colours must have equal number of elements");
    }

    @NotNull
    public final int[] getFractionIndices(@NotNull float[] fractions, float progress) {
        Intrinsics.checkParameterIsNotNull(fractions, "fractions");
        int startPoint = 0;
        int[] range = new int[2];
        for (startPoint = 0; startPoint < fractions.length && fractions[startPoint] <= progress; ++startPoint) {
        }
        if (startPoint >= fractions.length) {
            startPoint = fractions.length - 1;
        }
        range[0] = startPoint - 1;
        range[1] = startPoint;
        return range;
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
    public static final Color rainbow22() {
        Color currentColor = new Color(Color.HSBtoRGB((float)(System.nanoTime() + 400000L) / 1.0E10f % 1.0f, 1.0f, 1.0f));
        return new Color((float)currentColor.getRed() / 255.0f * 1.0f, (float)currentColor.getGreen() / 255.0f * 1.0f, (float)currentColor.getBlue() / 255.0f * 1.0f, (float)currentColor.getAlpha() / 255.0f);
    }

    @JvmStatic
    @NotNull
    public static final Color rainbow22(long offset) {
        Color currentColor = new Color(Color.HSBtoRGB((float)(System.nanoTime() + offset) / 1.0E10f % 1.0f, 1.0f, 1.0f));
        return new Color((float)currentColor.getRed() / 255.0f * 1.0f, (float)currentColor.getGreen() / 255.0f * 1.0f, (float)currentColor.getBlue() / 255.0f * 1.0f, (float)currentColor.getAlpha() / 255.0f);
    }

    @JvmStatic
    @NotNull
    public static final Color rainbow3(long offset, float rainbowSpeed, float rainbowBright) {
        Color currentColor = new Color(Color.HSBtoRGB((float)(System.nanoTime() + offset) / 1.0E10f % 1.0f, rainbowSpeed, rainbowBright));
        return new Color((float)currentColor.getRed() / 255.0f * 1.0f, (float)currentColor.getGreen() / 255.0f * 1.0f, (float)currentColor.getBlue() / 255.0f * 1.0f, (float)currentColor.getAlpha() / 255.0f);
    }

    @JvmStatic
    @NotNull
    public static final Color rainbowW(long offset) {
        Color currentColor = new Color(Color.HSBtoRGB((float)(System.nanoTime() + offset) / 1.0E10f % 1.0f, 0.6f, 1.0f));
        return new Color(0.0f, (float)currentColor.getRed() / 255.0f * 1.0f, (float)currentColor.getBlue() / 255.0f * 1.0f, (float)currentColor.getAlpha() / 255.0f);
    }

    @JvmStatic
    @NotNull
    public static final Color redRainbow(long offset) {
        Color currentColor = new Color(Color.HSBtoRGB((float)(System.nanoTime() + offset) / 1.0E10f % 1.0f, 0.5f, 1.0f));
        return new Color((float)currentColor.getRed() / 255.0f * 1.0f, 0.0f, 0.0f, (float)currentColor.getAlpha() / 255.0f);
    }

    @JvmStatic
    @NotNull
    public static final Color greenRainbow(long offset) {
        Color currentColor = new Color(Color.HSBtoRGB((float)(System.nanoTime() + offset) / 1.0E10f % 1.0f, 0.5f, 1.0f));
        return new Color(0.0f, (float)currentColor.getGreen() / 255.0f * 1.0f, 0.0f, (float)currentColor.getAlpha() / 255.0f);
    }

    @JvmStatic
    @NotNull
    public static final Color blueRainbow(long offset) {
        Color currentColor = new Color(Color.HSBtoRGB((float)(System.nanoTime() + offset) / 1.0E10f % 1.0f, 0.5f, 1.0f));
        return new Color(0.0f, 0.0f, (float)currentColor.getBlue() / 255.0f * 1.0f, (float)currentColor.getAlpha() / 255.0f);
    }

    @JvmStatic
    @NotNull
    public static final Color rainbow(float alpha) {
        return ColorUti.rainbow(400000L, alpha, 0.5f);
    }

    @JvmStatic
    @NotNull
    public static final Color rainbow(int alpha) {
        return ColorUti.rainbow(400000L, alpha / 255, 0.5f);
    }

    @JvmStatic
    @NotNull
    public static final Color rainbow(long offset, int alpha, float saturation) {
        return ColorUti.rainbow(offset, (float)alpha / (float)255, saturation);
    }

    @JvmStatic
    @NotNull
    public static final Color rainbow(long offset, float alpha, float saturation) {
        Color currentColor = new Color(Color.HSBtoRGB((float)(System.nanoTime() - offset) / 7.5E9f % 1.0f, saturation, 1.0f));
        return new Color((float)currentColor.getRed() / 255.0f, (float)currentColor.getGreen() / 255.0f, (float)currentColor.getBlue() / 255.0f, alpha);
    }

    @JvmStatic
    @NotNull
    public static final Color rainbow22(float alpha) {
        return ColorUti.rainbow(400000L, alpha, 0.5f);
    }

    @JvmStatic
    @NotNull
    public static final Color rainbow22(int alpha) {
        return ColorUti.rainbow(400000L, alpha / 255, 0.5f);
    }

    @JvmStatic
    @NotNull
    public static final Color rainbow22(long offset, int alpha, float saturation) {
        return ColorUti.rainbow(offset, (float)alpha / (float)255, saturation);
    }

    @JvmStatic
    @NotNull
    public static final Color rainbow22(long offset, float alpha, float saturation) {
        Color currentColor = new Color(Color.HSBtoRGB((float)(System.nanoTime() - offset) / 7.5E9f % 1.0f, saturation, 1.0f));
        return new Color((float)currentColor.getRed() / 255.0f, (float)currentColor.getGreen() / 255.0f, (float)currentColor.getBlue() / 255.0f, alpha);
    }

    @JvmStatic
    public static final int newRainbow(float speed, float offset, float speed2) {
        float color = ((float)new Date().getTime() + offset) % speed;
        return new Color(Color.HSBtoRGB(color /= speed, 0.55f, speed2)).getRGB();
    }

    @JvmStatic
    public static final int otherRainbow(float offset, float rainbowSpeed) {
        int currentColor = new Color(Color.HSBtoRGB(offset, rainbowSpeed, 1.0f)).getRGB();
        return currentColor;
    }

    @JvmStatic
    @NotNull
    public static final Color reAlpha(@NotNull Color color, int alpha) {
        Intrinsics.checkParameterIsNotNull(color, "color");
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    private ColorUti() {
    }

    static {
        ColorUti colorUti;
        INSTANCE = colorUti = new ColorUti();
        COLOR_PATTERN = Pattern.compile("(?i)\u0e22\u0e07[0-9A-FK-OR]");
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
            ColorUti.hexColors[i] = (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
        }
    }
}

