/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  org.lwjgl.opengl.GL11
 */
package liying.utils.render;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class GLUtils {
    private static FloatBuffer colorBuffer;
    private static Map glCapMap;

    public static void glDisable(int n) {
        GLUtils.setGLCap(n, false);
    }

    private static FloatBuffer setColorBuffer(double d, double d2, double d3, double d4) {
        return GLUtils.setColorBuffer((float)d, (float)d2, (float)d3, (float)d4);
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

    public static void enableGUIStandardItemLighting() {
        GlStateManager.func_179094_E();
        GlStateManager.func_179114_b((float)-30.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)165.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        GLUtils.enableStandardItemLighting();
        GlStateManager.func_179121_F();
    }

    public static void revertAllCaps() {
        Iterator iterator2 = glCapMap.keySet().iterator();
        while (iterator2.hasNext()) {
            int n = (Integer)iterator2.next();
            GLUtils.revertGLCap(n);
        }
    }

    private static FloatBuffer setColorBuffer(float f, float f2, float f3, float f4) {
        colorBuffer.clear();
        colorBuffer.put(f).put(f2).put(f3).put(f4);
        colorBuffer.flip();
        return colorBuffer;
    }

    public static void enableStandardItemLighting() {
        GlStateManager.func_179145_e();
        GlStateManager.func_179085_a((int)0);
        GlStateManager.func_179085_a((int)1);
        GlStateManager.func_179142_g();
        GlStateManager.func_179104_a((int)1032, (int)5634);
        float f = 0.4f;
        float f2 = 0.6f;
        GL11.glLight((int)16384, (int)4609, (FloatBuffer)GLUtils.setColorBuffer(0.6f, 0.6f, 0.6f, 1.0f));
        GL11.glLight((int)16384, (int)4608, (FloatBuffer)GLUtils.setColorBuffer(0.0f, 0.0f, 0.0f, 1.0f));
        GL11.glLight((int)16385, (int)4609, (FloatBuffer)GLUtils.setColorBuffer(0.6f, 0.6f, 0.6f, 1.0f));
        GL11.glLight((int)16385, (int)4608, (FloatBuffer)GLUtils.setColorBuffer(0.0f, 0.0f, 0.0f, 1.0f));
        GlStateManager.func_179103_j((int)7424);
        GL11.glLightModel((int)2899, (FloatBuffer)GLUtils.setColorBuffer(0.4f, 0.4f, 0.4f, 1.0f));
    }

    public static void revertGLCap(int n) {
        Boolean bl = (Boolean)glCapMap.get(n);
        if (bl != null) {
            if (bl.booleanValue()) {
                GL11.glEnable((int)n);
            } else {
                GL11.glDisable((int)n);
            }
        }
    }

    public static void setGLCap(int n, boolean bl) {
        glCapMap.put(n, GL11.glGetBoolean((int)n));
        if (bl) {
            GL11.glEnable((int)n);
        } else {
            GL11.glDisable((int)n);
        }
    }

    public static void disableStandardItemLighting() {
        GlStateManager.func_179140_f();
        GlStateManager.func_179122_b((int)0);
        GlStateManager.func_179122_b((int)1);
        GlStateManager.func_179119_h();
    }

    public static void endSmooth() {
        GL11.glDisable((int)2848);
        GL11.glDisable((int)2881);
        GL11.glEnable((int)2832);
    }

    public static void glEnable(int n) {
        GLUtils.setGLCap(n, true);
    }

    static {
        glCapMap = new HashMap();
    }
}

