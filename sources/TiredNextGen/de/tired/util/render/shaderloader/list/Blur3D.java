package de.tired.util.render.shaderloader.list;

import de.tired.util.math.MathUtil;
import de.tired.util.render.StencilUtil;

import de.tired.util.render.shaderloader.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL20.glUniform1;

@ShaderAnnoation(fragName = "blur.frag", renderType = ShaderRenderType.RENDER2D)
public class Blur3D extends ShaderProgram {

    private static Framebuffer framebuffer = new Framebuffer(1, 1, false);

    public void startBlur() {
        StencilUtil.initStencilToWrite();
    }

    public void stopBlur(Framebuffer fbo) {
        StencilUtil.readStencilBuffer(1);
        drawBlur(fbo);
        StencilUtil.uninitStencilBuffer();
    }

    public void drawBlur(Framebuffer objectFBO) {
        framebuffer = KoksFramebuffer.doFrameBuffer(framebuffer);
        MC.entityRenderer.setupOverlayRendering();
        RenderHelper.disableStandardItemLighting();
        ShaderExtension.useShader(getShaderProgramID());

        //first pass
        setUniforms(1, 0);
        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(true);
        glBindTexture(GL_TEXTURE_2D, MC.getFramebuffer().framebufferTexture);
        MC.getFramebuffer().framebufferRenderExt(MC.displayWidth, MC.displayHeight, false);
        framebuffer.unbindFramebuffer();

        //second pass
        ShaderExtension.useShader(getShaderProgramID());
        setUniforms(0, 1);
        MC.getFramebuffer().bindFramebuffer(true);
        glBindTexture(GL_TEXTURE_2D, framebuffer.framebufferTexture);
        framebuffer.framebufferRenderExt(MC.displayWidth, MC.displayHeight, false);
        ShaderExtension.deleteProgram();
        MC.entityRenderer.disableLightmap();
        GlStateManager.enableLighting();
        GlStateManager.enableRescaleNormal();
    }

    private void setUniforms(int xAxis, int yAxis) {
        setShaderUniformI("currentTexture", 0);
        setShaderUniformI("currentTexture2", 16);
        setShaderUniform("texelSize", (float) (1.5 / MC.displayWidth), (float) (1.5 / MC.displayHeight));
        setShaderUniform("coords", xAxis, yAxis);
        final FloatBuffer buffer = BufferUtils.createFloatBuffer(40);
        for (int i = 1; i <= 40; i++) {
            buffer.put(MathUtil.calculateGaussianValue(i, 20 / 2));
        }
        buffer.rewind();

        glUniform1(getUniform("weights"), buffer);

        setShaderUniform("pixelSkippingAmount", 212);
        setShaderUniform("blurRadius", 15);

    }


}
