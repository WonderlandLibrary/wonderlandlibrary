// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.render.blur;

import org.lwjgl.opengl.GL20;
import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL13;
import dev.tenacity.utils.misc.MathUtils;
import org.lwjgl.BufferUtils;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.GlStateManager;
import dev.tenacity.utils.render.RenderUtil;
import net.minecraft.client.shader.Framebuffer;
import dev.tenacity.utils.render.ShaderUtil;
import dev.tenacity.utils.Utils;

public class GaussianBloom implements Utils
{
    public static ShaderUtil gaussianBloom;
    public static Framebuffer framebuffer;
    
    public static void renderBlur(final int sourceTexture, final int radius, final int offset) {
        GaussianBloom.framebuffer = RenderUtil.createFrameBuffer(GaussianBloom.framebuffer);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.0f);
        GlStateManager.enableBlend();
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        final FloatBuffer weightBuffer = BufferUtils.createFloatBuffer(256);
        for (int i = 0; i <= radius; ++i) {
            weightBuffer.put(MathUtils.calculateGaussianValue((float)i, (float)radius));
        }
        weightBuffer.rewind();
        RenderUtil.setAlphaLimit(0.0f);
        GaussianBloom.framebuffer.framebufferClear();
        GaussianBloom.framebuffer.bindFramebuffer(true);
        GaussianBloom.gaussianBloom.init();
        setupUniforms(radius, offset, 0, weightBuffer);
        RenderUtil.bindTexture(sourceTexture);
        ShaderUtil.drawQuads();
        GaussianBloom.gaussianBloom.unload();
        GaussianBloom.framebuffer.unbindFramebuffer();
        GaussianBloom.mc.getFramebuffer().bindFramebuffer(true);
        GaussianBloom.gaussianBloom.init();
        setupUniforms(radius, 0, offset, weightBuffer);
        GL13.glActiveTexture(34000);
        RenderUtil.bindTexture(sourceTexture);
        GL13.glActiveTexture(33984);
        RenderUtil.bindTexture(GaussianBloom.framebuffer.framebufferTexture);
        ShaderUtil.drawQuads();
        GaussianBloom.gaussianBloom.unload();
        GlStateManager.alphaFunc(516, 0.1f);
        GlStateManager.enableAlpha();
        GlStateManager.bindTexture(0);
    }
    
    public static void setupUniforms(final int radius, final int directionX, final int directionY, final FloatBuffer weights) {
        GaussianBloom.gaussianBloom.setUniformi("inTexture", 0);
        GaussianBloom.gaussianBloom.setUniformi("textureToCheck", 16);
        GaussianBloom.gaussianBloom.setUniformf("radius", (float)radius);
        GaussianBloom.gaussianBloom.setUniformf("texelSize", 1.0f / GaussianBloom.mc.displayWidth, 1.0f / GaussianBloom.mc.displayHeight);
        GaussianBloom.gaussianBloom.setUniformf("direction", (float)directionX, (float)directionY);
        GL20.glUniform1(GaussianBloom.gaussianBloom.getUniform("weights"), weights);
    }
    
    static {
        GaussianBloom.gaussianBloom = new ShaderUtil("Tenacity/Shaders/bloom.frag");
        GaussianBloom.framebuffer = new Framebuffer(1, 1, false);
    }
}
