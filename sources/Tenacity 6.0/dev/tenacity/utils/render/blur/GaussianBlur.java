// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.render.blur;

import net.minecraft.client.renderer.GlStateManager;
import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.utils.render.StencilUtil;
import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL20;
import dev.tenacity.utils.misc.MathUtils;
import org.lwjgl.BufferUtils;
import net.minecraft.client.shader.Framebuffer;
import dev.tenacity.utils.render.ShaderUtil;
import dev.tenacity.utils.Utils;

public class GaussianBlur implements Utils
{
    private static final ShaderUtil gaussianBlur;
    private static Framebuffer framebuffer;
    
    private static void setupUniforms(final float dir1, final float dir2, final float radius) {
        GaussianBlur.gaussianBlur.setUniformi("textureIn", 0);
        GaussianBlur.gaussianBlur.setUniformf("texelSize", 1.0f / GaussianBlur.mc.displayWidth, 1.0f / GaussianBlur.mc.displayHeight);
        GaussianBlur.gaussianBlur.setUniformf("direction", dir1, dir2);
        GaussianBlur.gaussianBlur.setUniformf("radius", radius);
        final FloatBuffer weightBuffer = BufferUtils.createFloatBuffer(256);
        for (int i = 0; i <= radius; ++i) {
            weightBuffer.put(MathUtils.calculateGaussianValue((float)i, radius / 2.0f));
        }
        weightBuffer.rewind();
        GL20.glUniform1(GaussianBlur.gaussianBlur.getUniform("weights"), weightBuffer);
    }
    
    public static void startBlur() {
        StencilUtil.initStencilToWrite();
    }
    
    public static void endBlur(final float radius, final float compression) {
        StencilUtil.readStencilBuffer(1);
        (GaussianBlur.framebuffer = RenderUtil.createFrameBuffer(GaussianBlur.framebuffer)).framebufferClear();
        GaussianBlur.framebuffer.bindFramebuffer(false);
        GaussianBlur.gaussianBlur.init();
        setupUniforms(compression, 0.0f, radius);
        RenderUtil.bindTexture(GaussianBlur.mc.getFramebuffer().framebufferTexture);
        ShaderUtil.drawQuads();
        GaussianBlur.framebuffer.unbindFramebuffer();
        GaussianBlur.gaussianBlur.unload();
        GaussianBlur.mc.getFramebuffer().bindFramebuffer(false);
        GaussianBlur.gaussianBlur.init();
        setupUniforms(0.0f, compression, radius);
        RenderUtil.bindTexture(GaussianBlur.framebuffer.framebufferTexture);
        ShaderUtil.drawQuads();
        GaussianBlur.gaussianBlur.unload();
        StencilUtil.uninitStencilBuffer();
        RenderUtil.resetColor();
        GlStateManager.bindTexture(0);
    }
    
    static {
        gaussianBlur = new ShaderUtil("Tenacity/Shaders/gaussian.frag");
        GaussianBlur.framebuffer = new Framebuffer(1, 1, false);
    }
}
