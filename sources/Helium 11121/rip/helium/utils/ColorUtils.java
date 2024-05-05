package rip.helium.utils;

import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ColorUtils {
    public static int rainbow(int delay) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0D);
        return Color.getHSBColor((float) ((rainbowState %= 360.0D) / 360.0D), 0.8F, 0.7F).getRGB();
    }

    public static Color getRGB(int speed, int offset) {
        return getRGB(speed, offset, System.currentTimeMillis());
    }

    public static Color getRGB(int speed, int offset, long time) {
        return getRGB(speed, offset, time, 1.0F);
    }

    public static Color getRGB(int speed, int offset, long time, float s) {
        float hue = (float) ((time + offset) % speed);
        return Color.getHSBColor(hue /= speed, s, 1.0F);
    }

    public static Color addAlpha(Color c2, int alpha) {
        return new Color(c2.getRed(), c2.getGreen(), c2.getBlue(), alpha);
    }

    public static Color getRainbow(int speed, int offset) {
        float hue = (float) ((System.currentTimeMillis() + offset) % speed);
        return Color.getHSBColor(hue /= speed, 0.6F, 1.0F);
    }

    public static Color glColor(int color, float alpha) {
        int hex = color;
        float red = (hex >> 16 & 0xFF) / 255.0F;
        float green = (hex >> 8 & 0xFF) / 255.0F;
        float blue = (hex & 0xFF) / 255.0F;
        GL11.glColor4f(red, green, blue, alpha);
        return new Color(red, green, blue, alpha);
    }

    public static Color glColor(int hex) {
        float alpha = (hex >> 24 & 0xFF) / 256.0F;
        float red = (hex >> 16 & 0xFF) / 255.0F;
        float green = (hex >> 8 & 0xFF) / 255.0F;
        float blue = (hex & 0xFF) / 255.0F;
        GL11.glColor4f(red, green, blue, alpha);
        return new Color(red, green, blue, alpha);
    }

    public static Color blendColors(float[] fractions, Color[] colors, float progress) {
        return null;
    }

    public void glColor(Color color) {
        GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color
                .getAlpha() / 255.0F);
    }

    public Color glColor(float alpha, int redRGB, int greenRGB, int blueRGB) {
        float red = 0.003921569F * redRGB;
        float green = 0.003921569F * greenRGB;
        float blue = 0.003921569F * blueRGB;
        GL11.glColor4f(red, green, blue, alpha);
        return new Color(red, green, blue, alpha);
    }
}
