// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.color;

import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.GlStateManager;
import java.awt.Color;

public class ColorUtil
{
    public static void setColor(final Color color, final float alpha) {
        final float red = color.getRed() / 255.0f;
        final float green = color.getGreen() / 255.0f;
        final float blue = color.getBlue() / 255.0f;
        GlStateManager.color(red, green, blue, alpha);
    }
    
    public static int rgba(final int r, final int g, final int b, final int a) {
        return a << 24 | r << 16 | g << 8 | b;
    }
    
    public static int rgba(final double r, final double g, final double b, final double a) {
        return rgba((int)r, (int)g, (int)b, (int)a);
    }
    
    public static int getColor(final int red, final int green, final int blue, final int alpha) {
        int color = 0;
        color |= alpha << 24;
        color |= red << 16;
        color |= green << 8;
        return color |= blue;
    }
    
    public static int HSBtoRGB(final float hue, final float saturation, final float brightness) {
        int r = 0;
        int g = 0;
        int b = 0;
        if (saturation == 0.0f) {
            g = (r = (b = (int)(brightness * 255.0f + 0.5f)));
        }
        else {
            final float h = (hue - (float)Math.floor(hue)) * 6.0f;
            final float f = h - (float)Math.floor(h);
            final float p = brightness * (1.0f - saturation);
            final float q = brightness * (1.0f - saturation * f);
            final float t = brightness * (1.0f - saturation * (1.0f - f));
            switch ((int)h) {
                case 0: {
                    r = (int)(brightness * 255.0f + 0.5f);
                    g = (int)(t * 255.0f + 0.5f);
                    b = (int)(p * 255.0f + 0.5f);
                    break;
                }
                case 1: {
                    r = (int)(q * 255.0f + 0.5f);
                    g = (int)(brightness * 255.0f + 0.5f);
                    b = (int)(p * 255.0f + 0.5f);
                    break;
                }
                case 2: {
                    r = (int)(p * 255.0f + 0.5f);
                    g = (int)(brightness * 255.0f + 0.5f);
                    b = (int)(t * 255.0f + 0.5f);
                    break;
                }
                case 3: {
                    r = (int)(p * 255.0f + 0.5f);
                    g = (int)(q * 255.0f + 0.5f);
                    b = (int)(brightness * 255.0f + 0.5f);
                    break;
                }
                case 4: {
                    r = (int)(t * 255.0f + 0.5f);
                    g = (int)(p * 255.0f + 0.5f);
                    b = (int)(brightness * 255.0f + 0.5f);
                    break;
                }
                case 5: {
                    r = (int)(brightness * 255.0f + 0.5f);
                    g = (int)(p * 255.0f + 0.5f);
                    b = (int)(q * 255.0f + 0.5f);
                    break;
                }
            }
        }
        return 0xFF000000 | r << 16 | g << 8 | b;
    }
    
    public static int getColor(final int bright) {
        return getColor(bright, bright, bright, 255);
    }
    
    public static String getHealthStr(final EntityLivingBase entity) {
        String str = "";
        final int health = (int)entity.getHealth();
        if (health <= entity.getMaxHealth() * 0.25) {
            str = "§4";
        }
        else if (health <= entity.getMaxHealth() * 0.5) {
            str = "§6";
        }
        else if (health <= entity.getMaxHealth() * 0.75) {
            str = "§e";
        }
        else if (health <= entity.getMaxHealth()) {
            str = "§a";
        }
        return str;
    }
    
    public static void setColor(final double red, final double green, final double blue, final double alpha) {
        GL11.glColor4d(red, green, blue, alpha);
    }
    
    public static int setAlpha(final int color, final int alpha) {
        final Color c = new Color(color);
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha).getRGB();
    }
    
    public static Color fade(final int speed, final int index, final Color color, final float alpha) {
        final float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        int angle = (int)((System.currentTimeMillis() / speed + index) % 360L);
        angle = ((angle > 180) ? (360 - angle) : angle) + 180;
        final Color colorHSB = new Color(Color.HSBtoRGB(hsb[0], hsb[1], angle / 360.0f));
        return new Color(colorHSB.getRed(), colorHSB.getGreen(), colorHSB.getBlue(), Math.max(0, Math.min(255, (int)(alpha * 255.0f))));
    }
    
    public static Color TwoColorEffect(final Color cl1, final Color cl2, final double speed) {
        final double thing = speed / 4.0 % 1.0;
        final float val = MathHelper.clamp((float)Math.sin(18.84955592153876 * thing) / 2.0f + 0.5f, 0.0f, 1.0f);
        return new Color(lerp(cl1.getRed() / 255.0f, cl2.getRed() / 255.0f, val), lerp(cl1.getGreen() / 255.0f, cl2.getGreen() / 255.0f, val), lerp(cl1.getBlue() / 255.0f, cl2.getBlue() / 255.0f, val));
    }
    
    public static Color rainbow(final int speed, final int index, final float saturation, final float brightness, final float opacity) {
        final int angle = (int)((System.currentTimeMillis() / speed + index) % 360L);
        final float hue = angle / 360.0f;
        final Color color = new Color(Color.HSBtoRGB(hue, saturation, brightness));
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.max(0, Math.min(255, (int)(opacity * 255.0f))));
    }
    
    public static float lerp(final float a, final float b, final float f) {
        return a + f * (b - a);
    }
    
    public static void setColor(Color color) {
        if (color == null) {
            color = Color.white;
        }
        setColor(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
    }
    
    public static void setColor(final int color) {
        setColor(color, (color >> 24 & 0xFF) / 255.0f);
    }
    
    public static void setColor(final int color, final float alpha) {
        final float r = (color >> 16 & 0xFF) / 255.0f;
        final float g = (color >> 8 & 0xFF) / 255.0f;
        final float b = (color & 0xFF) / 255.0f;
        GlStateManager.color(r, g, b, alpha);
    }
    
    public static Color applyOpacity(final Color color, int alpha) {
        alpha = MathHelper.clamp(alpha, 0, 255);
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
    
    public static Color applyOpacity(final Color color, float opacity) {
        opacity = Math.min(1.0f, Math.max(0.0f, opacity));
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(color.getAlpha() * opacity));
    }
}
