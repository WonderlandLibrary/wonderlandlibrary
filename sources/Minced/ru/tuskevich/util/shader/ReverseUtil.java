// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.shader;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;

public class ReverseUtil
{
    public static ShaderUtility shader;
    public static Framebuffer framebuffer;
    
    public static void setupUniforms(final float time) {
        ReverseUtil.shader.setUniformi("textureIn", 0);
        ReverseUtil.shader.setUniformf("time", time);
    }
    
    public static void renderReerse() {
        GlStateManager.enableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        (ReverseUtil.framebuffer = ShaderUtility.createFrameBuffer(ReverseUtil.framebuffer)).framebufferClear();
        ReverseUtil.framebuffer.bindFramebuffer(true);
        ReverseUtil.shader.init();
        setupUniforms((float)Math.sin(System.nanoTime() / 1.0E9f));
        ShaderUtility.bindTexture(ShaderUtility.mc.getFramebuffer().framebufferTexture);
        ShaderUtility.drawQuads();
        ReverseUtil.framebuffer.unbindFramebuffer();
        ReverseUtil.shader.unload();
        ShaderUtility.mc.getFramebuffer().bindFramebuffer(true);
        ReverseUtil.shader.init();
        setupUniforms(System.nanoTime() / 1.0E9f);
        ShaderUtility.bindTexture(ReverseUtil.framebuffer.framebufferTexture);
        ShaderUtility.drawQuads();
        ReverseUtil.shader.unload();
        GlStateManager.resetColor();
        GlStateManager.bindTexture(0);
    }
    
    static {
        ReverseUtil.shader = new ShaderUtility("client/shaders/glitch.frag");
        ReverseUtil.framebuffer = new Framebuffer(1, 1, false);
    }
}
