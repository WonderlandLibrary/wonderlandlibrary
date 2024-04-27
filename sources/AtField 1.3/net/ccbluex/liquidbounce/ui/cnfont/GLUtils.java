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
    public static void resetColor() {
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void popMatrix() {
        GL11.glPopMatrix();
    }

    public static void enable(int n) {
        GL11.glEnable((int)n);
    }

    public static void depthMask(boolean bl) {
        GL11.glDepthMask((boolean)bl);
    }

    public static void translated(double d, double d2, double d3) {
        GL11.glTranslated((double)d, (double)d2, (double)d3);
    }

    public static void color(int n, int n2, int n3, int n4) {
        GlStateManager.func_179131_c((float)((float)n / 255.0f), (float)((float)n2 / 255.0f), (float)((float)n3 / 255.0f), (float)((float)n4 / 255.0f));
    }

    public static void color(int n, int n2, int n3) {
        GLUtils.color(n, n2, n3, 255);
    }

    public static void rotated(double d, double d2, double d3, double d4) {
        GL11.glRotated((double)d, (double)d2, (double)d3, (double)d4);
    }

    public static void color(int n) {
        GlStateManager.func_179131_c((float)((float)(n >> 16 & 0xFF) / 255.0f), (float)((float)(n >> 8 & 0xFF) / 255.0f), (float)((float)(n & 0xFF) / 255.0f), (float)((float)(n >> 24 & 0xFF) / 255.0f));
    }

    public static void blendFunc(int n, int n2) {
        GL11.glBlendFunc((int)n, (int)n2);
    }

    public static void pushMatrix() {
        GL11.glPushMatrix();
    }

    public static void disable(int n) {
        GL11.glDisable((int)n);
    }

    public static void startScissor(int n, int n2, int n3, int n4) {
        int n5 = new ScaledResolution(Minecraft.func_71410_x()).func_78325_e();
        GL11.glPushMatrix();
        GL11.glEnable((int)3089);
        GL11.glScissor((int)(n * n5), (int)(Minecraft.func_71410_x().field_71440_d - (n2 + n4) * n5), (int)(n3 * n5), (int)(n4 * n5));
    }

    public static void stopScissor() {
        GL11.glDisable((int)3089);
        GL11.glPopMatrix();
    }
}

