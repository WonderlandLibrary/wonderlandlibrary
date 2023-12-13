// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.utils.color;

import org.lwjgl.opengl.GL11;
import java.awt.Color;

public class ColorUtil
{
    public static float getClickGUIColor() {
        return 0.19607843f;
    }
    
    public static Color setColor(final int r, final int g, final int b, final int a) {
        final Color c = new Color(r, g, b, a);
        return c;
    }
    
    public static Color bw(final int bw, final int a) {
        final Color c = new Color(bw, bw, bw, a);
        return c;
    }
    
    public static Color black(final int a) {
        final Color c = new Color(0, 0, 0, a);
        return c;
    }
    
    public static Color white(final int a) {
        final Color c = new Color(255, 255, 255, a);
        return c;
    }
    
    public static void glColor(final Color color) {
        GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
    }
    
    public static void glColor(final int r, final int g, final int b, final int a) {
        GL11.glColor4f(r / 255.0f, g / 255.0f, b / 255.0f, a / 255.0f);
    }
    
    public static void resetglColor() {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public static int hsbcolor(final int h, final float s, final float b) {
        return Color.HSBtoRGB((float)h, s, b);
    }
    
    public static int[] rainbow(final int delay, final float saturation, final float brightness) {
        final double jump = 360.0f / delay;
        final int[] colors = new int[delay + 1];
        for (int i = 0; i < colors.length; ++i) {
            colors[i] = Color.HSBtoRGB((float)(i * jump), saturation, brightness);
        }
        return colors;
    }
    
    public static Color pulseBrightness(final Color color, final int index, final int count) {
        final float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        float brightness = Math.abs((System.currentTimeMillis() % 2000L / 1000.0f + index / (float)count * 2.0f) % 2.0f - 1.0f);
        brightness = 0.5f + 0.5f * brightness;
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], brightness % 2.0f));
    }
    
    public static Color pulseSaturation(final Color color, final int index, final int count) {
        final float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        float brightness = Math.abs((System.currentTimeMillis() % 2000L / 1000.0f + index / (float)count * 2.0f) % 2.0f - 1.0f);
        hsb[1] = (brightness *= 0.8f) % 2.0f;
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
    }
    
    public static String textColor(final String color) {
        if (color.equalsIgnoreCase("BLACK")) {
            return "§0";
        }
        if (color.equalsIgnoreCase("DBLUE")) {
            return "§1";
        }
        if (color.equalsIgnoreCase("DGREEN")) {
            return "§2";
        }
        if (color.equalsIgnoreCase("DAQUA")) {
            return "§3";
        }
        if (color.equalsIgnoreCase("DRED")) {
            return "§4";
        }
        if (color.equalsIgnoreCase("DPURPLE")) {
            return "§5";
        }
        if (color.equalsIgnoreCase("GOLD")) {
            return "§6";
        }
        if (color.equalsIgnoreCase("GRAY")) {
            return "§7";
        }
        if (color.equalsIgnoreCase("DGRAY")) {
            return "§8";
        }
        if (color.equalsIgnoreCase("BLUE")) {
            return "§9";
        }
        if (color.equalsIgnoreCase("GREEN")) {
            return "§a";
        }
        if (color.equalsIgnoreCase("AQUA")) {
            return "§b";
        }
        if (color.equalsIgnoreCase("RED")) {
            return "§c";
        }
        if (color.equalsIgnoreCase("LPURPLE")) {
            return "§d";
        }
        if (color.equalsIgnoreCase("YELLOW")) {
            return "§e";
        }
        if (color.equalsIgnoreCase("WHITE")) {
            return "§f";
        }
        return "";
    }
    
    public static int fadeBetween(final int color1, final int color2, float offset) {
        if (offset > 1.0f) {
            offset = 1.0f - offset % 1.0f;
        }
        final double invert = 1.0f - offset;
        final int r = (int)((color1 >> 16 & 0xFF) * invert + (color2 >> 16 & 0xFF) * offset);
        final int g = (int)((color1 >> 8 & 0xFF) * invert + (color2 >> 8 & 0xFF) * offset);
        final int b = (int)((color1 & 0xFF) * invert + (color2 & 0xFF) * offset);
        final int a = (int)((color1 >> 24 & 0xFF) * invert + (color2 >> 24 & 0xFF) * offset);
        return (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF);
    }
    
    public static int darker(final int color, final float factor) {
        final int r = (int)((color >> 16 & 0xFF) * factor);
        final int g = (int)((color >> 8 & 0xFF) * factor);
        final int b = (int)((color & 0xFF) * factor);
        final int a = color >> 24 & 0xFF;
        return (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF) | (a & 0xFF) << 24;
    }
}
