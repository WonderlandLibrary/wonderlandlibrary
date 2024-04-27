package me.utils.render;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class GLUtils {
    private static FloatBuffer colorBuffer;
    private static Map<Integer, Boolean> glCapMap;

    public static void disableStandardItemLighting() {
        GlStateManager.disableLighting();
        GlStateManager.disableLight((int)0);
        GlStateManager.disableLight((int)1);
        GlStateManager.disableColorMaterial();
    }

    public static void enableGUIStandardItemLighting() {
        GlStateManager.pushMatrix();
        GlStateManager.rotate((float)-30.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)165.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        GLUtils.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    public static void enableStandardItemLighting() {
        GlStateManager.enableLighting();
        GlStateManager.enableLight((int)0);
        GlStateManager.enableLight((int)1);
        GlStateManager.enableColorMaterial();
        GlStateManager.colorMaterial((int)1032, (int)5634);
        float n = 0.4f;
        float n2 = 0.6f;
        GL11.glLight((int)16384, (int)4609, (FloatBuffer)GLUtils.setColorBuffer(0.6f, 0.6f, 0.6f, 1.0f));
        GL11.glLight((int)16384, (int)4608, (FloatBuffer)GLUtils.setColorBuffer(0.0f, 0.0f, 0.0f, 1.0f));
        GL11.glLight((int)16385, (int)4609, (FloatBuffer)GLUtils.setColorBuffer(0.6f, 0.6f, 0.6f, 1.0f));
        GL11.glLight((int)16385, (int)4608, (FloatBuffer)GLUtils.setColorBuffer(0.0f, 0.0f, 0.0f, 1.0f));
        GlStateManager.shadeModel((int)7424);
        GL11.glLightModel((int)2899, (FloatBuffer)GLUtils.setColorBuffer(0.4f, 0.4f, 0.4f, 1.0f));
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

    private static FloatBuffer setColorBuffer(double p_setColorBuffer_0_, double p_setColorBuffer_2_, double p_setColorBuffer_4_, double p_setColorBuffer_6_) {
        return GLUtils.setColorBuffer((float)p_setColorBuffer_0_, (float)p_setColorBuffer_2_, (float)p_setColorBuffer_4_, (float)p_setColorBuffer_6_);
    }

    private static FloatBuffer setColorBuffer(float p_setColorBuffer_0_, float p_setColorBuffer_1_, float p_setColorBuffer_2_, float p_setColorBuffer_3_) {
        colorBuffer.clear();
        colorBuffer.put(p_setColorBuffer_0_).put(p_setColorBuffer_1_).put(p_setColorBuffer_2_).put(p_setColorBuffer_3_);
        colorBuffer.flip();
        return colorBuffer;
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

    static {
        glCapMap = new HashMap<Integer, Boolean>();
    }
}
