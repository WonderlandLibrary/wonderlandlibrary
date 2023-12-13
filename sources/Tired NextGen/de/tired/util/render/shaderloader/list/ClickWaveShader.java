package de.tired.util.render.shaderloader.list;

import de.tired.base.interfaces.IHook;
import de.tired.util.render.shaderloader.KoksFramebuffer;
import de.tired.util.render.shaderloader.ShaderProgramOther;
import de.tired.util.render.shaderloader.ShaderExtension;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.GL11;

public class ClickWaveShader implements IHook {

    public ShaderProgramOther shaderProgram = new ShaderProgramOther("clickwave.glsl");
    private static Framebuffer framebuffer = new Framebuffer(1, 1, true);

    private long startTime;

    public ClickWaveShader() {
        startTime = System.currentTimeMillis();
    }


    public void doRender() {
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        framebuffer = KoksFramebuffer.doFrameBuffer(framebuffer);
        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(true);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        MC.getFramebuffer().bindFramebuffer(true);

        MC.entityRenderer.disableLightmap();
        RenderHelper.disableStandardItemLighting();
        ShaderExtension.useShader(shaderProgram.getShaderProgramID());
        setUniforms(0, 0);
        MC.entityRenderer.setupOverlayRendering();
        KoksFramebuffer.renderFRFscreen(framebuffer);
        ShaderExtension.deleteProgram();
        GlStateManager.disableBlend();
        MC.entityRenderer.disableLightmap();
        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
        GlStateManager.disableDepth();

    }

    public void stopShader(int x, int y) {


    }

    public void resetTime() {
        startTime = System.currentTimeMillis();
    }

    private void setUniforms(int x, int y) {

        final int width = MC.displayWidth, height = MC.displayHeight;

        shaderProgram.setShaderUniformI("currentTexture", 0);

        shaderProgram.setShaderUniform("shaderX", x);
        shaderProgram.setShaderUniform("shaderY", y);

        float time = (System.currentTimeMillis() - this.startTime) / 500f;
        shaderProgram.setShaderUniform("resolution", width, height);
        shaderProgram.setShaderUniform("time", time);

    }

    public void renderFRFscreen(final Framebuffer framebuffer) {
        if (MC.gameSettings.ofFastRender) return;
        final ScaledResolution resolution = new ScaledResolution(MC);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, framebuffer.framebufferTexture);
        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glTexCoord2d(0, 1);
            GL11.glVertex2d(0, 0);
            GL11.glTexCoord2d(0, 0);
            GL11.glVertex2d(0, resolution.getScaledHeight());
            GL11.glTexCoord2d(1, 0);
            GL11.glVertex2d(resolution.getScaledWidth(), resolution.getScaledHeight());
            GL11.glTexCoord2d(1, 1);
            GL11.glVertex2d(resolution.getScaledWidth(), 0);
        }
        GL11.glEnd();
        ShaderExtension.deleteProgram();
    }
}