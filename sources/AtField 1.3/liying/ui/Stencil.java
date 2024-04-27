/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.shader.Framebuffer
 *  org.lwjgl.opengl.EXTFramebufferObject
 *  org.lwjgl.opengl.GL11
 */
package liying.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;

public class Stencil {
    public static Minecraft mc = Minecraft.func_71410_x();
    public static int nextColor;

    public static void dispose() {
        GL11.glDisable((int)2960);
        GlStateManager.func_179118_c();
        GlStateManager.func_179084_k();
    }

    public static void checkSetupFBO() {
        Framebuffer framebuffer = Minecraft.func_71410_x().func_147110_a();
        if (framebuffer != null && framebuffer.field_147624_h > -1) {
            Stencil.setupFBO(framebuffer);
            framebuffer.field_147624_h = -1;
        }
    }

    public static void setupFBO(Framebuffer framebuffer) {
        EXTFramebufferObject.glDeleteRenderbuffersEXT((int)framebuffer.field_147624_h);
        int n = EXTFramebufferObject.glGenRenderbuffersEXT();
        EXTFramebufferObject.glBindRenderbufferEXT((int)36161, (int)n);
        EXTFramebufferObject.glRenderbufferStorageEXT((int)36161, (int)34041, (int)Minecraft.func_71410_x().field_71443_c, (int)Minecraft.func_71410_x().field_71440_d);
        EXTFramebufferObject.glFramebufferRenderbufferEXT((int)36160, (int)36128, (int)36161, (int)n);
        EXTFramebufferObject.glFramebufferRenderbufferEXT((int)36160, (int)36096, (int)36161, (int)n);
    }

    public static void write(boolean bl) {
        Stencil.checkSetupFBO();
        GL11.glClearStencil((int)0);
        GL11.glClear((int)1024);
        GL11.glEnable((int)2960);
        GL11.glStencilFunc((int)519, (int)1, (int)65535);
        GL11.glStencilOp((int)7680, (int)7680, (int)7681);
        if (!bl) {
            GlStateManager.func_179135_a((boolean)false, (boolean)false, (boolean)false, (boolean)false);
        }
    }

    public static void erase(boolean bl) {
        GL11.glStencilFunc((int)(bl ? 514 : 517), (int)1, (int)65535);
        GL11.glStencilOp((int)7680, (int)7680, (int)7681);
        GlStateManager.func_179135_a((boolean)true, (boolean)true, (boolean)true, (boolean)true);
        GlStateManager.func_179141_d();
        GlStateManager.func_179147_l();
        GL11.glAlphaFunc((int)516, (float)0.0f);
    }
}

