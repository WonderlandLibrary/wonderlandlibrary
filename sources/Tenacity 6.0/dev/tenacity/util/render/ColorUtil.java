package dev.tenacity.util.render;

import dev.tenacity.util.misc.MathUtil;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public final class ColorUtil {

    private static Color primaryColor = new Color(0xffcf03fc);

    private static final Color surfaceColor = new Color(0xff1C1B1F),
            surfaceVariantColor = new Color(0xff49454F);

    public static void setPrimaryColor(final Color color) {
        primaryColor = color;
    }

    public static Color getPrimaryColor() {
        return primaryColor;
    }

    public static Color getSurfaceColor() {
        return surfaceColor;
    }

    public static Color getSurfaceVariantColor() {
        return surfaceVariantColor;
    }

    private ColorUtil() {
    }

    public static Color averageColorFromImage(final BufferedImage bi, final int width, final int height, final int pixelStep) {
        final int[] color = new int[3];
        for (int x = 0; x < width; x += pixelStep) {
            for (int y = 0; y < height; y += pixelStep) {
                final Color pixel = new Color(bi.getRGB(x, y));
                color[0] += pixel.getRed();
                color[1] += pixel.getGreen();
                color[2] += pixel.getBlue();
            }
        }
        final int num = (width * height) / (pixelStep * pixelStep);
        return new Color(color[0] / num, color[1] / num, color[2] / num);
    }

    public static Color interpolateColorsBackAndForth(final int speed, final int index, final Color start, final Color end, final boolean trueColor) {
        int angle = (int) (((System.currentTimeMillis()) / speed + index) % 360);
        angle = (angle >= 180 ? 360 - angle : angle) * 2;
        return trueColor ? interpolateColorHue(start, end, angle / 360f) : interpolateColorC(start, end, angle / 360f);
    }

    public static Color interpolateColorHue(final Color color1, final Color color2, float amount) {
        amount = Math.min(1, Math.max(0, amount));

        final float[] color1HSB = Color.RGBtoHSB(color1.getRed(), color1.getGreen(), color1.getBlue(), null);
        final float[] color2HSB = Color.RGBtoHSB(color2.getRed(), color2.getGreen(), color2.getBlue(), null);

        final Color resultColor = Color.getHSBColor(MathUtil.interpolateFloat(color1HSB[0], color2HSB[0], amount),
                MathUtil.interpolateFloat(color1HSB[1], color2HSB[1], amount), MathUtil.interpolateFloat(color1HSB[2], color2HSB[2], amount));

        return ColorUtil.applyOpacity(resultColor, MathUtil.interpolateInt(color1.getAlpha(), color2.getAlpha(), amount) / 255f);
    }

    public static Color interpolateColorC(final Color color1, final Color color2, final float amount) {
        return new Color(MathUtil.interpolateInt(color1.getRed(), color2.getRed(), Math.min(1, Math.max(0, amount))),
                MathUtil.interpolateInt(color1.getGreen(), color2.getGreen(), Math.min(1, Math.max(0, amount))),
                MathUtil.interpolateInt(color1.getBlue(), color2.getBlue(), Math.min(1, Math.max(0, amount))),
                MathUtil.interpolateInt(color1.getAlpha(), color2.getAlpha(), Math.min(1, Math.max(0, amount))));
    }

    public static Color applyOpacity(final Color color, float opacity) {
        opacity = Math.min(1, Math.max(0, opacity));
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (color.getAlpha() * opacity));
    }

    public static void setColor(final int color, final float alpha) {
        final float r = (float) (color >> 16 & 255) / 255.0F;
        final float g = (float) (color >> 8 & 255) / 255.0F;
        final float b = (float) (color & 255) / 255.0F;
        GlStateManager.color(r, g, b, alpha);
    }

    public static void setColor(final Color color) {
        final float r = color.getRed();
        final float g = color.getGreen();
        final float b = color.getBlue();
        final float a = color.getAlpha();
        GlStateManager.color(r, g, b, a);
    }

    public static void setColor(final int color) {
        setColor(color, (float) (color >> 24 & 255) / 255.0F);
    }

    public static void resetColor() {
        GlStateManager.color(1, 1, 1, 1);
    }

}
