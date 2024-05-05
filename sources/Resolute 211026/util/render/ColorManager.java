// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.util.render;

import java.text.NumberFormat;
import vip.Resolute.util.animation.impl.Opacity;
import net.minecraft.entity.EntityLivingBase;
import java.awt.Color;
import java.util.function.Supplier;

public enum ColorManager
{
    BLUE(() -> new Color(116, 202, 255)), 
    NICE_BLUE(() -> new Color(116, 202, 255)), 
    DARK_PURPLE(() -> new Color(133, 46, 215)), 
    GREEN(() -> new Color(0, 255, 138)), 
    PURPLE(() -> new Color(198, 139, 255)), 
    WHITE(() -> Color.WHITE);
    
    private final Supplier<Color> colorSupplier;
    
    private ColorManager(final Supplier<Color> colorSupplier) {
        this.colorSupplier = colorSupplier;
    }
    
    public static Color fade(final Color color) {
        return fade(color, 2, 100);
    }
    
    public static Color flash(final Color color) {
        return flash(color, 2, 10);
    }
    
    public static Color fade(final Color color, final int index, final int count) {
        final float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        float brightness = Math.abs((System.currentTimeMillis() % 2000L / 1000.0f + index / (float)count * 2.0f) % 2.0f - 1.0f);
        brightness = 0.5f + 0.5f * brightness;
        hsb[2] = brightness % 2.0f;
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
    }
    
    public static Color flash(final Color color, final int index, final int count) {
        final float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        float brightness = Math.abs((System.currentTimeMillis() % 200L / 500.0f + index / (float)count * 2.0f) % 2.0f - 1.0f);
        brightness = 0.5f + 0.5f * brightness;
        hsb[2] = brightness % 2.0f;
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
    }
    
    public static Color rainbow(final int index, final double speed) {
        final int angle = (int)((System.currentTimeMillis() / speed + index) % 360.0);
        final float hue = angle / 360.0f;
        final int color = Color.HSBtoRGB(hue, 1.0f, 1.0f);
        return new Color(color);
    }
    
    public static Color rainbow(final int index, final int speed, final float saturation, final float brightness, final float opacity) {
        final int angle = (int)((System.currentTimeMillis() / speed + index) % 360L);
        final float hue = angle / 360.0f;
        final int color = Color.HSBtoRGB(hue, saturation, brightness);
        final Color obj = new Color(color);
        return new Color(obj.getRed(), obj.getGreen(), obj.getBlue(), Math.max(0, Math.min(255, (int)(opacity * 255.0f))));
    }
    
    public static Color astolfo(final int index, final int speed, final float saturation, final float brightness, final float opacity) {
        int angle = (int)((System.currentTimeMillis() / speed + index) % 360L);
        angle = ((angle > 180) ? (360 - angle) : angle) + 180;
        final float hue = angle / 360.0f;
        final int color = Color.HSBtoRGB(brightness, saturation, hue);
        final Color obj = new Color(color);
        return new Color(obj.getRed(), obj.getGreen(), obj.getBlue(), Math.max(0, Math.min(255, (int)(opacity * 255.0f))));
    }
    
    public static int getAstoGay(final int delay, final float offset) {
        final int yStart = 20;
        final float speed = 3000.0f;
        final float index = 0.3f;
        float hue;
        for (hue = System.currentTimeMillis() % delay + offset; hue > speed; hue -= speed) {}
        hue /= speed;
        if (hue > 0.5) {
            hue = 0.5f - (hue - 0.5f);
        }
        hue += 0.5f;
        return Color.HSBtoRGB(hue, 0.5f, 1.0f);
    }
    
    public static Color getAstoGayColor(final int delay, final float offset) {
        final int yStart = 20;
        final float speed = 3000.0f;
        final float index = 0.3f;
        float hue;
        for (hue = System.currentTimeMillis() % delay + offset; hue > speed; hue -= speed) {}
        hue /= speed;
        if (hue > 0.5) {
            hue = 0.5f - (hue - 0.5f);
        }
        hue += 0.5f;
        return Color.getHSBColor(hue, 0.5f, 1.0f);
    }
    
    public static Color getHealthColor(final EntityLivingBase entityLivingBase) {
        final float health = entityLivingBase.getHealth();
        final float[] fractions = { 0.0f, 0.15f, 0.55f, 0.7f, 0.9f };
        final Color[] colors = { new Color(133, 0, 0), Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN };
        final float progress = health / entityLivingBase.getMaxHealth();
        return (health >= 0.0f) ? blendColors(fractions, colors, progress).brighter() : colors[0];
    }
    
    public static Color blendColors(final float[] fractions, final Color[] colors, final float progress) {
        final Opacity color = null;
        if (fractions == null) {
            throw new IllegalArgumentException("Fractions can't be null");
        }
        if (colors == null) {
            throw new IllegalArgumentException("Colours can't be null");
        }
        if (fractions.length != colors.length) {
            throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
        }
        final int[] indicies = getFractionIndicies(fractions, progress);
        final float[] range = { fractions[indicies[0]], fractions[indicies[1]] };
        final Color[] colorRange = { colors[indicies[0]], colors[indicies[1]] };
        final float max = range[1] - range[0];
        final float value = progress - range[0];
        final float weight = value / max;
        return blend(colorRange[0], colorRange[1], 1.0f - weight);
    }
    
    public static int[] getFractionIndicies(final float[] fractions, final float progress) {
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
    
    public static Color blend(final Color color1, final Color color2, final double ratio) {
        final float r = (float)ratio;
        final float ir = 1.0f - r;
        final float[] rgb1 = new float[3];
        final float[] rgb2 = new float[3];
        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);
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
        catch (IllegalArgumentException exp) {
            final NumberFormat nf = NumberFormat.getNumberInstance();
            System.out.println(nf.format(red) + "; " + nf.format(green) + "; " + nf.format(blue));
            exp.printStackTrace();
        }
        return color3;
    }
    
    public Color getColor() {
        return this.colorSupplier.get();
    }
}
