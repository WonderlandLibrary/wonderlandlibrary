/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  net.minecraft.util.ChatAllowedCharacters
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.utils.render;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.awt.Color;
import java.util.Random;
import java.util.regex.Pattern;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.JvmField;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.util.ChatAllowedCharacters;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000d\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0019\n\u0002\b\u0003\n\u0002\u0010\u0015\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\n\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\f\n\u0002\b\n\n\u0002\u0010\u0006\n\u0002\b\u0005\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J(\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0014H\u0007J*\u0010\u0016\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u0010\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0014H\u0007J*\u0010\u0017\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u0010\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0014H\u0007J\u0018\u0010\u0018\u001a\u00020\u000f2\u0006\u0010\u0019\u001a\u00020\r2\u0006\u0010\u001a\u001a\u00020\u0014H\u0007J\u0010\u0010\u001b\u001a\u00020\u000f2\u0006\u0010\u001c\u001a\u00020\u000fH\u0007J\u001a\u0010\u001d\u001a\u00020\u000f2\u0006\u0010\u001e\u001a\u00020\u001f2\b\b\u0002\u0010\u001a\u001a\u00020\u0012H\u0007J\u0010\u0010 \u001a\u00020\u000f2\u0006\u0010!\u001a\u00020\"H\u0007J \u0010#\u001a\u00020\u000f2\u0006\u0010\u001c\u001a\u00020\u000f2\u0006\u0010$\u001a\u00020\u00122\u0006\u0010\u0011\u001a\u00020\u0012H\u0007J\"\u0010%\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u001c\u001a\u00020\u000f2\u0006\u0010$\u001a\u00020\u00122\u0006\u0010\u0011\u001a\u00020\u0012H\u0007J\u0010\u0010&\u001a\u00020\u000f2\u0006\u0010\u001c\u001a\u00020\u000fH\u0007J\"\u0010'\u001a\u00020\u000f2\u0006\u0010(\u001a\u00020\u00142\u0006\u0010)\u001a\u00020\u00142\b\b\u0002\u0010\u001a\u001a\u00020\u0012H\u0007J6\u0010*\u001a\u00020\u000f2\u0006\u0010$\u001a\u00020\u00122\b\b\u0002\u0010+\u001a\u00020\u00142\b\b\u0002\u0010,\u001a\u00020\u00142\b\b\u0002\u0010-\u001a\u00020\u00122\b\b\u0002\u0010.\u001a\u00020\u0012J.\u0010/\u001a\u00020\u000f2\b\b\u0002\u0010+\u001a\u00020\u00142\b\b\u0002\u0010,\u001a\u00020\u00142\b\b\u0002\u0010-\u001a\u00020\u00122\b\b\u0002\u0010.\u001a\u00020\u0012J\u000e\u00100\u001a\u0002012\u0006\u00102\u001a\u000203J\u0010\u00104\u001a\u00020\u000f2\u0006\u0010\u0019\u001a\u00020\rH\u0007J\b\u00105\u001a\u00020\u000fH\u0007J\u0010\u00105\u001a\u00020\u000f2\u0006\u0010\u001a\u001a\u00020\u0014H\u0007J\u0010\u00105\u001a\u00020\u000f2\u0006\u0010\u001a\u001a\u00020\u0012H\u0007J\u0010\u00105\u001a\u00020\u000f2\u0006\u0010\u0019\u001a\u00020\rH\u0007J\u0018\u00105\u001a\u00020\u000f2\u0006\u0010\u0019\u001a\u00020\r2\u0006\u0010\u001a\u001a\u00020\u0014H\u0007J\u0018\u00105\u001a\u00020\u000f2\u0006\u0010\u0019\u001a\u00020\r2\u0006\u0010\u001a\u001a\u00020\u0012H\u0007J\u000e\u00106\u001a\u00020\u001f2\u0006\u00107\u001a\u00020\u001fJ\u0018\u00108\u001a\u00020\u000f2\u0006\u0010\u001c\u001a\u00020\u000f2\u0006\u0010\u001a\u001a\u00020\u0012H\u0007J&\u00109\u001a\u00020\u000f2\u0006\u0010:\u001a\u00020\u00122\u0006\u0010;\u001a\u00020\u00142\u0006\u0010<\u001a\u00020\u00142\u0006\u0010=\u001a\u00020>J\u0014\u0010?\u001a\u0004\u0018\u00010\u001f2\b\u0010@\u001a\u0004\u0018\u00010\u001fH\u0007J\u0010\u0010A\u001a\u00020\u001f2\u0006\u0010B\u001a\u00020\u001fH\u0007R\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0010\u0010\n\u001a\u00020\u000b8\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006C"}, d2={"Lnet/ccbluex/liquidbounce/utils/render/ColorUtils;", "", "()V", "COLOR_PATTERN", "Ljava/util/regex/Pattern;", "kotlin.jvm.PlatformType", "allowedCharactersArray", "", "getAllowedCharactersArray", "()[C", "hexColors", "", "startTime", "", "LiquidSlowly", "Ljava/awt/Color;", "time", "count", "", "qd", "", "sq", "LiquidSlowly2", "LiquidSlowlys", "TwoRainbow", "offset", "alpha", "antiColor", "color", "colorCode", "code", "", "decodeColorJsonFormat", "json", "Lcom/google/gson/JsonObject;", "fade", "index", "fade2", "getOppositeColor", "healthColor", "hp", "maxHP", "hslRainbow", "lowest", "bigest", "indexOffset", "timeSplit", "hsslRainbow", "isAllowedCharacter", "", "character", "", "originalrainbow", "rainbow", "randomMagicText", "text", "reAlpha", "skyRainbow", "var2", "bright", "st", "speed", "", "stripColor", "input", "translateAlternateColorCodes", "textToTranslate", "KyinoClient"})
public final class ColorUtils {
    @NotNull
    private static final char[] allowedCharactersArray;
    private static final Pattern COLOR_PATTERN;
    @JvmField
    @NotNull
    public static final int[] hexColors;
    private static final long startTime;
    public static final ColorUtils INSTANCE;

    @NotNull
    public final char[] getAllowedCharactersArray() {
        return allowedCharactersArray;
    }

    public final boolean isAllowedCharacter(char character) {
        return character != '\u00a7' && character >= ' ' && character != '\u007f';
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
    public static final Color getOppositeColor(@NotNull Color color) {
        Intrinsics.checkParameterIsNotNull(color, "color");
        return new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue(), color.getAlpha());
    }

    @JvmStatic
    @NotNull
    public static final Color antiColor(@NotNull Color color) {
        Intrinsics.checkParameterIsNotNull(color, "color");
        return new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue(), color.getAlpha());
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
    public static final Color originalrainbow(long offset) {
        Color currentColor = new Color(Color.HSBtoRGB((float)(System.nanoTime() + offset) / 1.0E10f % 1.0f, 1.0f, 1.0f));
        return new Color((float)currentColor.getRed() / 255.0f * 1.0f, (float)currentColor.getGreen() / 255.0f * 1.0f, (float)currentColor.getBlue() / 255.0f * 1.0f, (float)currentColor.getAlpha() / 255.0f);
    }

    @JvmStatic
    @Nullable
    public static final Color LiquidSlowly2(long time, int count, float qd, float sq) {
        Color color = new Color(Color.HSBtoRGB(((float)time + (float)count * -3000000.0f) / (float)2 / 1.0E9f, qd, sq));
        return new Color((float)color.getRed() / 255.0f * 1.0f, (float)color.getGreen() / 255.0f * 1.0f, (float)color.getBlue() / 255.0f * 1.0f, (float)color.getAlpha() / 255.0f);
    }

    @JvmStatic
    @NotNull
    public static final Color LiquidSlowly(long time, int count, float qd, float sq) {
        Color color = new Color(Color.HSBtoRGB(((float)time + (float)count * -3000000.0f) / (float)2 / 1.0E9f, qd, sq));
        return new Color((float)color.getRed() / 255.0f * 1.0f, (float)color.getGreen() / 255.0f * 1.0f, (float)color.getBlue() / 255.0f * 1.0f, (float)color.getAlpha() / 255.0f);
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

    public static /* synthetic */ Color colorCode$default(String string, int n, int n2, Object object) {
        if ((n2 & 2) != 0) {
            n = 255;
        }
        return ColorUtils.colorCode(string, n);
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

    public static /* synthetic */ Color healthColor$default(float f, float f2, int n, int n2, Object object) {
        if ((n2 & 4) != 0) {
            n = 255;
        }
        return ColorUtils.healthColor(f, f2, n);
    }

    @JvmStatic
    @NotNull
    public static final Color decodeColorJsonFormat(@NotNull JsonObject json) {
        int n;
        Color color;
        Intrinsics.checkParameterIsNotNull(json, "json");
        if (json.has("rainbow")) {
            JsonElement jsonElement = json.get("rainbow");
            Intrinsics.checkExpressionValueIsNotNull(jsonElement, "json.get(\"rainbow\")");
            String string = jsonElement.getAsString();
            Intrinsics.checkExpressionValueIsNotNull(string, "json.get(\"rainbow\").asString");
            String string2 = string;
            boolean bl = false;
            String string3 = string2;
            if (string3 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string4 = string3.toLowerCase();
            Intrinsics.checkExpressionValueIsNotNull(string4, "(this as java.lang.String).toLowerCase()");
            switch (string4) {
                case "normal": {
                    int n2;
                    if (json.has("rainbow_index")) {
                        JsonElement jsonElement2 = json.get("rainbow_index");
                        Intrinsics.checkExpressionValueIsNotNull(jsonElement2, "json.get(\"rainbow_index\")");
                        n2 = jsonElement2.getAsInt();
                    } else {
                        n2 = 1;
                    }
                    color = ColorUtils.rainbow(400000000L * (long)n2);
                    break;
                }
                case "other": {
                    int n3;
                    if (json.has("rainbow_index")) {
                        JsonElement jsonElement3 = json.get("rainbow_index");
                        Intrinsics.checkExpressionValueIsNotNull(jsonElement3, "json.get(\"rainbow_index\")");
                        n3 = jsonElement3.getAsInt();
                    } else {
                        n3 = 1;
                    }
                    color = RenderUtils.arrayRainbow(n3 + 1);
                    break;
                }
                default: {
                    color = Color.WHITE;
                    break;
                }
            }
        } else {
            JsonElement jsonElement = json.get("red");
            Intrinsics.checkExpressionValueIsNotNull(jsonElement, "json.get(\"red\")");
            int n4 = jsonElement.getAsInt();
            JsonElement jsonElement4 = json.get("green");
            Intrinsics.checkExpressionValueIsNotNull(jsonElement4, "json.get(\"green\")");
            int n5 = jsonElement4.getAsInt();
            JsonElement jsonElement5 = json.get("blue");
            Intrinsics.checkExpressionValueIsNotNull(jsonElement5, "json.get(\"blue\")");
            color = new Color(n4, n5, jsonElement5.getAsInt());
        }
        Intrinsics.checkExpressionValueIsNotNull(color, "if(json.has(\"rainbow\")){\u2026(\"blue\").asInt)\n        }");
        if (json.has("alpha")) {
            JsonElement jsonElement = json.get("alpha");
            Intrinsics.checkExpressionValueIsNotNull(jsonElement, "json.get(\"alpha\")");
            n = jsonElement.getAsInt();
        } else {
            n = 160;
        }
        return ColorUtils.reAlpha(color, n);
    }

    @JvmStatic
    @NotNull
    public static final Color TwoRainbow(long offset, float alpha) {
        Color currentColor = new Color(Color.HSBtoRGB((float)(System.nanoTime() + offset) / 9.0E10f % 1.0f, 0.75f, 0.8f));
        return new Color((float)currentColor.getRed() / 255.0f * 1.0f, (float)currentColor.getGreen() / 255.0f * 1.0f, (float)currentColor.getBlue() / 255.0f * 1.0f, alpha);
    }

    @JvmStatic
    @Nullable
    public static final Color LiquidSlowlys(long time, int count, float qd, float sq) {
        Color color = new Color(Color.HSBtoRGB(((float)time + (float)count * -3000000.0f) / (float)2 / 1.0E9f, qd, sq));
        return new Color((float)color.getRed() / 255.0f * 1.0f, (float)color.getGreen() / 255.0f * 1.0f, (float)color.getBlue() / 255.0f * 1.0f, (float)color.getAlpha() / 255.0f);
    }

    @JvmStatic
    @Nullable
    public static final Color fade2(@NotNull Color color, int index, int count) {
        Intrinsics.checkParameterIsNotNull(color, "color");
        float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        float brightness = Math.abs(((float)(System.currentTimeMillis() % 10000L) / 1000.0f + (float)index / (float)count * 2.0f) % 2.0f - 1.0f);
        brightness = 0.5f + 0.5f * brightness;
        hsb[2] = brightness % 2.0f;
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
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
        return ColorUtils.rainbow(400000L, alpha);
    }

    @JvmStatic
    @NotNull
    public static final Color rainbow(int alpha) {
        return ColorUtils.rainbow(400000L, alpha / 255);
    }

    @JvmStatic
    @NotNull
    public static final Color rainbow(long offset, int alpha) {
        return ColorUtils.rainbow(offset, (float)alpha / (float)255);
    }

    @JvmStatic
    @NotNull
    public static final Color rainbow(long offset, float alpha) {
        Color currentColor = new Color(Color.HSBtoRGB((float)(System.nanoTime() + offset) / 1.0E10f % 1.0f, 1.0f, 1.0f));
        return new Color((float)currentColor.getRed() / 255.0f * 1.0f, (float)currentColor.getGreen() / 255.0f * 1.0f, (float)currentColor.getBlue() / 255.0f * 1.0f, alpha);
    }

    @NotNull
    public final Color hslRainbow(int index, float lowest, float bigest, int indexOffset, int timeSplit) {
        float f = (float)((int)(System.currentTimeMillis() - startTime) + index * indexOffset) / (float)timeSplit % (float)2 - 1.0f;
        boolean bl = false;
        Color color = Color.getHSBColor(Math.abs(f) * (bigest - lowest) + lowest, 0.7f, 1.0f);
        Intrinsics.checkExpressionValueIsNotNull(color, "Color.getHSBColor((abs((\u2026-lowest))+lowest,0.7f,1f)");
        return color;
    }

    public static /* synthetic */ Color hslRainbow$default(ColorUtils colorUtils, int n, float f, float f2, int n2, int n3, int n4, Object object) {
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
            n3 = 3000;
        }
        return colorUtils.hslRainbow(n, f, f2, n2, n3);
    }

    @NotNull
    public final Color hsslRainbow(float lowest, float bigest, int indexOffset, int timeSplit) {
        float f = (float)((int)(System.currentTimeMillis() - startTime) + indexOffset) / (float)timeSplit % (float)2 - 1.0f;
        boolean bl = false;
        Color color = Color.getHSBColor(Math.abs(f) * (bigest - lowest) + lowest, 0.7f, 1.0f);
        Intrinsics.checkExpressionValueIsNotNull(color, "Color.getHSBColor((abs((\u2026-lowest))+lowest,0.7f,1f)");
        return color;
    }

    public static /* synthetic */ Color hsslRainbow$default(ColorUtils colorUtils, float f, float f2, int n, int n2, int n3, Object object) {
        if ((n3 & 1) != 0) {
            f = 0.41f;
        }
        if ((n3 & 2) != 0) {
            f2 = 0.58f;
        }
        if ((n3 & 4) != 0) {
            n = 300;
        }
        if ((n3 & 8) != 0) {
            n2 = 3000;
        }
        return colorUtils.hsslRainbow(f, f2, n, n2);
    }

    @NotNull
    public final Color skyRainbow(int var2, float bright, float st, double speed) {
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
        Intrinsics.checkExpressionValueIsNotNull(color, "Color.getHSBColor(if ((3\u2026.toFloat() }, st, bright)");
        return color;
    }

    @JvmStatic
    @NotNull
    public static final Color reAlpha(@NotNull Color color, int alpha) {
        Intrinsics.checkParameterIsNotNull(color, "color");
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
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

