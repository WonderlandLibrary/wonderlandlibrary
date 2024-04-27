/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.JvmField
 *  kotlin.jvm.JvmStatic
 *  kotlin.text.StringsKt
 *  net.minecraft.util.ChatAllowedCharacters
 */
package ad.utils.Color;

import java.awt.Color;
import java.util.Random;
import kotlin.jvm.JvmField;
import kotlin.jvm.JvmStatic;
import kotlin.text.StringsKt;
import net.minecraft.util.ChatAllowedCharacters;

public final class ColorUtils {
    @JvmField
    public static final int[] hexColors;
    public static final ColorUtils INSTANCE;

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

    public final Color fade(Color a, Color b, double speed, double offset) {
        double time = (double)System.currentTimeMillis() * speed + offset;
        boolean bl = false;
        double d = Math.sin(time);
        boolean bl2 = false;
        double factor = Math.abs(d);
        return this.mix(a, b, (float)factor);
    }

    public static /* synthetic */ Color fade$default(ColorUtils colorUtils, Color color, Color color2, double d, double d2, int n, Object object) {
        if ((n & 4) != 0) {
            d = 0.001;
        }
        if ((n & 8) != 0) {
            d2 = 0.0;
        }
        return colorUtils.fade(color, color2, d, d2);
    }

    public final Color rainbow(double speed, double offset, int alpha) {
        double time = ((double)System.currentTimeMillis() * speed + offset) % 1.0;
        Color c = Color.getHSBColor((float)time, 1.0f, 1.0f);
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
    }

    public static /* synthetic */ Color rainbow$default(ColorUtils colorUtils, double d, double d2, int n, int n2, Object object) {
        if ((n2 & 1) != 0) {
            d = 5.0E-4;
        }
        if ((n2 & 2) != 0) {
            d2 = 0.0;
        }
        if ((n2 & 4) != 0) {
            n = 255;
        }
        return colorUtils.rainbow(d, d2, n);
    }

    public final Color hsbTransition(float from, float to, int angle, float s, float b) {
        return Color.getHSBColor(angle < 180 ? from + (to - from) * ((float)angle / 180.0f) : from + (to - from) * ((float)(-(angle - 360)) / 180.0f), s, b);
    }

    public static /* synthetic */ Color hsbTransition$default(ColorUtils colorUtils, float f, float f2, int n, float f3, float f4, int n2, Object object) {
        if ((n2 & 8) != 0) {
            f3 = 1.0f;
        }
        if ((n2 & 0x10) != 0) {
            f4 = 1.0f;
        }
        return colorUtils.hsbTransition(f, f2, n, f3, f4);
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
            if (!ChatAllowedCharacters.func_71566_a((char)c)) continue;
            int index = new Random().nextInt(allowedCharacters.length());
            String string2 = allowedCharacters;
            StringBuilder stringBuilder2 = stringBuilder;
            boolean bl2 = false;
            char[] cArray = string2.toCharArray();
            stringBuilder2.append(cArray[index]);
        }
        return stringBuilder.toString();
    }

    private ColorUtils() {
    }

    static {
        ColorUtils colorUtils;
        INSTANCE = colorUtils = new ColorUtils();
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
    }
}

