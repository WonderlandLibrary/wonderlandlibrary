/*
 * Decompiled with CFR 0.150.
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
    private static FloatBuffer colorBuffer = GLAllocation.createDirectFloatBuffer(16);
    private static final Vec3 field_82884_b = new Vec3(0.2f, 1.0, -0.7f).normalize();
    private static final Vec3 field_82885_c = new Vec3(-0.2f, 1.0, 0.7f).normalize();
    private static final String __OBFID = "CL_00000629";

    public static void disableStandardItemLighting() {
        GlStateManager.disableLighting();
        GlStateManager.disableBooleanStateAt(0);
        GlStateManager.disableBooleanStateAt(1);
        GlStateManager.disableColorMaterial();
    }

    public static void enableStandardItemLighting() {
        GlStateManager.enableLighting();
        GlStateManager.enableBooleanStateAt(0);
        GlStateManager.enableBooleanStateAt(1);
        GlStateManager.enableColorMaterial();
        GlStateManager.colorMaterial(1032, 5634);
        float var0 = 0.4f;
        float var1 = 0.6f;
        float var2 = 0.0f;
        GL11.glLight((int)16384, (int)4611, (FloatBuffer)RenderHelper.setColorBuffer(RenderHelper.field_82884_b.xCoord, RenderHelper.field_82884_b.yCoord, RenderHelper.field_82884_b.zCoord, 0.0));
        GL11.glLight((int)16384, (int)4609, (FloatBuffer)RenderHelper.setColorBuffer(var1, var1, var1, 1.0f));
        GL11.glLight((int)16384, (int)4608, (FloatBuffer)RenderHelper.setColorBuffer(0.0f, 0.0f, 0.0f, 1.0f));
        GL11.glLight((int)16384, (int)4610, (FloatBuffer)RenderHelper.setColorBuffer(var2, var2, var2, 1.0f));
        GL11.glLight((int)16385, (int)4611, (FloatBuffer)RenderHelper.setColorBuffer(RenderHelper.field_82885_c.xCoord, RenderHelper.field_82885_c.yCoord, RenderHelper.field_82885_c.zCoord, 0.0));
        GL11.glLight((int)16385, (int)4609, (FloatBuffer)RenderHelper.setColorBuffer(var1, var1, var1, 1.0f));
        GL11.glLight((int)16385, (int)4608, (FloatBuffer)RenderHelper.setColorBuffer(0.0f, 0.0f, 0.0f, 1.0f));
        GL11.glLight((int)16385, (int)4610, (FloatBuffer)RenderHelper.setColorBuffer(var2, var2, var2, 1.0f));
        GlStateManager.shadeModel(7424);
        GL11.glLightModel((int)2899, (FloatBuffer)RenderHelper.setColorBuffer(var0, var0, var0, 1.0f));
    }

    private static FloatBuffer setColorBuffer(double p_74517_0_, double p_74517_2_, double p_74517_4_, double p_74517_6_) {
        return RenderHelper.setColorBuffer((float)p_74517_0_, (float)p_74517_2_, (float)p_74517_4_, (float)p_74517_6_);
    }

    private static FloatBuffer setColorBuffer(float p_74521_0_, float p_74521_1_, float p_74521_2_, float p_74521_3_) {
        colorBuffer.clear();
        colorBuffer.put(p_74521_0_).put(p_74521_1_).put(p_74521_2_).put(p_74521_3_);
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

