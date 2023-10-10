package tech.atani.client.utility.render.color;

import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.utility.math.MathUtil;

import java.awt.*;
import java.util.Random;

public class ColorUtil {

    public static final int[] RAINBOW_COLORS = {
            0xfffc6a8c, 0xfffc6ad5, 0xffda6afc, 0xff916afc, 0xff6a8cfc, 0xff6ad5fc, 0xffda6afc, 0xfffc6a8c,
    };

    private static final int[] HEALTH_COLOURS = {
            0xFF006B32, // Darker green
            0xFFFFFF00,
            0xFFFF8000,
            0xFFFF0000,
            0xFF800000
    };

    public static int blendRainbowColours(final double progress) {
        return blendColours(RAINBOW_COLORS, progress);
    }

    public static int blendRainbowColours(final long offset) {
        return blendRainbowColours(getFadingFromSysTime(offset));
    }

    public static int blendHealthColours(final double progress) {
        return blendColours(HEALTH_COLOURS, progress);
    }

    public static float[] colorToRGBA(int col) {
        if ((col & 0xFC000000) == 0) {
            col |= 0xFF000000;
        }
        return new float[]{(float)(col >> 16 & 0xFF) / 255.0f, (float)(col >> 8 & 0xFF) / 255.0f, (float)(col & 0xFF) / 255.0f, (float)(col >> 24 & 0xFF) / 255.0f};
    }

    public static Color setAlpha(final Color color, final int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static Color setAlpha(final Color color, final float alpha) {
        return new Color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, alpha);
    }

    public static Color generateRandomTonedColor(int baseHue, int minValue, int maxValue, int alpha) {
        Random random = new Random();

        // Generate random saturation and value within the given range
        float saturation = random.nextFloat();
        float value = minValue + random.nextInt(maxValue - minValue + 1) / 255.0f;

        return Color.getHSBColor(baseHue / 360.0f, saturation, value).darker();
    }

    public static int darken(final int color, final float factor) {
        final int r = (int)((color >> 16 & 0xFF) * factor);
        final int g = (int)((color >> 8 & 0xFF) * factor);
        final int b = (int)((color & 0xFF) * factor);
        final int a = color >> 24 & 0xFF;
        return (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF) | (a & 0xFF) << 24;
    }

    public static Color getGradientOffset(Color color1, Color color2, double offset) {
        if (offset > 1) {
            double left = offset % 1;
            int off = (int) offset;
            offset = off % 2 == 0 ? left : 1 - left;
        }
        double inverse_percent = 1 - offset;
        int redPart = (int) (color1.getRed() * inverse_percent + color2.getRed() * offset);
        int greenPart = (int) (color1.getGreen() * inverse_percent + color2.getGreen() * offset);
        int bluePart = (int) (color1.getBlue() * inverse_percent + color2.getBlue() * offset);
        return new Color(redPart, greenPart, bluePart);
    }

    public static int blendColours(final int[] colours, final double progress) {
        final int size = colours.length;
        if (progress == 1.f) return colours[0];
        else if (progress == 0.f) return colours[size - 1];
        final double mulProgress = Math.max(0, (1 - progress) * (size - 1));
        final int index = (int) mulProgress;
        return fadeBetween(colours[index], colours[index + 1], mulProgress - index);
    }

    public static int fadeBetween(int startColour, int endColour, double progress) {
        if (progress > 1) progress = 1 - progress % 1;
        return fadeTo(startColour, endColour, progress);
    }

    public static int fadeBetween(int[] colors, int endColour, long offset) {
        return blendColours(colors, ((System.currentTimeMillis() + offset) % 2000L) / 1000.0);
    }

    public static int fadeBetween(int startColour, int endColour, long offset) {
        return fadeBetween(startColour, endColour, ((System.currentTimeMillis() + offset) % 2000L) / 1000.0);
    }

    public static int fadeBetween(int startColour, int endColour) {
        return fadeBetween(startColour, endColour, 0L);
    }

    public static int getAstolfoColor(Category category) {
        switch (category) {
            case COMBAT:
                return new Color(230, 77, 62).getRGB();
            case MOVEMENT:
                return new Color(48, 203, 116).getRGB();
            case PLAYER:
                return new Color(141, 67, 169).getRGB();
            case MISCELLANEOUS:
                return new Color(75, 145, 190).getRGB();
            case RENDER:
                return new Color(245, 155, 27).getRGB();
            case HUD:
                return new Color(56, 0, 196).getRGB();
            case CHAT:
                return new Color(251, 219, 101).getRGB();
            case OPTIONS:
                return new Color(26, 188, 156).getRGB();
            case SERVER:
                return new Color(37, 153, 87).getRGB();
        }
        return -1;
    }

    public static int fadeTo(int startColour, int endColour, double progress) {
        double invert = 1.0 - progress;
        int r = (int) ((startColour >> 16 & 0xFF) * invert +
                (endColour >> 16 & 0xFF) * progress);
        int g = (int) ((startColour >> 8 & 0xFF) * invert +
                (endColour >> 8 & 0xFF) * progress);
        int b = (int) ((startColour & 0xFF) * invert +
                (endColour & 0xFF) * progress);
        int a = (int) ((startColour >> 24 & 0xFF) * invert +
                (endColour >> 24 & 0xFF) * progress);
        return ((a & 0xFF) << 24) |
                ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8) |
                (b & 0xFF);
    }

    public static double getFadingFromSysTime(final long offset) {
        return ((System.currentTimeMillis() + offset) % 2000L) / 2000.0;
    }

    public static int getRainbow(int speed, int offset) {
        float hue = (System.currentTimeMillis() + offset) % speed;
        hue /= speed;
        return Color.getHSBColor(hue, 0.85f, 1f).getRGB();
    }

    public static int interpolateColor(int color1, int color2, float amount) {
        amount = Math.min(1, Math.max(0, amount));
        Color cColor1 = new Color(color1);
        Color cColor2 = new Color(color2);
        return interpolateColorC(cColor1, cColor2, amount).getRGB();
    }

    public static Color interpolateColorC(Color color1, Color color2, float amount) {
        amount = Math.min(1, Math.max(0, amount));
        return new Color(MathUtil.interpolateInt(color1.getRed(), color2.getRed(), amount),
                MathUtil.interpolateInt(color1.getGreen(), color2.getGreen(), amount),
                MathUtil.interpolateInt(color1.getBlue(), color2.getBlue(), amount),
                MathUtil.interpolateInt(color1.getAlpha(), color2.getAlpha(), amount));
    }

}
