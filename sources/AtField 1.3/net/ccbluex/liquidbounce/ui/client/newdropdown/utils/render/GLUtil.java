/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.newdropdown.utils.render;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class GLUtil {
    public static void render(int n, Runnable runnable) {
        GL11.glBegin((int)n);
        runnable.run();
        GL11.glEnd();
    }

    public static void rotate(float f, float f2, float f3, Runnable runnable) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179109_b((float)f, (float)f2, (float)0.0f);
        GlStateManager.func_179114_b((float)f3, (float)0.0f, (float)0.0f, (float)-1.0f);
        GlStateManager.func_179109_b((float)(-f), (float)(-f2), (float)0.0f);
        runnable.run();
        GlStateManager.func_179121_F();
    }

    public static void setup2DRendering(Runnable runnable) {
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3553);
        runnable.run();
        GL11.glEnable((int)3553);
        GlStateManager.func_179084_k();
    }
}

