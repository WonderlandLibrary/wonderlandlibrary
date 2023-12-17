// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import net.minecraft.util.math.Vec3d;
import java.nio.FloatBuffer;

public class RenderHelper
{
    private static final FloatBuffer COLOR_BUFFER;
    private static final Vec3d LIGHT0_POS;
    private static final Vec3d LIGHT1_POS;
    
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
        GlStateManager.glLight(16384, 4611, setColorBuffer(RenderHelper.LIGHT0_POS.x, RenderHelper.LIGHT0_POS.y, RenderHelper.LIGHT0_POS.z, 0.0));
        final float f = 0.6f;
        GlStateManager.glLight(16384, 4609, setColorBuffer(0.6f, 0.6f, 0.6f, 1.0f));
        GlStateManager.glLight(16384, 4608, setColorBuffer(0.0f, 0.0f, 0.0f, 1.0f));
        GlStateManager.glLight(16384, 4610, setColorBuffer(0.0f, 0.0f, 0.0f, 1.0f));
        GlStateManager.glLight(16385, 4611, setColorBuffer(RenderHelper.LIGHT1_POS.x, RenderHelper.LIGHT1_POS.y, RenderHelper.LIGHT1_POS.z, 0.0));
        GlStateManager.glLight(16385, 4609, setColorBuffer(0.6f, 0.6f, 0.6f, 1.0f));
        GlStateManager.glLight(16385, 4608, setColorBuffer(0.0f, 0.0f, 0.0f, 1.0f));
        GlStateManager.glLight(16385, 4610, setColorBuffer(0.0f, 0.0f, 0.0f, 1.0f));
        GlStateManager.shadeModel(7424);
        final float f2 = 0.4f;
        GlStateManager.glLightModel(2899, setColorBuffer(0.4f, 0.4f, 0.4f, 1.0f));
    }
    
    private static FloatBuffer setColorBuffer(final double p_74517_0_, final double p_74517_2_, final double p_74517_4_, final double p_74517_6_) {
        return setColorBuffer((float)p_74517_0_, (float)p_74517_2_, (float)p_74517_4_, (float)p_74517_6_);
    }
    
    public static FloatBuffer setColorBuffer(final float p_74521_0_, final float p_74521_1_, final float p_74521_2_, final float p_74521_3_) {
        RenderHelper.COLOR_BUFFER.clear();
        RenderHelper.COLOR_BUFFER.put(p_74521_0_).put(p_74521_1_).put(p_74521_2_).put(p_74521_3_);
        RenderHelper.COLOR_BUFFER.flip();
        return RenderHelper.COLOR_BUFFER;
    }
    
    public static void enableGUIStandardItemLighting() {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(-30.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(165.0f, 1.0f, 0.0f, 0.0f);
        enableStandardItemLighting();
        GlStateManager.popMatrix();
    }
    
    static {
        COLOR_BUFFER = GLAllocation.createDirectFloatBuffer(4);
        LIGHT0_POS = new Vec3d(0.20000000298023224, 1.0, -0.699999988079071).normalize();
        LIGHT1_POS = new Vec3d(-0.20000000298023224, 1.0, 0.699999988079071).normalize();
    }
}
