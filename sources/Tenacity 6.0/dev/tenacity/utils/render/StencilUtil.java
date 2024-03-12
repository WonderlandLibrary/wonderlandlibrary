// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.render;

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
