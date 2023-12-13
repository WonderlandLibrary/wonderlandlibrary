package me.nyan.flush.utils.render;

import com.google.gson.JsonSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class BlurUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static ShaderGroup shaderGroup;
    private static Framebuffer framebuffer;

    private static int lastFactor;
    private static int lastWidth;
    private static int lastHeight;

    public static void init() {
        try {
            shaderGroup = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(),
                    new ResourceLocation("flush/blur.json"));
            shaderGroup.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
            framebuffer = shaderGroup.getMainFramebuffer();
        } catch (JsonSyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void setValues(int strength) {
        for (int i = 0; i < 3; i++) {
            shaderGroup.getShaders().get(i).getShaderManager().getShaderUniform("Radius").set(strength);
        }
    }

    public static void blur(float x, float y, float width, float height, int blurStrength) {
        GlStateManager.pushMatrix();
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtils.glScissor(x, y, x + width, y + height);
        blur(blurStrength);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();
    }

    public static void blur(int blurStrength) {
        if (!OpenGlHelper.isFramebufferEnabled()) {
            return;
        }

        ScaledResolution scaledResolution = new ScaledResolution(mc);
        final int scaleFactor = scaledResolution.getScaleFactor();
        final int width = scaledResolution.getScaledWidth();
        final int height = scaledResolution.getScaledHeight();

        if (lastFactor != scaleFactor || lastWidth != width || lastHeight != height || framebuffer == null || shaderGroup == null) {
            init();
        }

        lastFactor = scaleFactor;
        lastWidth = width;
        lastHeight = height;

        setValues(blurStrength);
        framebuffer.bindFramebuffer(true);
        shaderGroup.loadShaderGroup(mc.timer.renderPartialTicks);
        mc.getFramebuffer().bindFramebuffer(true);
    }
}