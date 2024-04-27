/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.cnfont;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class GLUtils {
    public static void startScissor(int x, int y, int width, int height) {
        int scaleFactor = new ScaledResolution(Minecraft.func_71410_x()).func_78325_e();
        GL11.glPushMatrix();
        GL11.glEnable((int)3089);
        GL11.glScissor((int)(x * scaleFactor), (int)(Minecraft.func_71410_x().field_71440_d - (y + height) * scaleFactor), (int)(width * scaleFactor), (int)(height * scaleFactor));
    }

    public static void stopScissor() {
        GL11.glDisable((int)3089);
        GL11.glPopMatrix();
    }

    public static void pushMatrix() {
        GL11.glPushMatrix();
    }

    public static void popMatrix() {
        GL11.glPopMatrix();
    }

    public static void enable(int cap) {
        GL11.glEnable((int)cap);
    }

    public static void disable(int cap) {
        GL11.glDisable((int)cap);
    }

    public static void blendFunc(int sFactor, int dFactor) {
        GL11.glBlendFunc((int)sFactor, (int)dFactor);
    }

    public static void translated(double x, double y, double z) {
        GL11.glTranslated((double)x, (double)y, (double)z);
    }

    public static void rotated(double angle, double x, double y, double z) {
        GL11.glRotated((double)angle, (double)x, (double)y, (double)z);
    }

    public static void depthMask(boolean flag) {
        GL11.glDepthMask((boolean)flag);
    }

    public static void color(int r, int g, int b) {
        GLUtils.color(r, g, b, 255);
    }

    public static void color(int r, int g, int b, int a) {
        GlStateManager.func_179131_c((float)((float)r / 255.0f), (float)((float)g / 255.0f), (float)((float)b / 255.0f), (float)((float)a / 255.0f));
    }

    public static void color(int hex) {
        GlStateManager.func_179131_c((float)((float)(hex >> 16 & 0xFF) / 255.0f), (float)((float)(hex >> 8 & 0xFF) / 255.0f), (float)((float)(hex & 0xFF) / 255.0f), (float)((float)(hex >> 24 & 0xFF) / 255.0f));
    }

    public static void resetColor() {
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }
}

