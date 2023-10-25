package org.dreamcore.client.helpers.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class BlurUtil {

    protected static Minecraft mc = Minecraft.getInstance();
    private final ResourceLocation resourceLocation = new ResourceLocation("dreamcore/shaders/fragment/blur.json");
    private ShaderGroup shaderGroup;
    private Framebuffer framebuffer;

    private int lastFactor;
    private int lastWidth;
    private int lastHeight;

    public void init() {
        try {
            shaderGroup = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), resourceLocation);
            shaderGroup.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
            framebuffer = shaderGroup.mainFramebuffer;

        } catch (Exception e) {
        }
    }

    public void blur(float xBlur, float yBlur, float widthBlur, float heightBlur, int strength) {
        ScaledResolution scaledResolution = new ScaledResolution(mc);

        int scaleFactor = scaledResolution.getScaleFactor();
        int width = scaledResolution.getScaledWidth();
        int height = scaledResolution.getScaledHeight();

        if (sizeHasChanged(scaleFactor, width, height) || framebuffer == null || shaderGroup == null) {
            init();
        }

        lastFactor = scaleFactor;
        lastWidth = width;
        lastHeight = height;

        GL11.glPushMatrix();

        GL11.glEnable(GL11.GL_SCISSOR_TEST);

        RenderHelper.scissorRect(xBlur, yBlur, widthBlur, heightBlur);

        framebuffer.bindFramebuffer(true);
        shaderGroup.loadShaderGroup(mc.timer.renderPartialTicks);

        for (int i = 0; i < 3; i++) {
            shaderGroup.getShaders().get(i).getShaderManager().getShaderUniform("Radius").set(strength);
        }

        mc.getFramebuffer().bindFramebuffer(false);

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopMatrix();
    }

    private boolean sizeHasChanged(int scaleFactor, int width, int height) {
        return (lastFactor != scaleFactor || lastWidth != width || lastHeight != height);
    }
}