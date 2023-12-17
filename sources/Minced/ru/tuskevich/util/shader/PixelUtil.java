// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.shader;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;

public class PixelUtil
{
    public static ShaderUtility shader;
    public static Framebuffer framebuffer;
    
    public static void setupUniforms() {
        PixelUtil.shader.setUniformi("textureIn", 0);
    }
    
    public static void renderReerse() {
        GlStateManager.enableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        (PixelUtil.framebuffer = ShaderUtility.createFrameBuffer(PixelUtil.framebuffer)).framebufferClear();
        PixelUtil.framebuffer.bindFramebuffer(true);
        PixelUtil.shader.init();
        setupUniforms();
        ShaderUtility.bindTexture(ShaderUtility.mc.getFramebuffer().framebufferTexture);
        ShaderUtility.drawQuads();
        PixelUtil.framebuffer.unbindFramebuffer();
        PixelUtil.shader.unload();
        ShaderUtility.mc.getFramebuffer().bindFramebuffer(true);
        PixelUtil.shader.init();
        setupUniforms();
        ShaderUtility.bindTexture(PixelUtil.framebuffer.framebufferTexture);
        ShaderUtility.drawQuads();
        PixelUtil.shader.unload();
        GlStateManager.resetColor();
        GlStateManager.bindTexture(0);
    }
    
    static {
        PixelUtil.shader = new ShaderUtility("client/shaders/pixel.frag");
        PixelUtil.framebuffer = new Framebuffer(1, 1, false);
    }
}
