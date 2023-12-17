// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.render;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import ru.tuskevich.util.shader.ShaderUtility;
import ru.tuskevich.util.Utility;

public class GrayUtility implements Utility
{
    public static ShaderUtility blurShader;
    public static Framebuffer framebuffer;
    
    public static void setupUniforms() {
        GrayUtility.blurShader.setUniformi("textureIn", 0);
    }
    
    public static void renderGray() {
        GlStateManager.enableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        (GrayUtility.framebuffer = ShaderUtility.createFrameBuffer(GrayUtility.framebuffer)).framebufferClear();
        GrayUtility.framebuffer.bindFramebuffer(true);
        GrayUtility.blurShader.init();
        setupUniforms();
        ShaderUtility.bindTexture(GrayUtility.mc.getFramebuffer().framebufferTexture);
        ShaderUtility.drawQuads();
        GrayUtility.framebuffer.unbindFramebuffer();
        GrayUtility.blurShader.unload();
        GrayUtility.mc.getFramebuffer().bindFramebuffer(true);
        GrayUtility.blurShader.init();
        setupUniforms();
        ShaderUtility.bindTexture(GrayUtility.framebuffer.framebufferTexture);
        ShaderUtility.drawQuads();
        GrayUtility.blurShader.unload();
        GlStateManager.resetColor();
        GlStateManager.bindTexture(0);
    }
    
    static {
        GrayUtility.blurShader = new ShaderUtility("client/shaders/white.frag");
        GrayUtility.framebuffer = new Framebuffer(1, 1, false);
    }
}
