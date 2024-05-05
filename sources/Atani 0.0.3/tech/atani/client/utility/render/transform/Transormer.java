package tech.atani.client.utility.render.transform;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.GL11;
import tech.atani.client.utility.render.shader.TextureRenderer;
import tech.atani.client.utility.render.shader.util.FramebufferHelper;

public class Transormer {

    private final Minecraft mc = Minecraft.getMinecraft();
    private Framebuffer framebuffer = new Framebuffer(1, 1, false);

    public void collect() {
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        framebuffer = FramebufferHelper.doFrameBuffer(framebuffer);
        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(true);
        mc.entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 0);
    }

    public void draw() {
        mc.entityRenderer.setupOverlayRendering();
        RenderHelper.disableStandardItemLighting();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        TextureRenderer.renderFRFscreen(framebuffer);


        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        mc.getFramebuffer().bindFramebuffer(true);

        mc.entityRenderer.disableLightmap();
        RenderHelper.disableStandardItemLighting();
    }

    public void release() {
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }
}
