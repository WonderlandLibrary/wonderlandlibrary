// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.util.render;

import net.minecraft.util.MathHelper;
import java.awt.Color;

public class ColorUtils
{
    public static final int WHITE;
    public static final int RED;
    public static final int PINK;
    public static final int PURPLE;
    public static final int DEEP_PURPLE;
    public static final int INDIGO;
    public static final int BLUE;
    public static final int LIGHT_BLUE;
    public static final int CYAN;
    public static final int TEAL;
    public static final int GREEN;
    
    public static Color rainbow(final float speed, final float off) {
        double time = System.currentTimeMillis() / (double)speed;
        time += off;
        time %= 255.0;
        return Color.getHSBColor((float)(time / 255.0), 1.0f, 1.0f);
    }
    
    public static Color getHealthColor(final float health, final float maxHealth) {
        final float[] fractions = { 0.0f, 0.5f, 1.0f };
        final Color[] colors = { new Color(108, 20, 20), new Color(255, 0, 60), Color.GREEN };
        final float progress = health / maxHealth;
        return blendColors(fractions, colors, progress).brighter();
    }
    
    public static Color blendColors(final float[] fractions, final Color[] colors, final float progress) {
        if (fractions.length == colors.length) {
            final int[] indices = getFractionIndices(fractions, progress);
            final float[] range = { fractions[indices[0]], fractions[indices[1]] };
            final Color[] colorRange = { colors[indices[0]], colors[indices[1]] };
            final float max = range[1] - range[0];
            final float value = progress - range[0];
            final float weight = value / max;
            final Color color = blend(colorRange[0], colorRange[1], 1.0f - weight);
            return color;
        }
        throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
    }
    
    public static Color blend(final Color color1, final Color color2, final double ratio) {
        final float r = (float)ratio;
        final float ir = 1.0f - r;
        final float[] rgb1 = color1.getColorComponents(new float[3]);
        final float[] rgb2 = color2.getColorComponents(new float[3]);
        float red = rgb1[0] * r + rgb2[0] * ir;
        float green = rgb1[1] * r + rgb2[1] * ir;
        float blue = rgb1[2] * r + rgb2[2] * ir;
        if (red < 0.0f) {
            red = 0.0f;
        }
        else if (red > 255.0f) {
            red = 255.0f;
        }
        if (green < 0.0f) {
            green = 0.0f;
        }
        else if (green > 255.0f) {
            green = 255.0f;
        }
        if (blue < 0.0f) {
            blue = 0.0f;
        }
        else if (blue > 255.0f) {
            blue = 255.0f;
        }
        Color color3 = null;
        try {
            color3 = new Color(red, green, blue);
        }
        catch (IllegalArgumentException ex) {}
        return color3;
    }
    
    public static int[] getFractionIndices(final float[] fractions, final float progress) {
        final int[] range = new int[2];
        int startPoint;
        for (startPoint = 0; startPoint < fractions.length && fractions[startPoint] <= progress; ++startPoint) {}
        if (startPoint >= fractions.length) {
            startPoint = fractions.length - 1;
        }
        range[0] = startPoint - 1;
        range[1] = startPoint;
        return range;
    }
    
    public static int astolfoColors(final int yOffset, final int yTotal) {
        float speed;
        float hue;
        for (speed = 2900.0f, hue = System.currentTimeMillis() % (int)speed + (float)((yTotal - yOffset) * 9); hue > speed; hue -= speed) {}
        hue /= speed;
        if (hue > 0.5) {
            hue = 0.5f - (hue - 0.5f);
        }
        hue += 0.5f;
        return Color.HSBtoRGB(hue, 0.5f, 1.0f);
    }
    
    public static int moonColors(final int yOffset, final int yTotal) {
        float speed;
        float hue;
        for (speed = 2900.0f, hue = MathHelper.sin((float)System.currentTimeMillis()) % (int)speed + (yTotal - yOffset) * 9; hue > speed; hue -= speed) {}
        hue /= speed;
        if (hue > 1.0f) {
            hue = 1.0f - (hue - 1.0f);
        }
        ++hue;
        return Color.HSBtoRGB(hue, 0.5f, 1.0f);
    }
    
    public static int getRainbow(final float seconds, final float saturation, final float brightness) {
        final float hue = System.currentTimeMillis() % (int)(seconds * 1000.0f) / (seconds * 1000.0f);
        final int color = Color.HSBtoRGB(hue, saturation, brightness);
        return color;
    }
    
    public static int getRainbow(final float seconds, final float saturation, final float brightness, final long index) {
        final float hue = (System.currentTimeMillis() + index) % (int)(seconds * 1000.0f) / (seconds * 1000.0f);
        final int color = Color.HSBtoRGB(hue, saturation, brightness);
        return color;
    }
    
    public static Color colorLerpv2(final Color start, final Color end, float ratio) {
        ratio = Math.min(Math.max(ratio, 0.0f), 1.0f);
        final int red = (int)Math.abs(ratio * start.getRed() + (1.0f - ratio) * end.getRed());
        final int green = (int)Math.abs(ratio * start.getGreen() + (1.0f - ratio) * end.getGreen());
        final int blue = (int)Math.abs(ratio * start.getBlue() + (1.0f - ratio) * end.getBlue());
        return new Color(red, green, blue);
    }
    
    public static int rainbow(final int delay) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
        rainbowState %= 360.0;
        return Color.getHSBColor((float)(rainbowState / 360.0), 0.8f, 0.7f).getRGB();
    }
    
    public static Color pulseBrightness(final Color color, final int index, final int count) {
        final float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        float brightness = Math.abs((System.currentTimeMillis() % 2000L / 1000.0f + index / (float)count * 2.0f) % 2.0f - 1.0f);
        brightness = 0.5f + 0.5f * brightness;
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], brightness % 2.0f));
    }
    
    public static int getColor(final Color color) {
        return getColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }
    
    public static int getColor(final int brightness) {
        return getColor(brightness, brightness, brightness, 255);
    }
    
    public static int getColor(final int brightness, final int alpha) {
        return getColor(brightness, brightness, brightness, alpha);
    }
    
    public static int getColor(final int red, final int green, final int blue) {
        return getColor(red, green, blue, 255);
    }
    
    public static int getColor(final int red, final int green, final int blue, final int alpha) {
        int color = MathHelper.clamp_int(alpha, 0, 255) << 24;
        color |= MathHelper.clamp_int(red, 0, 255) << 16;
        color |= MathHelper.clamp_int(green, 0, 255) << 8;
        color |= MathHelper.clamp_int(blue, 0, 255);
        return color;
    }
    
    static {
        WHITE = Color.WHITE.getRGB();
        RED = new Color(16007990).getRGB();
        PINK = new Color(16744619).getRGB();
        PURPLE = new Color(12216520).getRGB();
        DEEP_PURPLE = new Color(8281781).getRGB();
        INDIGO = new Color(7964363).getRGB();
        BLUE = new Color(1668818).getRGB();
        LIGHT_BLUE = new Color(7652351).getRGB();
        CYAN = new Color(44225).getRGB();
        TEAL = new Color(11010027).getRGB();
        GREEN = new Color(65350).getRGB();
    }
}
