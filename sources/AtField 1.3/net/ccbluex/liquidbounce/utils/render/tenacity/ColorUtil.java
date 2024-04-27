/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.render.tenacity;

import java.awt.Color;
import java.awt.image.BufferedImage;
import net.ccbluex.liquidbounce.utils.render.tenacity.MathUtils;

public class ColorUtil {
    public static Color hslToRGB(float[] fArray) {
        float f;
        float f2;
        float f3;
        if (fArray[1] == 0.0f) {
            f3 = 1.0f;
            f2 = 1.0f;
            f = 1.0f;
        } else {
            float f4 = (double)fArray[2] < 0.5 ? fArray[2] * (1.0f + fArray[1]) : fArray[2] + fArray[1] - fArray[2] * fArray[1];
            float f5 = 2.0f * fArray[2] - f4;
            f = ColorUtil.hueToRGB(f5, f4, fArray[0] + 0.33333334f);
            f2 = ColorUtil.hueToRGB(f5, f4, fArray[0]);
            f3 = ColorUtil.hueToRGB(f5, f4, fArray[0] - 0.33333334f);
        }
        return new Color((int)(f *= 255.0f), (int)(f2 *= 255.0f), (int)(f3 *= 255.0f));
    }

    public static Color[] getAnalogousColor(Color color) {
        Color[] colorArray = new Color[2];
        float[] fArray = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        float f = 0.083333336f;
        float f2 = fArray[0] + f;
        colorArray[0] = new Color(Color.HSBtoRGB(f2, fArray[1], fArray[2]));
        float f3 = fArray[0] - f;
        colorArray[1] = new Color(Color.HSBtoRGB(f3, fArray[1], fArray[2]));
        return colorArray;
    }

    private static float getAnimationEquation(int n, int n2) {
        int n3 = (int)((System.currentTimeMillis() / (long)n2 + (long)n) % 360L);
        return (float)((n3 > 180 ? 360 - n3 : n3) + 180) / 360.0f;
    }

    public static int[] createColorArray(int n) {
        return new int[]{ColorUtil.bitChangeColor(n, 16), ColorUtil.bitChangeColor(n, 8), ColorUtil.bitChangeColor(n, 0), ColorUtil.bitChangeColor(n, 24)};
    }

    public static int getOppositeColor(int n) {
        int n2 = ColorUtil.bitChangeColor(n, 0);
        int n3 = ColorUtil.bitChangeColor(n, 8);
        int n4 = ColorUtil.bitChangeColor(n, 16);
        int n5 = ColorUtil.bitChangeColor(n, 24);
        n2 = 255 - n2;
        n3 = 255 - n3;
        n4 = 255 - n4;
        return n2 + (n3 << 8) + (n4 << 16) + (n5 << 24);
    }

    public static Color imitateTransparency(Color color, Color color2, float f) {
        return new Color(ColorUtil.interpolateColor(color, color2, 255.0f * f / 255.0f));
    }

    public static Color averageColor(BufferedImage bufferedImage, int n, int n2, int n3) {
        int n4;
        int[] nArray = new int[3];
        for (n4 = 0; n4 < n; n4 += n3) {
            for (int i = 0; i < n2; i += n3) {
                Color color = new Color(bufferedImage.getRGB(n4, i));
                nArray[0] = nArray[0] + color.getRed();
                nArray[1] = nArray[1] + color.getGreen();
                nArray[2] = nArray[2] + color.getBlue();
            }
        }
        n4 = n * n2 / (n3 * n3);
        return new Color(nArray[0] / n4, nArray[1] / n4, nArray[2] / n4);
    }

    public static int applyOpacity(int n, float f) {
        Color color = new Color(n);
        return ColorUtil.applyOpacity(color, f).getRGB();
    }

    public static Color brighter(Color color, float f) {
        int n = color.getRed();
        int n2 = color.getGreen();
        int n3 = color.getBlue();
        int n4 = color.getAlpha();
        int n5 = (int)(1.0 / (1.0 - (double)f));
        if (n == 0 && n2 == 0 && n3 == 0) {
            return new Color(n5, n5, n5, n4);
        }
        if (n > 0 && n < n5) {
            n = n5;
        }
        if (n2 > 0 && n2 < n5) {
            n2 = n5;
        }
        if (n3 > 0 && n3 < n5) {
            n3 = n5;
        }
        return new Color(Math.min((int)((float)n / f), 255), Math.min((int)((float)n2 / f), 255), Math.min((int)((float)n3 / f), 255), n4);
    }

    public static float hueToRGB(float f, float f2, float f3) {
        float f4 = f3;
        if (f4 < 0.0f) {
            f4 += 1.0f;
        }
        if (f4 > 1.0f) {
            f4 -= 1.0f;
        }
        if (f4 < 0.16666667f) {
            return f + (f2 - f) * 6.0f * f4;
        }
        if (f4 < 0.5f) {
            return f2;
        }
        if (f4 < 0.6666667f) {
            return f + (f2 - f) * (0.6666667f - f4) * 6.0f;
        }
        return f;
    }

    public static Color fade(int n, int n2, Color color, float f) {
        float[] fArray = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        int n3 = (int)((System.currentTimeMillis() / (long)n + (long)n2) % 360L);
        n3 = (n3 > 180 ? 360 - n3 : n3) + 180;
        Color color2 = new Color(Color.HSBtoRGB(fArray[0], fArray[1], (float)n3 / 360.0f));
        return new Color(color2.getRed(), color2.getGreen(), color2.getBlue(), Math.max(0, Math.min(255, (int)(f * 255.0f))));
    }

    private static int bitChangeColor(int n, int n2) {
        return n >> n2 & 0xFF;
    }

    public static int interpolateColor(Color color, Color color2, float f) {
        f = Math.min(1.0f, Math.max(0.0f, f));
        return ColorUtil.interpolateColorC(color, color2, f).getRGB();
    }

    public static float[] rgbToHSL(Color color) {
        float f = (float)color.getRed() / 255.0f;
        float f2 = (float)color.getGreen() / 255.0f;
        float f3 = (float)color.getBlue() / 255.0f;
        float f4 = Math.max(Math.max(f, f2), f3);
        float f5 = Math.min(Math.min(f, f2), f3);
        float f6 = (f4 + f5) / 2.0f;
        float[] fArray = new float[]{f6, f6, f6};
        if (f4 == f5) {
            fArray[1] = 0.0f;
            fArray[0] = 0.0f;
        } else {
            float f7 = f4 - f5;
            float f8 = fArray[1] = (double)fArray[2] > 0.5 ? f7 / (2.0f - f4 - f5) : f7 / (f4 + f5);
            if (f4 == f) {
                fArray[0] = (f2 - f3) / f7 + (float)(f2 < f3 ? 6 : 0);
            } else if (f4 == f3) {
                fArray[0] = (f3 - f) / f7 + 2.0f;
            } else if (f4 == f2) {
                fArray[0] = (f - f2) / f7 + 4.0f;
            }
            fArray[0] = fArray[0] / 6.0f;
        }
        return fArray;
    }

    public static Color darker(Color color, float f) {
        return new Color(Math.max((int)((float)color.getRed() * f), 0), Math.max((int)((float)color.getGreen() * f), 0), Math.max((int)((float)color.getBlue() * f), 0), color.getAlpha());
    }

    public static Color interpolateColorsBackAndForth(int n, int n2, Color color, Color color2, boolean bl) {
        int n3 = (int)((System.currentTimeMillis() / (long)n + (long)n2) % 360L);
        n3 = (n3 >= 180 ? 360 - n3 : n3) * 2;
        return bl ? ColorUtil.interpolateColorHue(color, color2, (float)n3 / 360.0f) : ColorUtil.interpolateColorC(color, color2, (float)n3 / 360.0f);
    }

    public static Color rainbow(int n, int n2, float f, float f2, float f3) {
        int n3 = (int)((System.currentTimeMillis() / (long)n + (long)n2) % 360L);
        float f4 = (float)n3 / 360.0f;
        Color color = new Color(Color.HSBtoRGB(f4, f, f2));
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.max(0, Math.min(255, (int)(f3 * 255.0f))));
    }

    public static int interpolateColor(int n, int n2, float f) {
        f = Math.min(1.0f, Math.max(0.0f, f));
        Color color = new Color(n);
        Color color2 = new Color(n2);
        return ColorUtil.interpolateColorC(color, color2, f).getRGB();
    }

    public static Color interpolateColorHue(Color color, Color color2, float f) {
        f = Math.min(1.0f, Math.max(0.0f, f));
        float[] fArray = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        float[] fArray2 = Color.RGBtoHSB(color2.getRed(), color2.getGreen(), color2.getBlue(), null);
        Color color3 = Color.getHSBColor(MathUtils.interpolateFloat(fArray[0], fArray2[0], f), MathUtils.interpolateFloat(fArray[1], fArray2[1], f), MathUtils.interpolateFloat(fArray[2], fArray2[2], f));
        return new Color(color3.getRed(), color3.getGreen(), color3.getBlue(), MathUtils.interpolateInt(color.getAlpha(), color2.getAlpha(), f));
    }

    public static Color interpolateColorC(Color color, Color color2, float f) {
        f = Math.min(1.0f, Math.max(0.0f, f));
        return new Color(MathUtils.interpolateInt(color.getRed(), color2.getRed(), f), MathUtils.interpolateInt(color.getGreen(), color2.getGreen(), f), MathUtils.interpolateInt(color.getBlue(), color2.getBlue(), f), MathUtils.interpolateInt(color.getAlpha(), color2.getAlpha(), f));
    }

    public static Color applyOpacity(Color color, float f) {
        f = Math.min(1.0f, Math.max(0.0f, f));
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)((float)color.getAlpha() * f));
    }
}

