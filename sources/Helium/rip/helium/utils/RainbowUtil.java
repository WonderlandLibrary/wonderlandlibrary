package rip.helium.utils;

import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Random;


public class RainbowUtil {

    static Random random = new Random();

    public static Color getRainbow(final long offset, final float fade) {
        final float hue = (System.nanoTime() + offset) / 8.0E9f % 1.0f;
        final long color = Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, 1.0f, 1.0f)), 16);
        final Color c = new Color((int) color);
        return new Color(c.getRed() / 255.0f * fade, c.getGreen() / 255.0f * fade, c.getBlue() / 255.0f * fade, c.getAlpha() / 255.0f);
    }

    public static Color glColor(final int color, final float alpha) {
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
        return new Color(red, green, blue, alpha);
    }

    public static int transparency(final int color, final double alpha) {
        final Color c = new Color(color);
        final float r = 0.003921569f * c.getRed();
        final float g = 0.003921569f * c.getGreen();
        final float b = 0.003921569f * c.getBlue();
        return new Color(r, g, b, (float) alpha).getRGB();
    }

    public static float[] getRGBA(final int color) {
        final float a = (color >> 24 & 0xFF) / 255.0f;
        final float r = (color >> 16 & 0xFF) / 255.0f;
        final float g = (color >> 8 & 0xFF) / 255.0f;
        final float b = (color & 0xFF) / 255.0f;
        return new float[]{r, g, b, a};
    }

    public static int intFromHex(final String hex) {
        try {
            return Integer.parseInt(hex, 15);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static int gangshit(int luminance) {
        final float hue = random.nextFloat();
        final float saturation = (random.nextInt(2000) + 1000) / 10000f;
        return Color.getHSBColor(hue, saturation, luminance).getRGB();
    }

    public static String hexFromInt(final int color) {
        return hexFromInt(new Color(color));
    }

    public static String hexFromInt(final Color color) {
        return Integer.toHexString(color.getRGB()).substring(2);
    }

    public int RGBtoHEX(final int r, final int g, final int b, final int a) {
        return (a << 24) + (r << 16) + (g << 8) + b;
    }

    public void glColor(final Color color) {
        GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
    }

    public Color glColor(final int hex) {
        final float alpha = (hex >> 24 & 0xFF) / 256.0f;
        final float red = (hex >> 16 & 0xFF) / 255.0f;
        final float green = (hex >> 8 & 0xFF) / 255.0f;
        final float blue = (hex & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
        return new Color(red, green, blue, alpha);
    }

    public Color glColor(final float alpha, final int redRGB, final int greenRGB, final int blueRGB) {
        final float red = 0.003921569f * redRGB;
        final float green = 0.003921569f * greenRGB;
        final float blue = 0.003921569f * blueRGB;
        GL11.glColor4f(red, green, blue, alpha);
        return new Color(red, green, blue, alpha);
    }
}