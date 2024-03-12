// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.render;

import dev.tenacity.utils.misc.MathUtils;
import java.awt.image.BufferedImage;
import java.awt.Color;

public class ColorUtil
{
    public static Color tripleColor(final int rgbValue) {
        return tripleColor(rgbValue, 1.0f);
    }
    
    public static Color tripleColor(final int rgbValue, float alpha) {
        alpha = Math.min(1.0f, Math.max(0.0f, alpha));
        return new Color(rgbValue, rgbValue, rgbValue, (int)(255.0f * alpha));
    }
    
    public static Color blendColors(final float[] fractions, final Color[] colors, final float progress) {
        int startPoint = 0;
        final int[] indices = new int[2];
        while (startPoint < fractions.length && fractions[startPoint] <= progress) {
            ++startPoint;
        }
        if (startPoint >= fractions.length) {
            startPoint = fractions.length - 1;
        }
        indices[0] = startPoint - 1;
        indices[1] = startPoint;
        final float[] range = { fractions[indices[0]], fractions[indices[1]] };
        final Color[] colorRange = { colors[indices[0]], colors[indices[1]] };
        final float max = range[1] - range[0];
        final float value = progress - range[0];
        final float weight = value / max;
        final float r = 1.0f - weight;
        final float ir = 1.0f - r;
        final float[] rgb1 = new float[3];
        final float[] rgb2 = new float[3];
        colorRange[0].getColorComponents(rgb1);
        colorRange[1].getColorComponents(rgb2);
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
        return new Color(red, green, blue);
    }
    
    public static Color[] getAnalogousColor(final Color color) {
        final Color[] colors = new Color[2];
        final float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        final float degree = 0.083333336f;
        final float newHueAdded = hsb[0] + degree;
        colors[0] = new Color(Color.HSBtoRGB(newHueAdded, hsb[1], hsb[2]));
        final float newHueSubtracted = hsb[0] - degree;
        colors[1] = new Color(Color.HSBtoRGB(newHueSubtracted, hsb[1], hsb[2]));
        return colors;
    }
    
    public static Color getRandomColor() {
        return new Color(Color.HSBtoRGB((float)Math.random(), (float)(0.5 + Math.random() / 2.0), (float)(0.5 + Math.random() / 2.0)));
    }
    
    public static Color hslToRGB(final float[] hsl) {
        float blue;
        float red;
        float green;
        if (hsl[1] == 0.0f) {
            green = (red = (blue = 1.0f));
        }
        else {
            final float q = (hsl[2] < 0.5) ? (hsl[2] * (1.0f + hsl[1])) : (hsl[2] + hsl[1] - hsl[2] * hsl[1]);
            final float p = 2.0f * hsl[2] - q;
            red = hueToRGB(p, q, hsl[0] + 0.33333334f);
            green = hueToRGB(p, q, hsl[0]);
            blue = hueToRGB(p, q, hsl[0] - 0.33333334f);
        }
        red *= 255.0f;
        green *= 255.0f;
        blue *= 255.0f;
        return new Color((int)red, (int)green, (int)blue);
    }
    
    public static float hueToRGB(final float p, final float q, final float t) {
        float newT = t;
        if (newT < 0.0f) {
            ++newT;
        }
        if (newT > 1.0f) {
            --newT;
        }
        if (newT < 0.16666667f) {
            return p + (q - p) * 6.0f * newT;
        }
        if (newT < 0.5f) {
            return q;
        }
        if (newT < 0.6666667f) {
            return p + (q - p) * (0.6666667f - newT) * 6.0f;
        }
        return p;
    }
    
    public static float[] rgbToHSL(final Color rgb) {
        final float red = rgb.getRed() / 255.0f;
        final float green = rgb.getGreen() / 255.0f;
        final float blue = rgb.getBlue() / 255.0f;
        final float max = Math.max(Math.max(red, green), blue);
        final float min = Math.min(Math.min(red, green), blue);
        final float c = (max + min) / 2.0f;
        final float[] hsl = { c, c, c };
        if (max == min) {
            hsl[0] = (hsl[1] = 0.0f);
        }
        else {
            final float d = max - min;
            hsl[1] = ((hsl[2] > 0.5) ? (d / (2.0f - max - min)) : (d / (max + min)));
            if (max == red) {
                hsl[0] = (green - blue) / d + ((green < blue) ? 6 : 0);
            }
            else if (max == blue) {
                hsl[0] = (blue - red) / d + 2.0f;
            }
            else if (max == green) {
                hsl[0] = (red - green) / d + 4.0f;
            }
            final float[] array = hsl;
            final int n = 0;
            array[n] /= 6.0f;
        }
        return hsl;
    }
    
    public static Color imitateTransparency(final Color backgroundColor, final Color accentColor, final float percentage) {
        return new Color(interpolateColor(backgroundColor, accentColor, 255.0f * percentage / 255.0f));
    }
    
    public static int applyOpacity(final int color, final float opacity) {
        final Color old = new Color(color);
        return applyOpacity(old, opacity).getRGB();
    }
    
    public static Color applyOpacity(final Color color, float opacity) {
        opacity = Math.min(1.0f, Math.max(0.0f, opacity));
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(color.getAlpha() * opacity));
    }
    
    public static Color darker(final Color color, final float FACTOR) {
        return new Color(Math.max((int)(color.getRed() * FACTOR), 0), Math.max((int)(color.getGreen() * FACTOR), 0), Math.max((int)(color.getBlue() * FACTOR), 0), color.getAlpha());
    }
    
    public static Color brighter(final Color color, final float FACTOR) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        final int alpha = color.getAlpha();
        final int i = (int)(1.0 / (1.0 - FACTOR));
        if (r == 0 && g == 0 && b == 0) {
            return new Color(i, i, i, alpha);
        }
        if (r > 0 && r < i) {
            r = i;
        }
        if (g > 0 && g < i) {
            g = i;
        }
        if (b > 0 && b < i) {
            b = i;
        }
        return new Color(Math.min((int)(r / FACTOR), 255), Math.min((int)(g / FACTOR), 255), Math.min((int)(b / FACTOR), 255), alpha);
    }
    
    public static Color averageColor(final BufferedImage bi, final int width, final int height, final int pixelStep) {
        final int[] color = new int[3];
        for (int x = 0; x < width; x += pixelStep) {
            for (int y = 0; y < height; y += pixelStep) {
                final Color pixel = new Color(bi.getRGB(x, y));
                final int[] array = color;
                final int n = 0;
                array[n] += pixel.getRed();
                final int[] array2 = color;
                final int n2 = 1;
                array2[n2] += pixel.getGreen();
                final int[] array3 = color;
                final int n3 = 2;
                array3[n3] += pixel.getBlue();
            }
        }
        final int num = width * height / (pixelStep * pixelStep);
        return new Color(color[0] / num, color[1] / num, color[2] / num);
    }
    
    public static Color rainbow(final int speed, final int index, final float saturation, final float brightness, final float opacity) {
        final int angle = (int)((System.currentTimeMillis() / speed + index) % 360L);
        final float hue = angle / 360.0f;
        final Color color = new Color(Color.HSBtoRGB(hue, saturation, brightness));
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.max(0, Math.min(255, (int)(opacity * 255.0f))));
    }
    
    public static Color interpolateColorsBackAndForth(final int speed, final int index, final Color start, final Color end, final boolean trueColor) {
        int angle = (int)((System.currentTimeMillis() / speed + index) % 360L);
        angle = ((angle >= 180) ? (360 - angle) : angle) * 2;
        return trueColor ? interpolateColorHue(start, end, angle / 360.0f) : interpolateColorC(start, end, angle / 360.0f);
    }
    
    public static int interpolateColor(final Color color1, final Color color2, float amount) {
        amount = Math.min(1.0f, Math.max(0.0f, amount));
        return interpolateColorC(color1, color2, amount).getRGB();
    }
    
    public static int interpolateColor(final int color1, final int color2, float amount) {
        amount = Math.min(1.0f, Math.max(0.0f, amount));
        final Color cColor1 = new Color(color1);
        final Color cColor2 = new Color(color2);
        return interpolateColorC(cColor1, cColor2, amount).getRGB();
    }
    
    public static Color interpolateColorC(final Color color1, final Color color2, float amount) {
        amount = Math.min(1.0f, Math.max(0.0f, amount));
        return new Color(MathUtils.interpolateInt(color1.getRed(), color2.getRed(), amount), MathUtils.interpolateInt(color1.getGreen(), color2.getGreen(), amount), MathUtils.interpolateInt(color1.getBlue(), color2.getBlue(), amount), MathUtils.interpolateInt(color1.getAlpha(), color2.getAlpha(), amount));
    }
    
    public static Color interpolateColorHue(final Color color1, final Color color2, float amount) {
        amount = Math.min(1.0f, Math.max(0.0f, amount));
        final float[] color1HSB = Color.RGBtoHSB(color1.getRed(), color1.getGreen(), color1.getBlue(), null);
        final float[] color2HSB = Color.RGBtoHSB(color2.getRed(), color2.getGreen(), color2.getBlue(), null);
        final Color resultColor = Color.getHSBColor(MathUtils.interpolateFloat(color1HSB[0], color2HSB[0], amount), MathUtils.interpolateFloat(color1HSB[1], color2HSB[1], amount), MathUtils.interpolateFloat(color1HSB[2], color2HSB[2], amount));
        return applyOpacity(resultColor, MathUtils.interpolateInt(color1.getAlpha(), color2.getAlpha(), amount) / 255.0f);
    }
    
    public static Color fade(final int speed, final int index, final Color color, final float alpha) {
        final float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        int angle = (int)((System.currentTimeMillis() / speed + index) % 360L);
        angle = ((angle > 180) ? (360 - angle) : angle) + 180;
        final Color colorHSB = new Color(Color.HSBtoRGB(hsb[0], hsb[1], angle / 360.0f));
        return new Color(colorHSB.getRed(), colorHSB.getGreen(), colorHSB.getBlue(), Math.max(0, Math.min(255, (int)(alpha * 255.0f))));
    }
    
    private static float getAnimationEquation(final int index, final int speed) {
        final int angle = (int)((System.currentTimeMillis() / speed + index) % 360L);
        return (((angle > 180) ? (360 - angle) : angle) + 180) / 360.0f;
    }
    
    public static int[] createColorArray(final int color) {
        return new int[] { bitChangeColor(color, 16), bitChangeColor(color, 8), bitChangeColor(color, 0), bitChangeColor(color, 24) };
    }
    
    public static int getOppositeColor(final int color) {
        int R = bitChangeColor(color, 0);
        int G = bitChangeColor(color, 8);
        int B = bitChangeColor(color, 16);
        final int A = bitChangeColor(color, 24);
        R = 255 - R;
        G = 255 - G;
        B = 255 - B;
        return R + (G << 8) + (B << 16) + (A << 24);
    }
    
    public static Color getOppositeColor(final Color color) {
        return new Color(getOppositeColor(color.getRGB()));
    }
    
    private static int bitChangeColor(final int color, final int bitChange) {
        return color >> bitChange & 0xFF;
    }
}
