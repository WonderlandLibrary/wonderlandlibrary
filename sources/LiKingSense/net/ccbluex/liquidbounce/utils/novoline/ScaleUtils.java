/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.GlStateManager
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.utils.novoline;

import java.awt.Color;
import java.text.NumberFormat;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class ScaleUtils {
    static Color startColor = new Color(-393028);
    static Color endColor = new Color(-16718593);

    public static void drawOutline(float x, float y, float width, float height, float radius, float line, float offset) {
        double angleRadians;
        int i;
        ScaleUtils.enableRender2D();
        GL11.glLineWidth((float)line);
        GL11.glBegin((int)3);
        float edgeRadius = radius;
        float centerX = x + edgeRadius;
        float centerY = y + edgeRadius;
        int vertices = (int)Math.min(Math.max(edgeRadius, 10.0f), 90.0f);
        int colorI = 0;
        centerX = width;
        centerY = height + edgeRadius;
        vertices = (int)Math.min(Math.max(edgeRadius, 10.0f), 90.0f);
        for (i = 0; i <= vertices; ++i) {
            RenderUtils.setColor(ScaleUtils.fadeBetween(startColor.getRGB(), endColor.getRGB(), 20L * (long)colorI));
            angleRadians = Math.PI * 2 * (double)i / (double)(vertices * 4);
            GL11.glVertex2d((double)((double)centerX + Math.sin(angleRadians) * (double)edgeRadius), (double)((double)centerY + Math.cos(angleRadians) * (double)edgeRadius));
            ++colorI;
        }
        GL11.glEnd();
        GL11.glLineWidth((float)line);
        GL11.glBegin((int)3);
        centerX = width + edgeRadius;
        centerY = height + edgeRadius;
        i = 0;
        while ((float)i <= height - y) {
            RenderUtils.setColor(ScaleUtils.fadeBetween(startColor.getRGB(), endColor.getRGB(), 20L * (long)colorI));
            GL11.glVertex2d((double)centerX, (double)(centerY - (float)i));
            ++colorI;
            ++i;
        }
        GL11.glEnd();
        GL11.glLineWidth((float)line);
        GL11.glBegin((int)3);
        centerX = width;
        centerY = y + edgeRadius;
        for (i = 0; i <= vertices; ++i) {
            RenderUtils.setColor(ScaleUtils.fadeBetween(startColor.getRGB(), endColor.getRGB(), 20L * (long)colorI));
            angleRadians = Math.PI * 2 * (double)(i + 90) / (double)(vertices * 4);
            GL11.glVertex2d((double)((double)centerX + Math.sin(angleRadians) * (double)edgeRadius), (double)((double)centerY + Math.cos(angleRadians) * (double)edgeRadius));
            ++colorI;
        }
        GL11.glEnd();
        GL11.glLineWidth((float)line);
        GL11.glBegin((int)3);
        centerX = width;
        centerY = y;
        i = 0;
        while ((float)i <= width - x) {
            RenderUtils.setColor(ScaleUtils.fadeBetween(startColor.getRGB(), endColor.getRGB(), 20L * (long)colorI));
            GL11.glVertex2d((double)(centerX - (float)i), (double)centerY);
            ++colorI;
            ++i;
        }
        GL11.glEnd();
        GL11.glLineWidth((float)line);
        GL11.glBegin((int)3);
        centerX = x;
        centerY = y + edgeRadius;
        for (i = 0; i <= vertices; ++i) {
            RenderUtils.setColor(ScaleUtils.fadeBetween(startColor.getRGB(), endColor.getRGB(), 20L * (long)colorI));
            angleRadians = Math.PI * 2 * (double)(i + 180) / (double)(vertices * 4);
            GL11.glVertex2d((double)((double)centerX + Math.sin(angleRadians) * (double)edgeRadius), (double)((double)centerY + Math.cos(angleRadians) * (double)edgeRadius));
            ++colorI;
        }
        colorI = 0;
        GL11.glEnd();
        GL11.glLineWidth((float)line);
        GL11.glBegin((int)3);
        centerX = width;
        centerY = height + (float)vertices + offset;
        i = 0;
        while ((float)i <= width - x) {
            RenderUtils.setColor(ScaleUtils.fadeBetween(startColor.getRGB(), endColor.getRGB(), 20L * (long)colorI));
            GL11.glVertex2d((double)(centerX - (float)i), (double)centerY);
            ++colorI;
            ++i;
        }
        GL11.glEnd();
        GL11.glLineWidth((float)line);
        GL11.glBegin((int)3);
        centerX = x;
        centerY = height + edgeRadius;
        for (i = 0; i <= vertices; ++i) {
            RenderUtils.setColor(ScaleUtils.fadeBetween(startColor.getRGB(), endColor.getRGB(), 20L * (long)colorI));
            angleRadians = Math.PI * 2 * (double)(i + 180) / (double)(vertices * 4);
            GL11.glVertex2d((double)((double)centerX + Math.sin(angleRadians) * (double)edgeRadius), (double)((double)centerY - Math.cos(angleRadians) * (double)edgeRadius));
            ++colorI;
        }
        GL11.glEnd();
        GL11.glLineWidth((float)line);
        GL11.glBegin((int)3);
        centerX = x - edgeRadius;
        centerY = height + edgeRadius;
        i = 0;
        while ((float)i <= height - y) {
            RenderUtils.setColor(ScaleUtils.fadeBetween(startColor.getRGB(), endColor.getRGB(), 20L * (long)colorI));
            GL11.glVertex2d((double)centerX, (double)(centerY - (float)i));
            ++colorI;
            ++i;
        }
        GL11.glEnd();
        ScaleUtils.disableRender2D();
    }

    public static Color blend(Color color1, Color color2, double ratio) {
        float r = (float)ratio;
        float ir = 1.0f - r;
        float[] rgb1 = new float[3];
        float[] rgb2 = new float[3];
        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);
        float red = rgb1[0] * r + rgb2[0] * ir;
        float green = rgb1[1] * r + rgb2[1] * ir;
        float blue = rgb1[2] * r + rgb2[2] * ir;
        if (red < 0.0f) {
            red = 0.0f;
        } else if (red > 255.0f) {
            red = 255.0f;
        }
        if (green < 0.0f) {
            green = 0.0f;
        } else if (green > 255.0f) {
            green = 255.0f;
        }
        if (blue < 0.0f) {
            blue = 0.0f;
        } else if (blue > 255.0f) {
            blue = 255.0f;
        }
        Color color3 = null;
        try {
            color3 = new Color(red, green, blue);
        }
        catch (IllegalArgumentException exp) {
            NumberFormat nf = NumberFormat.getNumberInstance();
            exp.printStackTrace();
        }
        return color3;
    }

    public static void setColor(int colorHex) {
        float alpha = (float)(colorHex >> 24 & 0xFF) / 255.0f;
        float red = (float)(colorHex >> 16 & 0xFF) / 255.0f;
        float green = (float)(colorHex >> 8 & 0xFF) / 255.0f;
        float blue = (float)(colorHex & 0xFF) / 255.0f;
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
    }

    public static int fadeBetween(int startColour, int endColour, double progress) {
        if (progress > 1.0) {
            progress = 1.0 - progress % 1.0;
        }
        return ScaleUtils.fadeTo(startColour, endColour, progress);
    }

    public static int fadeBetween(int startColour, int endColour, long offset) {
        return ScaleUtils.fadeBetween(startColour, endColour, (double)((System.currentTimeMillis() + offset) % 2000L) / 1000.0);
    }

    public static int fadeBetween(int startColour, int endColour) {
        return ScaleUtils.fadeBetween(startColour, endColour, 0L);
    }

    public static int fadeTo(int startColour, int endColour, double progress) {
        double invert = 1.0 - progress;
        int r = (int)((double)(startColour >> 16 & 0xFF) * invert + (double)(endColour >> 16 & 0xFF) * progress);
        int g = (int)((double)(startColour >> 8 & 0xFF) * invert + (double)(endColour >> 8 & 0xFF) * progress);
        int b = (int)((double)(startColour & 0xFF) * invert + (double)(endColour & 0xFF) * progress);
        int a = (int)((double)(startColour >> 24 & 0xFF) * invert + (double)(endColour >> 24 & 0xFF) * progress);
        return (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
    }

    public static void enableRender2D() {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)2884);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glShadeModel((int)7425);
        int n = 0;
        GL11.glPushMatrix();
    }

    public static void disableRender2D() {
        GL11.glPopMatrix();
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2884);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179103_j((int)7424);
        GlStateManager.func_179084_k();
        GlStateManager.func_179098_w();
    }

    public static int[] getScaledMouseCoordinates(Minecraft mc, int mouseX, int mouseY) {
        int x = mouseX;
        int y = mouseY;
        switch (mc.field_71474_y.field_74335_Z) {
            case 0: {
                x <<= 1;
                y <<= 1;
                break;
            }
            case 1: {
                x = (int)((double)x * 0.5);
                y = (int)((double)y * 0.5);
                break;
            }
            case 3: {
                x = (int)((double)x * 1.5);
                y = (int)((double)y * 1.5);
            }
        }
        return new int[]{x, y};
    }

    public static double[] getScaledMouseCoordinates(Minecraft mc, double mouseX, double mouseY) {
        double x = mouseX;
        double y = mouseY;
        switch (mc.field_71474_y.field_74335_Z) {
            case 0: {
                x *= 2.0;
                y *= 2.0;
                break;
            }
            case 1: {
                x *= 0.5;
                y *= 0.5;
                break;
            }
            case 3: {
                x *= 1.5;
                y *= 1.5;
            }
        }
        return new double[]{x, y};
    }

    public static void scale(Minecraft mc) {
        switch (mc.field_71474_y.field_74335_Z) {
            case 0: {
                GlStateManager.func_179139_a((double)0.5, (double)0.5, (double)0.5);
                break;
            }
            case 1: {
                GlStateManager.func_179152_a((float)2.0f, (float)2.0f, (float)2.0f);
                break;
            }
            case 3: {
                GlStateManager.func_179139_a((double)0.6666666666666667, (double)0.6666666666666667, (double)0.6666666666666667);
            }
        }
    }
}

