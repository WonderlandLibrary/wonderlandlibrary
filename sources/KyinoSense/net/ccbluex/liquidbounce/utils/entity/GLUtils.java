/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.GLAllocation
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.util.Vec3
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.Display
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.GLU
 */
package net.ccbluex.liquidbounce.utils.entity;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import net.ccbluex.liquidbounce.utils.math.Vec3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.Vec3;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class GLUtils {
    private static FloatBuffer colorBuffer;
    private static final Vec3 LIGHT0_POS;
    private static final Vec3 LIGHT1_POS;
    public static final FloatBuffer MODELVIEW;
    public static final FloatBuffer PROJECTION;
    public static final IntBuffer VIEWPORT;
    public static final FloatBuffer TO_SCREEN_BUFFER;
    public static final FloatBuffer TO_WORLD_BUFFER;
    private static Map<Integer, Boolean> glCapMap;

    public static void disableStandardItemLighting() {
        GlStateManager.func_179140_f();
        GlStateManager.func_179122_b((int)0);
        GlStateManager.func_179122_b((int)1);
        GlStateManager.func_179119_h();
    }

    public static Vec3f toWorld(Vec3f pos) {
        return GLUtils.toWorld(pos.getX(), pos.getY(), pos.getZ());
    }

    public static Vec3f toWorld(double x, double y, double z) {
        boolean result = GLU.gluUnProject((float)((float)x), (float)((float)y), (float)((float)z), (FloatBuffer)MODELVIEW, (FloatBuffer)PROJECTION, (IntBuffer)VIEWPORT, (FloatBuffer)((FloatBuffer)TO_WORLD_BUFFER.clear()));
        if (result) {
            return new Vec3f(TO_WORLD_BUFFER.get(0), TO_WORLD_BUFFER.get(1), TO_WORLD_BUFFER.get(2));
        }
        return null;
    }

    public static Vec3f toScreen(Vec3f pos) {
        return GLUtils.toScreen(pos.getX(), pos.getY(), pos.getZ());
    }

    public static Vec3f toScreen(double x, double y, double z) {
        boolean result = GLU.gluProject((float)((float)x), (float)((float)y), (float)((float)z), (FloatBuffer)MODELVIEW, (FloatBuffer)PROJECTION, (IntBuffer)VIEWPORT, (FloatBuffer)((FloatBuffer)TO_SCREEN_BUFFER.clear()));
        if (result) {
            return new Vec3f(TO_SCREEN_BUFFER.get(0), (float)Display.getHeight() - TO_SCREEN_BUFFER.get(1), TO_SCREEN_BUFFER.get(2));
        }
        return null;
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

    public static void glColor(int hex) {
        float[] color = GLUtils.getColor(hex);
        GlStateManager.func_179131_c((float)color[0], (float)color[1], (float)color[2], (float)color[3]);
    }

    public static float[] getColor(int hex) {
        return new float[]{(float)(hex >> 16 & 0xFF) / 255.0f, (float)(hex >> 8 & 0xFF) / 255.0f, (float)(hex & 0xFF) / 255.0f, (float)(hex >> 24 & 0xFF) / 255.0f};
    }

    public static int getScaleFactor() {
        int scaleFactor = 1;
        boolean isUnicode = Minecraft.func_71410_x().func_152349_b();
        int guiScale = Minecraft.func_71410_x().field_71474_y.field_74335_Z;
        if (guiScale == 0) {
            guiScale = 1000;
        }
        while (scaleFactor < guiScale && Minecraft.func_71410_x().field_71443_c / (scaleFactor + 1) >= 320 && Minecraft.func_71410_x().field_71440_d / (scaleFactor + 1) >= 240) {
            ++scaleFactor;
        }
        if (isUnicode && scaleFactor % 2 != 0 && scaleFactor != 1) {
            --scaleFactor;
        }
        return scaleFactor;
    }

    public static int getMouseX() {
        return Mouse.getX() * GLUtils.getScreenWidth() / Minecraft.func_71410_x().field_71443_c;
    }

    public static int getMouseY() {
        return GLUtils.getScreenHeight() - Mouse.getY() * GLUtils.getScreenHeight() / Minecraft.func_71410_x().field_71443_c - 1;
    }

    public static int getScreenWidth() {
        return Minecraft.func_71410_x().field_71443_c / GLUtils.getScaleFactor();
    }

    public static int getScreenHeight() {
        return Minecraft.func_71410_x().field_71440_d / GLUtils.getScaleFactor();
    }

    public static double interpolate(double current, double old, double scale) {
        return old + (current - old) * scale;
    }

    static {
        MODELVIEW = BufferUtils.createFloatBuffer((int)16);
        PROJECTION = BufferUtils.createFloatBuffer((int)16);
        VIEWPORT = BufferUtils.createIntBuffer((int)16);
        TO_SCREEN_BUFFER = BufferUtils.createFloatBuffer((int)3);
        TO_WORLD_BUFFER = BufferUtils.createFloatBuffer((int)3);
        colorBuffer = GLAllocation.func_74529_h((int)16);
        LIGHT0_POS = new Vec3((double)0.2f, 1.0, (double)-0.7f).func_72432_b();
        LIGHT1_POS = new Vec3((double)-0.2f, 1.0, (double)0.7f).func_72432_b();
        glCapMap = new HashMap<Integer, Boolean>();
    }
}

