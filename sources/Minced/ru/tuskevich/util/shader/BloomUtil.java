// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.shader;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL20;
import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL13;
import org.lwjgl.BufferUtils;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.GlStateManager;
import java.awt.Color;
import net.minecraft.client.shader.Framebuffer;
import ru.tuskevich.util.Utility;

public class BloomUtil implements Utility
{
    public static ShaderUtility gaussianBloom;
    public static Framebuffer framebuffer;
    
    public static void renderBlur(final int sourceTexture, final int radius, final int offset, final Color c, final float des, final boolean fill) {
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.0f);
        GlStateManager.enableBlend();
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        final FloatBuffer weightBuffer = BufferUtils.createFloatBuffer(256);
        for (int i = 1; i <= radius; ++i) {
            weightBuffer.put(ShaderUtility.calculateGaussianValue((float)i, radius / 2.0f));
        }
        weightBuffer.rewind();
        setAlphaLimit(0.0f);
        BloomUtil.framebuffer.framebufferClear();
        BloomUtil.framebuffer.bindFramebuffer(true);
        BloomUtil.gaussianBloom.init();
        setupUniforms(radius, 1, 0, weightBuffer, c, des);
        ShaderUtility.bindTexture(sourceTexture);
        ShaderUtility.drawQuads();
        BloomUtil.gaussianBloom.unload();
        BloomUtil.framebuffer.unbindFramebuffer();
        BloomUtil.mc.getFramebuffer().bindFramebuffer(true);
        BloomUtil.gaussianBloom.init();
        setupUniforms(radius, 0, 1, weightBuffer, c, des);
        GL13.glActiveTexture(34000);
        ShaderUtility.bindTexture(sourceTexture);
        GL13.glActiveTexture(33984);
        ShaderUtility.bindTexture(BloomUtil.framebuffer.framebufferTexture);
        ShaderUtility.drawQuads();
        BloomUtil.gaussianBloom.unload();
        GlStateManager.alphaFunc(516, 0.0f);
        GlStateManager.enableAlpha();
        GlStateManager.bindTexture(0);
    }
    
    public static void setAlphaLimit(final float limit) {
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, (float)(limit * 0.01));
    }
    
    public static void setupUniforms(final int radius, final int directionX, final int directionY, final FloatBuffer weights, final Color c, final float des) {
        BloomUtil.gaussianBloom.setUniformi("inTexture", 0);
        BloomUtil.gaussianBloom.setUniformi("textureToCheck", 16);
        BloomUtil.gaussianBloom.setUniformf("radius", (float)radius);
        BloomUtil.gaussianBloom.setUniformf("exposure", des);
        BloomUtil.gaussianBloom.setUniformf("color", c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f);
        BloomUtil.gaussianBloom.setUniformf("texelSize", 1.0f / BloomUtil.mc.displayWidth, 1.0f / BloomUtil.mc.displayHeight);
        BloomUtil.gaussianBloom.setUniformf("direction", (float)directionX, (float)directionY);
        GL20.glUniform1(BloomUtil.gaussianBloom.getUniform("weights"), weights);
    }
    
    static {
        BloomUtil.gaussianBloom = new ShaderUtility("client/shaders/bloom.frag");
        BloomUtil.framebuffer = ShaderUtility.createFrameBuffer(new Framebuffer(new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth(), new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight(), true));
    }
}
