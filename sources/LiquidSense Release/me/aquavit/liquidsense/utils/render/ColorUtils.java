package me.aquavit.liquidsense.utils.render;

import net.minecraft.util.ChatAllowedCharacters;

import java.awt.*;
import java.util.Random;
import java.util.regex.Pattern;

public class ColorUtils {

    public static Pattern COLOR_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]");
    public static Pattern COLOR_PATTERN2 = Pattern.compile("(?i)§[0-9A-F]");

    public static final int[] hexColors = new int[16];;

    public static final String stripColor(String input) {
        if (input == null) return null;
        return COLOR_PATTERN.matcher(input).replaceAll("");
    }

    public static final String stripColor2(String input) {
        if (input == null) return null;
        return COLOR_PATTERN2.matcher(input).replaceAll("");
    }

    static {
        for (int i = 0; i < 16; ++i) {
            int baseColor = (i >> 3 & 1) * 85;
            int red = (i >> 2 & 1) * 170 + baseColor + (i == 6 ? 85 : 0);
            int green = (i >> 1 & 1) * 170 + baseColor;
            int blue = (i & 1) * 170 + baseColor;
            ColorUtils.hexColors[i] = (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
        }
    }

    public static String translateAlternateColorCodes(String textToTranslate) {
        char[] chars = textToTranslate.toCharArray();

        for (int i = 0; i < chars.length - 1; ++i) {
            if (chars[i] == '&' && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".contains(String.valueOf(chars[i + 1]))) {
                chars[i] = '§';
                chars[i + 1] = Character.toLowerCase(chars[i + 1]);
            }
        }

        return String.valueOf(chars);
    }

    public static String randomMagicText(String text) {
        StringBuilder stringBuilder = new StringBuilder();
        String allowedCharacters = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000";

        for (char c : text.toCharArray()) {
            if (ChatAllowedCharacters.isAllowedCharacter(c)) {
                int index = new Random().nextInt(allowedCharacters.length());
                stringBuilder.append(allowedCharacters.toCharArray()[index]);
            }
        }

        return stringBuilder.toString();
    }

    public static Color rainbow() {
        final Color currentColor = new Color(Color.HSBtoRGB((System.nanoTime() + 400000L) / 10000000000F % 1, 0.6f, 1f));
        return new Color(currentColor.getRed() / 255.0f * 1.0f,
                currentColor.getGreen() / 255.0f * 1.0f,
                currentColor.getBlue() / 255.0f * 1.0f,
                currentColor.getAlpha() / 255.0f);
    }

    public static Color rainbow(long offset) {
        final Color currentColor = new Color(Color.HSBtoRGB((System.nanoTime() + offset) / 10000000000F % 1, 0.6f, 1f));
        return new Color(currentColor.getRed() / 255.0f * 0.75F,
                currentColor.getGreen() / 255.0f * 0.75F,
                currentColor.getBlue() / 255.0f * 0.75F,
                currentColor.getAlpha() / 255.0f);
    }

    public static Color rainbow(long offset, int alpha) {
        return rainbow(offset, (float) alpha / 255);
    }

    public static Color rainbow(long offset, float alpha) {
        final Color currentColor = new Color(Color.HSBtoRGB((System.nanoTime() - offset) / 6000000000F % 1, 0.6F, 1F));
        return new Color(currentColor.getRed() / 255.0f,
                currentColor.getGreen() / 255.0f,
                currentColor.getBlue() / 255.0f,
                alpha);
    }

    public static int rainbow(int offset, int speed) {
        final long hue = (System.currentTimeMillis() + (long) offset) % (long)speed;
        return Color.getHSBColor(hue / (float) speed, 0.35f, 1f).getRGB();
    }

    public static Color rainbow(float speed, float counter, float offset, float saturation, float fade) {
        final float hue = (speed + (1 + counter) * offset) / 1.0E10f % 1.0f;
        final Color color = new Color((int) Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, saturation, 1.0f)), 16));
        return new Color((float)color.getRed() / 255.0f * fade, (float)color.getGreen() / 255.0f * fade, (float)color.getBlue() / 255.0f * fade, (float)color.getAlpha() / 255.0f);
    }

    public static int Astolfo(int var2, float st, float bright) {
        double currentColor = Math.ceil(System.currentTimeMillis() + (long) (var2 * 130)) / 6;
        return Color.getHSBColor((double) ((float) ((currentColor %= 360.0) / 360.0)) < 0.5 ? -((float) (currentColor / 360.0)) : (float) (currentColor / 360.0), st, bright).getRGB();
    }
}
