/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GLAllocation
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.util.Vec3
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.utils.render;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class GLUtils {
    private static FloatBuffer colorBuffer = GLAllocation.func_74529_h((int)16);
    private static final Vec3 LIGHT0_POS = new Vec3((double)0.2f, 1.0, (double)-0.7f).func_72432_b();
    private static final Vec3 LIGHT1_POS = new Vec3((double)-0.2f, 1.0, (double)0.7f).func_72432_b();
    private static Map<Integer, Boolean> glCapMap = new HashMap<Integer, Boolean>();

    public static void disableGUIStandardItemLighting() {
        GlStateManager.func_179140_f();
        GlStateManager.func_179121_F();
        GlStateManager.func_179145_e();
        GlStateManager.func_179126_j();
        RenderHelper.func_74519_b();
    }

    public static void disableStandardItemLighting() {
        GlStateManager.func_179140_f();
        GlStateManager.func_179122_b((int)0);
        GlStateManager.func_179122_b((int)1);
        GlStateManager.func_179119_h();
    }

    public static void startSmooth() {
        GL11.glEnable((int)2848);
        GL11.glEnable((int)2881);
        GL11.glEnable((int)2832);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glHint((int)3154, (int)4354);
        GL11.glHint((int)3155, (int)4354);
        GL11.glHint((int)3153, (int)4354);
    }

    public static void endSmooth() {
        GL11.glDisable((int)2848);
        GL11.glDisable((int)2881);
        GL11.glEnable((int)2832);
    }

    public static void enableStandardItemLighting() {
        GlStateManager.func_179145_e();
        GlStateManager.func_179085_a((int)0);
        GlStateManager.func_179085_a((int)1);
        GlStateManager.func_179142_g();
        GlStateManager.func_179104_a((int)1032, (int)5634);
        float n = 0.4f;
        float n2 = 0.6f;
        float n3 = 0.0f;
        GL11.glLight((int)16384, (int)4611, (FloatBuffer)GLUtils.setColorBuffer(GLUtils.LIGHT0_POS.field_72450_a, GLUtils.LIGHT0_POS.field_72448_b, GLUtils.LIGHT0_POS.field_72449_c, 0.0));
        GL11.glLight((int)16384, (int)4609, (FloatBuffer)GLUtils.setColorBuffer(0.6f, 0.6f, 0.6f, 1.0f));
        GL11.glLight((int)16384, (int)4608, (FloatBuffer)GLUtils.setColorBuffer(0.0f, 0.0f, 0.0f, 1.0f));
        GL11.glLight((int)16384, (int)4610, (FloatBuffer)GLUtils.setColorBuffer(0.0f, 0.0f, 0.0f, 1.0f));
        GL11.glLight((int)16385, (int)4611, (FloatBuffer)GLUtils.setColorBuffer(GLUtils.LIGHT1_POS.field_72450_a, GLUtils.LIGHT1_POS.field_72448_b, GLUtils.LIGHT1_POS.field_72449_c, 0.0));
        GL11.glLight((int)16385, (int)4609, (FloatBuffer)GLUtils.setColorBuffer(0.6f, 0.6f, 0.6f, 1.0f));
        GL11.glLight((int)16385, (int)4608, (FloatBuffer)GLUtils.setColorBuffer(0.0f, 0.0f, 0.0f, 1.0f));
        GL11.glLight((int)16385, (int)4610, (FloatBuffer)GLUtils.setColorBuffer(0.0f, 0.0f, 0.0f, 1.0f));
        GlStateManager.func_179103_j((int)7424);
        GL11.glLightModel((int)2899, (FloatBuffer)GLUtils.setColorBuffer(0.4f, 0.4f, 0.4f, 1.0f));
    }

    private static FloatBuffer setColorBuffer(double p_setColorBuffer_0_, double p_setColorBuffer_2_, double p_setColorBuffer_4_, double p_setColorBuffer_6_) {
        return GLUtils.setColorBuffer((float)p_setColorBuffer_0_, (float)p_setColorBuffer_2_, (float)p_setColorBuffer_4_, (float)p_setColorBuffer_6_);
    }

    private static FloatBuffer setColorBuffer(float p_setColorBuffer_0_, float p_setColorBuffer_1_, float p_setColorBuffer_2_, float p_setColorBuffer_3_) {
        colorBuffer.clear();
        colorBuffer.put(p_setColorBuffer_0_).put(p_setColorBuffer_1_).put(p_setColorBuffer_2_).put(p_setColorBuffer_3_);
        colorBuffer.flip();
        return colorBuffer;
    }

    public static void enableGUIStandardItemLighting() {
        GlStateManager.func_179094_E();
        GlStateManager.func_179114_b((float)-30.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)165.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        GLUtils.enableStandardItemLighting();
        GlStateManager.func_179121_F();
    }

    public static void setGLCap(int cap, boolean flag) {
        glCapMap.put(cap, GL11.glGetBoolean((int)cap));
        if (flag) {
            GL11.glEnable((int)cap);
        } else {
            GL11.glDisable((int)cap);
        }
    }

    public static void revertGLCap(int cap) {
        Boolean origCap = glCapMap.get(cap);
        if (origCap != null) {
            if (origCap.booleanValue()) {
                GL11.glEnable((int)cap);
            } else {
                GL11.glDisable((int)cap);
            }
        }
    }

    public static void glEnable(int cap) {
        GLUtils.setGLCap(cap, true);
    }

    public static void glDisable(int cap) {
        GLUtils.setGLCap(cap, false);
    }

    public static void revertAllCaps() {
        for (int cap : glCapMap.keySet()) {
            GLUtils.revertGLCap(cap);
        }
    }

    public static double interpolate(double current, double old, double scale) {
        return old + (current - old) * scale;
    }
}

