package slavikcodd3r.rainbow.utils;

import org.lwjgl.opengl.GL11;
import java.awt.Color;

public class ColorUtils
{
    public static int rainbow(final int delay) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
        return Color.getHSBColor((float)((rainbowState %= 360.0) / 360.0), 0.8f, 0.7f).getRGB();
    }
    
    public static Color getRGB(final int speed, final int offset) {
        return getRGB(speed, offset, System.currentTimeMillis());
    }
    
    public static Color getRGB(final int speed, final int offset, final long time) {
        return getRGB(speed, offset, time, 1.0f);
    }
    
    public static Color getRGB(final int speed, final int offset, final long time, final float s) {
        float hue = (float)((time + offset) % speed);
        return Color.getHSBColor(hue /= speed, s, 1.0f);
    }
    
    public static Color addAlpha(final Color c2, final int alpha) {
        return new Color(c2.getRed(), c2.getGreen(), c2.getBlue(), alpha);
    }
    
    public static Color getRainbow(final int speed, final int offset) {
        float hue = (float)((System.currentTimeMillis() + offset) % speed);
        return Color.getHSBColor(hue /= speed, 0.6f, 1.0f);
    }
    
    public static Color glColor(final int color, final float alpha) {
        final int hex = color;
        final float red = (hex >> 16 & 0xFF) / 255.0f;
        final float green = (hex >> 8 & 0xFF) / 255.0f;
        final float blue = (hex & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
        return new Color(red, green, blue, alpha);
    }
    
    public void glColor(final Color color) {
        GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
    }
    
    public static Color glColor(final int hex) {
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
    
    public static Color blendColors(final float[] fractions, final Color[] colors, final float progress) {
        return null;
    }
}
