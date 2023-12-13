// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.utils.skid.rise5;

import com.ibm.icu.text.NumberFormat;
import java.awt.Color;
import java.util.regex.Pattern;

public final class ColorUtil
{
    private final Pattern COLOR_PATTERN;
    
    public ColorUtil() {
        this.COLOR_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]");
    }
    
    public Color liveColorBrighter(final Color c, final float factor) {
        return this.brighter(c, factor);
    }
    
    public Color liveColorDarker(final Color c, final float factor) {
        return this.darker(c, factor);
    }
    
    public Color brighter(final Color c, final float factor) {
        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();
        final int alpha = c.getAlpha();
        final int i = (int)(1.0 / (1.0 - factor));
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
        return new Color(Math.min((int)(r / factor), 255), Math.min((int)(g / factor), 255), Math.min((int)(b / factor), 255), alpha);
    }
    
    public Color darker(final Color c, final double FACTOR) {
        return new Color(Math.max((int)(c.getRed() * FACTOR), 0), Math.max((int)(c.getGreen() * FACTOR), 0), Math.max((int)(c.getBlue() * FACTOR), 0), c.getAlpha());
    }
    
    public static int getColor(final float hueoffset, final float saturation, final float brightness) {
        final float speed = 4500.0f;
        final float hue = System.currentTimeMillis() % 4500L / 4500.0f;
        return Color.HSBtoRGB(hue - hueoffset / 54.0f, saturation, brightness);
    }
    
    public static int getStaticColor(final float hueoffset, final float saturation, final float brightness) {
        return Color.HSBtoRGB(hueoffset / 54.0f, saturation, brightness);
    }
    
    public Color blend2colors(final Color color1, final Color color2, double offset) {
        final float hue = (float)System.currentTimeMillis();
        offset += hue;
        if (offset > 1.0) {
            final double left = offset % 1.0;
            final int off = (int)offset;
            offset = ((off % 2 == 0) ? left : (1.0 - left));
        }
        final double inversePercent = 1.0 - offset;
        final int redPart = (int)(color1.getRed() * inversePercent + color2.getRed() * offset);
        final int greenPart = (int)(color1.getGreen() * inversePercent + color2.getGreen() * offset);
        final int bluePart = (int)(color1.getBlue() * inversePercent + color2.getBlue() * offset);
        return new Color(redPart, greenPart, bluePart);
    }
    
    public static int getRainbow() {
        final float hue = System.currentTimeMillis() % 10000L / 10000.0f;
        return Color.HSBtoRGB(hue, 0.5f, 1.0f);
    }
    
    public String stripColor(final String text) {
        return this.COLOR_PATTERN.matcher(text).replaceAll("");
    }
    
    public Color mixColors(final Color color1, final Color color2, final double percent) {
        final double inverse_percent = 1.0 - percent;
        final int redPart = (int)(color1.getRed() * percent + color2.getRed() * inverse_percent);
        final int greenPart = (int)(color1.getGreen() * percent + color2.getGreen() * inverse_percent);
        final int bluePart = (int)(color1.getBlue() * percent + color2.getBlue() * inverse_percent);
        return new Color(redPart, greenPart, bluePart);
    }
    
    public static Color blendColors(final float[] fractions, final Color[] colors, final float progress) {
        if (fractions == null) {
            throw new IllegalArgumentException("Fractions can't be null");
        }
        if (colors == null) {
            throw new IllegalArgumentException("Colours can't be null");
        }
        if (fractions.length == colors.length) {
            final int[] getFractionBlack = getFraction(fractions, progress);
            final float[] range = { fractions[getFractionBlack[0]], fractions[getFractionBlack[1]] };
            final Color[] colorRange = { colors[getFractionBlack[0]], colors[getFractionBlack[1]] };
            final float max = range[1] - range[0];
            final float value = progress - range[0];
            final float weight = value / max;
            return blend(colorRange[0], colorRange[1], 1.0f - weight);
        }
        throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
    }
    
    public static int[] getFraction(final float[] fractions, final float progress) {
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
        catch (final IllegalArgumentException exp) {
            final NumberFormat nf = NumberFormat.getNumberInstance();
            exp.printStackTrace();
        }
        return color3;
    }
    
    public static String getColor(final int n) {
        if (n != 1) {
            if (n == 2) {
                return "§a";
            }
            if (n == 3) {
                return "§3";
            }
            if (n == 4) {
                return "§4";
            }
            if (n >= 5) {
                return "§e";
            }
        }
        return "§f";
    }
}
