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
package net.ccbluex.liquidbounce.utils.render;

import net.ccbluex.liquidbounce.utils.MinecraftInstance2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;

public class Stencil
extends MinecraftInstance2 {
    public static void dispose() {
        GL11.glDisable((int)2960);
        GlStateManager.func_179118_c();
        GlStateManager.func_179084_k();
    }

    public static void erase(boolean invert) {
        GL11.glStencilFunc((int)(invert ? 514 : 517), (int)1, (int)65535);
        GL11.glStencilOp((int)7680, (int)7680, (int)7681);
        GlStateManager.func_179135_a((boolean)true, (boolean)true, (boolean)true, (boolean)true);
        GlStateManager.func_179141_d();
        GlStateManager.func_179147_l();
        GL11.glAlphaFunc((int)516, (float)0.0f);
    }

    public static void write(boolean renderClipLayer) {
        Stencil.checkSetupFBO();
        GL11.glClearStencil((int)0);
        GL11.glClear((int)1024);
        GL11.glEnable((int)2960);
        GL11.glStencilFunc((int)519, (int)1, (int)65535);
        GL11.glStencilOp((int)7680, (int)7680, (int)7681);
        if (!renderClipLayer) {
            GlStateManager.func_179135_a((boolean)false, (boolean)false, (boolean)false, (boolean)false);
        }
    }

    public static void write(boolean renderClipLayer, Framebuffer fb, boolean clearStencil, boolean invert) {
        Stencil.checkSetupFBO(fb);
        if (clearStencil) {
            GL11.glClearStencil((int)0);
            GL11.glClear((int)1024);
            GL11.glEnable((int)2960);
        }
        GL11.glStencilFunc((int)519, (int)(invert ? 0 : 1), (int)65535);
        GL11.glStencilOp((int)7680, (int)7680, (int)7681);
        if (!renderClipLayer) {
            GlStateManager.func_179135_a((boolean)false, (boolean)false, (boolean)false, (boolean)false);
        }
    }

    public static void checkSetupFBO() {
        Framebuffer fbo = mc.func_147110_a();
        if (fbo.field_147624_h > -1) {
            Stencil.setupFBO(fbo);
            fbo.field_147624_h = -1;
        }
    }

    public static void checkSetupFBO(Framebuffer fbo) {
        if (fbo != null && fbo.field_147624_h > -1) {
            Stencil.setupFBO(fbo);
            fbo.field_147624_h = -1;
        }
    }

    public static void setupFBO(Framebuffer fbo) {
        EXTFramebufferObject.glDeleteRenderbuffersEXT((int)fbo.field_147624_h);
        int stencil_depth_buffer_ID = EXTFramebufferObject.glGenRenderbuffersEXT();
        EXTFramebufferObject.glBindRenderbufferEXT((int)36161, (int)stencil_depth_buffer_ID);
        EXTFramebufferObject.glRenderbufferStorageEXT((int)36161, (int)34041, (int)Minecraft.func_71410_x().field_71443_c, (int)Minecraft.func_71410_x().field_71440_d);
        EXTFramebufferObject.glFramebufferRenderbufferEXT((int)36160, (int)36128, (int)36161, (int)stencil_depth_buffer_ID);
        EXTFramebufferObject.glFramebufferRenderbufferEXT((int)36160, (int)36096, (int)36161, (int)stencil_depth_buffer_ID);
    }
}

