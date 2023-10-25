/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer;

import java.nio.FloatBuffer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.Vec3d;

public class RenderHelper {
    private static final FloatBuffer COLOR_BUFFER = GLAllocation.createDirectFloatBuffer(4);
    private static final Vec3d LIGHT0_POS = new Vec3d(0.2f, 1.0, -0.7f).normalize();
    private static final Vec3d LIGHT1_POS = new Vec3d(-0.2f, 1.0, 0.7f).normalize();

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
        GlStateManager.glLight(16384, 4611, RenderHelper.setColorBuffer(RenderHelper.LIGHT0_POS.x, RenderHelper.LIGHT0_POS.y, RenderHelper.LIGHT0_POS.z, 0.0));
        float f = 0.6f;
        GlStateManager.glLight(16384, 4609, RenderHelper.setColorBuffer(0.6f, 0.6f, 0.6f, 1.0f));
        GlStateManager.glLight(16384, 4608, RenderHelper.setColorBuffer(0.0f, 0.0f, 0.0f, 1.0f));
        GlStateManager.glLight(16384, 4610, RenderHelper.setColorBuffer(0.0f, 0.0f, 0.0f, 1.0f));
        GlStateManager.glLight(16385, 4611, RenderHelper.setColorBuffer(RenderHelper.LIGHT1_POS.x, RenderHelper.LIGHT1_POS.y, RenderHelper.LIGHT1_POS.z, 0.0));
        GlStateManager.glLight(16385, 4609, RenderHelper.setColorBuffer(0.6f, 0.6f, 0.6f, 1.0f));
        GlStateManager.glLight(16385, 4608, RenderHelper.setColorBuffer(0.0f, 0.0f, 0.0f, 1.0f));
        GlStateManager.glLight(16385, 4610, RenderHelper.setColorBuffer(0.0f, 0.0f, 0.0f, 1.0f));
        GlStateManager.shadeModel(7424);
        float f1 = 0.4f;
        GlStateManager.glLightModel(2899, RenderHelper.setColorBuffer(0.4f, 0.4f, 0.4f, 1.0f));
    }

    private static FloatBuffer setColorBuffer(double p_74517_0_, double p_74517_2_, double p_74517_4_, double p_74517_6_) {
        return RenderHelper.setColorBuffer((float)p_74517_0_, (float)p_74517_2_, (float)p_74517_4_, (float)p_74517_6_);
    }

    public static FloatBuffer setColorBuffer(float p_74521_0_, float p_74521_1_, float p_74521_2_, float p_74521_3_) {
        COLOR_BUFFER.clear();
        COLOR_BUFFER.put(p_74521_0_).put(p_74521_1_).put(p_74521_2_).put(p_74521_3_);
        COLOR_BUFFER.flip();
        return COLOR_BUFFER;
    }

    public static void enableGUIStandardItemLighting() {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(-30.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(165.0f, 1.0f, 0.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }
}

