/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.Utils.render;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class GLUtil {
    public static void render(int mode, Runnable render) {
        GL11.glBegin((int)mode);
        render.run();
        GL11.glEnd();
    }

    public static void setup2DRendering(Runnable f) {
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3553);
        f.run();
        GL11.glEnable((int)3553);
        GlStateManager.func_179084_k();
    }

    public static void rotate(float x, float y, float rotate, Runnable f) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179109_b((float)x, (float)y, (float)0.0f);
        GlStateManager.func_179114_b((float)rotate, (float)0.0f, (float)0.0f, (float)-1.0f);
        GlStateManager.func_179109_b((float)(-x), (float)(-y), (float)0.0f);
        f.run();
        GlStateManager.func_179121_F();
    }
}

