package xyz.cucumber.base.utils.render;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;

import xyz.cucumber.base.utils.render.shaders.GaussianKernel;
import xyz.cucumber.base.utils.render.shaders.Shaders;

public class BloomUtils {
    private final Minecraft mc = Minecraft.getMinecraft();

    private Framebuffer inputFramebuffer = new Framebuffer(1, 1, true);
    private Framebuffer outputFramebuffer = new Framebuffer(1, 1, true);
    private GaussianKernel gaussianKernel = new GaussianKernel(0);

    private final int radius = 9;
    private final float compression = 1F;
    private final int programId = Shaders.bloom.getProgramID();

    private ScaledResolution sr = new ScaledResolution(mc);

    public void pre() {
    	sr = new ScaledResolution(mc);
        this.inputFramebuffer.bindFramebuffer(true);
    }

    public void post() {
        this.outputFramebuffer.bindFramebuffer(true);
        Shaders.bloom.startProgram();

        if (this.gaussianKernel.getSize() != radius) {
            this.gaussianKernel = new GaussianKernel(radius);
            this.gaussianKernel.compute();

            final FloatBuffer buffer = BufferUtils.createFloatBuffer(radius);
            buffer.put(this.gaussianKernel.getKernel());
            buffer.flip();

            Shaders.bloom.uniform1f(programId, "u_radius", radius);
            Shaders.bloom.uniformFB(programId, "u_kernel", buffer);
            Shaders.bloom.uniform1i(programId, "u_diffuse_sampler", 0);
            Shaders.bloom.uniform1i(programId, "u_other_sampler", 20);
        }

        Shaders.bloom.uniform2f(programId, "u_texel_size", 1.0F / sr.getScaledWidth(), 1.0F / sr.getScaledHeight());
        Shaders.bloom.uniform2f(programId, "u_direction", compression, 0.0F);

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_SRC_ALPHA);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.0F);
        inputFramebuffer.bindFramebufferTexture();
        Shaders.bloom.renderShader(0, 0, sr.getScaledWidth(), sr.getScaledHeight());

        mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Shaders.bloom.uniform2f(programId, "u_direction", 0.0F, compression);
        outputFramebuffer.bindFramebufferTexture();
        GL13.glActiveTexture(GL13.GL_TEXTURE20);
        inputFramebuffer.bindFramebufferTexture();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        Shaders.bloom.renderShader(0, 0, sr.getScaledWidth(), sr.getScaledHeight());
        GlStateManager.disableBlend();

        Shaders.bloom.stopProgram();
    }

    public void reset() {
    	ScaledResolution sr = new ScaledResolution(mc);
    	if (mc.displayWidth != inputFramebuffer.framebufferWidth || mc.displayHeight != inputFramebuffer.framebufferHeight) {
            inputFramebuffer.deleteFramebuffer();
            inputFramebuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, true);

            outputFramebuffer.deleteFramebuffer();
            outputFramebuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, true);
        } else {
            inputFramebuffer.framebufferClear();
            outputFramebuffer.framebufferClear();
        }

        inputFramebuffer.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
        outputFramebuffer.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
    }
}
