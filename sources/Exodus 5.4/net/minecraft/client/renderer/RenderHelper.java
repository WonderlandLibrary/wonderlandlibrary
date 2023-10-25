/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package net.minecraft.client.renderer;

import java.nio.FloatBuffer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class RenderHelper {
    private static final Vec3 LIGHT0_POS;
    private static FloatBuffer colorBuffer;
    private static final Vec3 LIGHT1_POS;

    public static void disableStandardItemLighting() {
        GlStateManager.disableLighting();
        GlStateManager.disableLight(0);
        GlStateManager.disableLight(1);
        GlStateManager.disableColorMaterial();
    }

    public static void enableStandardItemLighting() {
        GlStateManager.enableLighting();
        GlStateManager.enableLight(0);
        GlStateManager.enableLight(1);
        GlStateManager.enableColorMaterial();
        GlStateManager.colorMaterial(1032, 5634);
        float f = 0.4f;
        float f2 = 0.6f;
        float f3 = 0.0f;
        GL11.glLight((int)16384, (int)4611, (FloatBuffer)RenderHelper.setColorBuffer(RenderHelper.LIGHT0_POS.xCoord, RenderHelper.LIGHT0_POS.yCoord, RenderHelper.LIGHT0_POS.zCoord, 0.0));
        GL11.glLight((int)16384, (int)4609, (FloatBuffer)RenderHelper.setColorBuffer(f2, f2, f2, 1.0f));
        GL11.glLight((int)16384, (int)4608, (FloatBuffer)RenderHelper.setColorBuffer(0.0f, 0.0f, 0.0f, 1.0f));
        GL11.glLight((int)16384, (int)4610, (FloatBuffer)RenderHelper.setColorBuffer(f3, f3, f3, 1.0f));
        GL11.glLight((int)16385, (int)4611, (FloatBuffer)RenderHelper.setColorBuffer(RenderHelper.LIGHT1_POS.xCoord, RenderHelper.LIGHT1_POS.yCoord, RenderHelper.LIGHT1_POS.zCoord, 0.0));
        GL11.glLight((int)16385, (int)4609, (FloatBuffer)RenderHelper.setColorBuffer(f2, f2, f2, 1.0f));
        GL11.glLight((int)16385, (int)4608, (FloatBuffer)RenderHelper.setColorBuffer(0.0f, 0.0f, 0.0f, 1.0f));
        GL11.glLight((int)16385, (int)4610, (FloatBuffer)RenderHelper.setColorBuffer(f3, f3, f3, 1.0f));
        GlStateManager.shadeModel(7424);
        GL11.glLightModel((int)2899, (FloatBuffer)RenderHelper.setColorBuffer(f, f, f, 1.0f));
    }

    private static FloatBuffer setColorBuffer(double d, double d2, double d3, double d4) {
        return RenderHelper.setColorBuffer((float)d, (float)d2, (float)d3, (float)d4);
    }

    static {
        colorBuffer = GLAllocation.createDirectFloatBuffer(16);
        LIGHT0_POS = new Vec3(0.2f, 1.0, -0.7f).normalize();
        LIGHT1_POS = new Vec3(-0.2f, 1.0, 0.7f).normalize();
    }

    private static FloatBuffer setColorBuffer(float f, float f2, float f3, float f4) {
        colorBuffer.clear();
        colorBuffer.put(f).put(f2).put(f3).put(f4);
        colorBuffer.flip();
        return colorBuffer;
    }

    public static void enableGUIStandardItemLighting() {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(-30.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(165.0f, 1.0f, 0.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }
}

