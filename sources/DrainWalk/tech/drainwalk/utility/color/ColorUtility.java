package tech.drainwalk.utility.color;


import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import tech.drainwalk.utility.Utility;
import tech.drainwalk.utility.math.MathUtility;

import java.awt.*;
import java.nio.ByteBuffer;

public class ColorUtility extends Utility {


    public static float[] getRGBAf(int color) {
        return new float[]{(color >> 16 & 0xFF) / 255.0f, (color >> 8 & 0xFF) / 255.0f, (color & 0xFF) / 255.0f,
                (color >> 24 & 0xFF) / 255.0f};
    }

    public static int getColorWithAlpha(int color, float alpha) {
        float[] rgb = getRGBAf(color);
        return rgba((int) (rgb[0] * 255.0f), (int) (rgb[1] * 255.0f), (int) (rgb[2] * 255.0f), (int) (alpha * 255.0f));
    }

    public static int interpolateColor(int color1, int color2, double offset) {
        float[] rgba1 = getRGBAf(color1);
        float[] rgba2 = getRGBAf(color2);
        double r = rgba1[0] + (rgba2[0] - rgba1[0]) * offset;
        double g = rgba1[1] + (rgba2[1] - rgba1[1]) * offset;
        double b = rgba1[2] + (rgba2[2] - rgba1[2]) * offset;
        double a = rgba1[3] + (rgba2[3] - rgba1[3]) * offset;
        return rgba((int) (r * 255.0f), (int) (g * 255.0f), (int) (b * 255.0f), (int) (a * 255.0f));
    }

    public static int rgba(int r, int g, int b, int a) {
        return a << 24 | r << 16 | g << 8 | b;
    }

    public static int rgb(int r, int g, int b) {
        return 255 << 24 | r << 16 | g << 8 | b;
    }

    public static int rgbaFloat(float r, float g, float b, float a) {
        return (int) (MathUtility.clamp(a,0,1) * 255) << 24 | (int) (MathUtility.clamp(r,0,1) * 255) << 16 | (int) (MathUtility.clamp(g,0,1) * 255) << 8 | (int) (MathUtility.clamp(b,0,1) * 255);
    }

    public static int rgbFloat(float r, float g, float b) {
        return (255) << 24 | (int) (MathUtility.clamp(r,0,1) * 255) << 16 | (int) (MathUtility.clamp(g,0,1) * 255) << 8 | (int) (MathUtility.clamp(b,0,1) * 255);
    }

    public static String RGBtoHEXString(int color) {
        return Integer.toHexString(color).substring(2);
    }

    public static int getColorFromPixel(int x, int y) {
        ByteBuffer rgb = BufferUtils.createByteBuffer(3);
        GL11.glReadPixels(x, y, 1, 1, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, rgb);
        return ColorUtility.rgb((rgb.get(0) & 255), (rgb.get(1) & 255), (rgb.get(2) & 255));
    }
    public static int HUEtoRGB(int value) {
        float hue = (value / 360f);
        return Color.HSBtoRGB(hue, 1, 1);
    }





}
