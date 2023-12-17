// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.shader;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;

public class WhiteBlackUtil
{
    public static ShaderUtility shader;
    public static Framebuffer framebuffer;
    
    public static void setupUniforms(final float force) {
        WhiteBlackUtil.shader.setUniformi("textureIn", 0);
    }
    
    public static void renderColor(final float force) {
        GlStateManager.enableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        (WhiteBlackUtil.framebuffer = ShaderUtility.createFrameBuffer(WhiteBlackUtil.framebuffer)).framebufferClear();
        WhiteBlackUtil.framebuffer.bindFramebuffer(true);
        WhiteBlackUtil.shader.init();
        setupUniforms(force);
        ShaderUtility.bindTexture(ShaderUtility.mc.getFramebuffer().framebufferTexture);
        ShaderUtility.drawQuads();
        WhiteBlackUtil.framebuffer.unbindFramebuffer();
        WhiteBlackUtil.shader.unload();
        ShaderUtility.mc.getFramebuffer().bindFramebuffer(true);
        WhiteBlackUtil.shader.init();
        setupUniforms(force);
        ShaderUtility.bindTexture(WhiteBlackUtil.framebuffer.framebufferTexture);
        ShaderUtility.drawQuads();
        WhiteBlackUtil.shader.unload();
        GlStateManager.resetColor();
        GlStateManager.bindTexture(0);
    }
    
    static {
        WhiteBlackUtil.shader = new ShaderUtility("client/shaders/white.frag");
        WhiteBlackUtil.framebuffer = new Framebuffer(1, 1, false);
    }
}
