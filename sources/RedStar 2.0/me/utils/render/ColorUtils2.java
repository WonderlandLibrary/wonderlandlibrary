package me.utils.render;

import java.awt.Color;
import java.util.Random;
import java.util.regex.Pattern;
import kotlin.Metadata;
import kotlin.jvm.JvmField;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.features.module.modules.color.Rainbow;
import net.minecraft.util.ChatAllowedCharacters;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000F\n\n\u0000\n\b\n\n\b\n\n\u0000\n\t\n\u0000\n\b\n\b\n\n\u0000\n\n\b\n\n\b\"\n\n\b\f\bÆ\u000020B\b¢J\n02\f0HJ\r0202\b\b\f0HJ02020HJ 0202020HJ02020J\"020202\b\b\f0HJL0202\b\b02\b\b 02\b\b!02\b\b\"02\b\b#02\b\b$0HJ%02&02'02(0J )02*02+02,0HJ\b-0HJ-02\f0HJ-020HJ-0202\f0HJ-0202\f0HJ.02\f0HJ/0200J10202\f0HJ10202\f0HJ2020HJ(30240250260207HJ\"80240260250HJ(902:0\t202;02<0HJ=02>0HJ?02@0HJA020\t2\f0HJB02\f0HR\n *00X¢\n\u0000R08X¢\n\u0000R\b0\tX¢\n\u0000¨C"}, d2={"Lme/utils/render/ColorUtils2;", "", "()V", "COLOR_PATTERN", "Ljava/util/regex/Pattern;", "kotlin.jvm.PlatformType", "hexColors", "", "startTime", "", "black", "", "alpha", "colorCode", "Ljava/awt/Color;", "code", "", "darker", "color", "percentage", "", "fade", "index", "count", "getRainbow", "speed", "offset", "healthColor", "hp", "maxHP", "hslRainbow", "lowest", "bigest", "indexOffset", "timeSplit", "saturation", "brightness", "mixColors", "color1", "color2", "percent", "normalRainbow", "delay", "sat", "brg", "rainbow", "rainbowWithAlpha", "randomMagicText", "text", "reAlpha", "reverseColor", "skyRainbow", "var2", "bright", "st", "", "skyRainbow2", "slowlyRainbow", "time", "qd", "sq", "stripColor", "input", "translateAlternateColorCodes", "textToTranslate", "twoRainbow", "white", "Pride"})
public final class ColorUtils2 {
    private static final Pattern COLOR_PATTERN;
    private static final long startTime;
    @JvmField
    @NotNull
    public static final int[] hexColors;
    public static final ColorUtils2 INSTANCE;

    @JvmStatic
    @NotNull
    public static final String stripColor(@NotNull String input) {
        Intrinsics.checkParameterIsNotNull(input, "input");
        String string = COLOR_PATTERN.matcher(input).replaceAll("");
        Intrinsics.checkExpressionValueIsNotNull(string, "COLOR_PATTERN.matcher(input).replaceAll(\"\")");
        return string;
    }

    @NotNull
    public final Color mixColors(@NotNull Color color1, @NotNull Color color2, float percent) {
        Intrinsics.checkParameterIsNotNull(color1, "color1");
        Intrinsics.checkParameterIsNotNull(color2, "color2");
        return new Color(color1.getRed() + (int)((float)(color2.getRed() - color1.getRed()) * percent), color1.getGreen() + (int)((float)(color2.getGreen() - color1.getGreen()) * percent), color1.getBlue() + (int)((float)(color2.getBlue() - color1.getBlue()) * percent), color1.getAlpha() + (int)((float)(color2.getAlpha() - color1.getAlpha()) * percent));
    }

    public final int getRainbow(int speed, int offset) {
        float hue = (System.currentTimeMillis() + (long)offset) % (long)speed;
        float f = speed;
        boolean bl = false;
        boolean bl2 = false;
        float it = f;
        boolean bl3 = false;
        Color color = Color.getHSBColor(hue /= it, 0.4f, 1.0f);
        Intrinsics.checkExpressionValueIsNotNull(color, "Color.getHSBColor(speed.…/= it; hue }, 0.4f, 1.0f)");
        return color.getRGB();
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
        String allowedCharacters = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000";
        String string = text;
        boolean bl = false;
        char[] cArray = string.toCharArray();
        Intrinsics.checkExpressionValueIsNotNull(cArray, "(this as java.lang.String).toCharArray()");
        for (char c : cArray) {
            char[] cArray2;
            if (!ChatAllowedCharacters.isAllowedCharacter((char)c)) continue;
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
    public static final int black(int alpha) {
        return new Color(0, 0, 0, alpha).getRGB();
    }

    @JvmStatic
    public static final int white(int alpha) {
        return new Color(255, 255, 255, alpha).getRGB();
    }

    @JvmStatic
    public static final int normalRainbow(int delay, float sat, float brg) {
        double d = (double)(System.currentTimeMillis() + (long)delay) / 20.0;
        boolean bl = false;
        double rainbowState = Math.ceil(d);
        Color color = Color.getHSBColor((float)((rainbowState %= 360.0) / (double)360.0f), sat, brg);
        Intrinsics.checkExpressionValueIsNotNull(color, "Color.getHSBColor((rainb….0f).toFloat(), sat, brg)");
        return color.getRGB();
    }

    @JvmStatic
    @Nullable
    public static final Color skyRainbow2(int var2, float st, float bright) {
        double d = System.currentTimeMillis() + (long)(var2 * 109);
        boolean bl = false;
        double d2 = Math.ceil(d);
        double v1 = d2 / (double)5;
        d = 360.0;
        boolean bl2 = false;
        bl = false;
        double it = d;
        boolean bl3 = false;
        return Color.getHSBColor((double)((float)(d / 360.0)) < 0.5 ? -((float)(v1 / 360.0)) : (float)((v1 %= it) / 360.0), st, bright);
    }

    @JvmStatic
    @NotNull
    public static final Color colorCode(@NotNull String code, int alpha) {
        Intrinsics.checkParameterIsNotNull(code, "code");
        String string = code;
        boolean bl = false;
        String string2 = string.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string2, "(this as java.lang.String).toLowerCase()");
        switch (string2) {
            case "0": {
                return new Color(0, 0, 0, alpha);
            }
            case "1": {
                return new Color(0, 0, 170, alpha);
            }
            case "2": {
                return new Color(0, 170, 0, alpha);
            }
            case "3": {
                return new Color(0, 170, 170, alpha);
            }
            case "4": {
                return new Color(170, 0, 0, alpha);
            }
            case "5": {
                return new Color(170, 0, 170, alpha);
            }
            case "6": {
                return new Color(255, 170, 0, alpha);
            }
            case "7": {
                return new Color(170, 170, 170, alpha);
            }
            case "8": {
                return new Color(85, 85, 85, alpha);
            }
            case "9": {
                return new Color(85, 85, 255, alpha);
            }
            case "a": {
                return new Color(85, 255, 85, alpha);
            }
            case "b": {
                return new Color(85, 255, 255, alpha);
            }
            case "c": {
                return new Color(255, 85, 85, alpha);
            }
            case "d": {
                return new Color(255, 85, 255, alpha);
            }
            case "e": {
                return new Color(255, 255, 85, alpha);
            }
        }
        return new Color(255, 255, 255, alpha);
    }

    public static Color colorCode$default(String string, int n, int n2, Object object) {
        if ((n2 & 2) != 0) {
            n = 255;
        }
        return ColorUtils2.colorCode(string, n);
    }

    @JvmStatic
    @NotNull
    public static final Color hslRainbow(int index, float lowest, float bigest, int indexOffset, int timeSplit, float saturation, float brightness) {
        float f = (float)((int)(System.currentTimeMillis() - startTime) + index * indexOffset) / (float)timeSplit % (float)2 - 1.0f;
        boolean bl = false;
        Color color = Color.getHSBColor(Math.abs(f) * (bigest - lowest) + lowest, saturation, brightness);
        Intrinsics.checkExpressionValueIsNotNull(color, "Color.getHSBColor(\n     …     brightness\n        )");
        return color;
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
    @NotNull
    public static final Color rainbow() {
        return ColorUtils2.hslRainbow$default(1, 0.0f, 0.0f, 0, 0, 0.0f, 0.0f, 126, null);
    }

    @JvmStatic
    @NotNull
    public static final Color rainbow(int index) {
        return ColorUtils2.hslRainbow$default(index, 0.0f, 0.0f, 0, 0, 0.0f, 0.0f, 126, null);
    }

    @JvmStatic
    @NotNull
    public static final Color rainbow(float alpha) {
        return ColorUtils2.reAlpha(ColorUtils2.hslRainbow$default(1, 0.0f, 0.0f, 0, 0, 0.0f, 0.0f, 126, null), alpha);
    }

    @JvmStatic
    @NotNull
    public static final Color rainbowWithAlpha(int alpha) {
        return ColorUtils2.reAlpha(ColorUtils2.hslRainbow$default(1, 0.0f, 0.0f, 0, 0, 0.0f, 0.0f, 126, null), alpha);
    }

    @JvmStatic
    @NotNull
    public static final Color rainbow(int index, int alpha) {
        return ColorUtils2.reAlpha(ColorUtils2.hslRainbow$default(index, 0.0f, 0.0f, 0, 0, 0.0f, 0.0f, 126, null), alpha);
    }

    @JvmStatic
    @NotNull
    public static final Color rainbow(int index, float alpha) {
        return ColorUtils2.reAlpha(ColorUtils2.hslRainbow$default(index, 0.0f, 0.0f, 0, 0, 0.0f, 0.0f, 126, null), alpha);
    }

    @JvmStatic
    @NotNull
    public static final Color reAlpha(@NotNull Color color, int alpha) {
        Intrinsics.checkParameterIsNotNull(color, "color");
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    @JvmStatic
    @NotNull
    public static final Color reAlpha(@NotNull Color color, float alpha) {
        Intrinsics.checkParameterIsNotNull(color, "color");
        return new Color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, alpha);
    }

    @JvmStatic
    @NotNull
    public static final Color slowlyRainbow(long time, int count, float qd, float sq) {
        Color color = new Color(Color.HSBtoRGB(((float)time + (float)count * -3000000.0f) / (float)2 / 1.0E9f, qd, sq));
        return new Color((float)color.getRed() / 255.0f * 1.0f, (float)color.getGreen() / 255.0f * 1.0f, (float)color.getBlue() / 255.0f * 1.0f, (float)color.getAlpha() / 255.0f);
    }

    @JvmStatic
    @NotNull
    public static final Color twoRainbow(long offset, float alpha) {
        Color currentColor = new Color(Color.HSBtoRGB((float)(System.nanoTime() + offset) / 8.9999999E10f % 1.0f, 0.75f, 0.8f));
        return new Color((float)currentColor.getRed() / 255.0f * 1.0f, (float)currentColor.getGreen() / 255.0f * 1.0f, (float)currentColor.getBlue() / 255.0f * 1.0f, alpha);
    }

    @JvmStatic
    @NotNull
    public static final Color skyRainbow(int var2, float bright, float st, double speed) {
        double d = (double)System.currentTimeMillis() / speed + (double)((long)var2 * 109L);
        boolean bl = false;
        double d2 = Math.ceil(d);
        double v1 = d2 / (double)5;
        d = 360.0;
        boolean bl2 = false;
        bl = false;
        double it = d;
        boolean bl3 = false;
        Color color = Color.getHSBColor(d / 360.0 < 0.5 ? -((float)(v1 / 360.0)) : (float)((v1 %= it) / 360.0), st, bright);
        Intrinsics.checkExpressionValueIsNotNull(color, "Color.getHSBColor(if ((3…()\n        }, st, bright)");
        return color;
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
    public static final Color reverseColor(@NotNull Color color) {
        Intrinsics.checkParameterIsNotNull(color, "color");
        return new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue(), color.getAlpha());
    }

    @JvmStatic
    @NotNull
    public static final Color healthColor(float hp, float maxHP, int alpha) {
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

    public static Color healthColor$default(float f, float f2, int n, int n2, Object object) {
        if ((n2 & 4) != 0) {
            n = 255;
        }
        return ColorUtils2.healthColor(f, f2, n);
    }

    @JvmStatic
    @NotNull
    public static final Color darker(@NotNull Color color, float percentage) {
        Intrinsics.checkParameterIsNotNull(color, "color");
        return new Color((int)((float)color.getRed() * percentage), (int)((float)color.getGreen() * percentage), (int)((float)color.getBlue() * percentage), (int)((float)color.getAlpha() * percentage));
    }

    private ColorUtils2() {
    }

    static {
        ColorUtils2 colorUtils2;
        INSTANCE = colorUtils2 = new ColorUtils2();
        COLOR_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]");
        startTime = System.currentTimeMillis();
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
            ColorUtils2.hexColors[i] = (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
        }
    }
}
