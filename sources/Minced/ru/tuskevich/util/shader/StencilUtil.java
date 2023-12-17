// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.shader;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.EXTFramebufferObject;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.Minecraft;

public class StencilUtil
{
    static Minecraft mc;
    
    public static void checkSetupFBO(final Framebuffer framebuffer) {
        if (framebuffer != null && framebuffer.depthBuffer > -1) {
            setupFBO(framebuffer);
            framebuffer.depthBuffer = -1;
        }
    }
    
    public static void setupFBO(final Framebuffer framebuffer) {
        EXTFramebufferObject.glDeleteRenderbuffersEXT(framebuffer.depthBuffer);
        final int stencilDepthBufferID = EXTFramebufferObject.glGenRenderbuffersEXT();
        EXTFramebufferObject.glBindRenderbufferEXT(36161, stencilDepthBufferID);
        EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34041, StencilUtil.mc.displayWidth, StencilUtil.mc.displayHeight);
        EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, stencilDepthBufferID);
        EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, stencilDepthBufferID);
    }
    
    public static void doDispose() {
        GL11.glDisable(2960);
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
    }
    
    public static void erase(final boolean invert) {
        GL11.glStencilFunc(invert ? 514 : 517, 1, 65535);
        GL11.glStencilOp(7680, 7680, 7681);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GL11.glAlphaFunc(516, 0.0f);
    }
    
    public static void write(final boolean renderClipLayer) {
        checkSetupFBO(StencilUtil.mc.getFramebuffer());
        GL11.glClearStencil(0);
        GL11.glClear(1024);
        GL11.glEnable(2960);
        GL11.glStencilFunc(519, 1, 65535);
        GL11.glStencilOp(7680, 7680, 7681);
        if (!renderClipLayer) {
            GlStateManager.colorMask(false, false, false, false);
        }
    }
    
    public static void write() {
        StencilUtil.mc.getFramebuffer().bindFramebuffer(false);
        checkSetupFBO(StencilUtil.mc.getFramebuffer());
        GL11.glClear(1024);
        GL11.glEnable(2960);
        GL11.glStencilFunc(519, 1, 1);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glColorMask(false, false, false, false);
    }
    
    public static void initStencilToWrite() {
        StencilUtil.mc.getFramebuffer().bindFramebuffer(false);
        checkSetupFBO(StencilUtil.mc.getFramebuffer());
        GL11.glClear(1024);
        GL11.glEnable(2960);
        GL11.glStencilFunc(519, 1, 1);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glColorMask(false, false, false, false);
    }
    
    public static void readStencilBuffer(final int ref) {
        GL11.glColorMask(true, true, true, true);
        GL11.glStencilFunc(514, ref, 1);
        GL11.glStencilOp(7680, 7680, 7680);
    }
    
    public static void uninitStencilBuffer() {
        GL11.glDisable(2960);
    }
    
    static {
        StencilUtil.mc = Minecraft.getMinecraft();
    }
}
