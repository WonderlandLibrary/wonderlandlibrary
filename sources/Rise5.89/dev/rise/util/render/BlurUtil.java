package dev.rise.util.render;

import com.google.gson.JsonSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

import java.io.IOException;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
public class BlurUtil {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private final ResourceLocation resourceLocation;
    private ShaderGroup shaderGroup;
    private Framebuffer framebuffer;

    private int lastFactor;
    private int lastWidth;
    private int lastHeight;

    public BlurUtil() {
        this.resourceLocation = new ResourceLocation("rise/shader/blur.json");
    }

    public void init() {
        try {
            this.shaderGroup = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), resourceLocation);
            this.shaderGroup.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
            this.framebuffer = shaderGroup.mainFramebuffer;
        } catch (final JsonSyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    private void setValues(final int strength) {
        this.shaderGroup.getShaders().get(0).getShaderManager().getShaderUniform("Radius").set(strength);
        this.shaderGroup.getShaders().get(1).getShaderManager().getShaderUniform("Radius").set(strength);
        this.shaderGroup.getShaders().get(2).getShaderManager().getShaderUniform("Radius").set(strength);
        this.shaderGroup.getShaders().get(3).getShaderManager().getShaderUniform("Radius").set(strength);
    }

    public final void blur(final int blurStrength) {
        final ScaledResolution scaledResolution = new ScaledResolution(mc);

        final int scaleFactor = scaledResolution.getScaleFactor();
        final int width = scaledResolution.getScaledWidth();
        final int height = scaledResolution.getScaledHeight();

        if (sizeHasChanged(scaleFactor, width, height) || framebuffer == null || shaderGroup == null) {
            init();
        }

        this.lastFactor = scaleFactor;
        this.lastWidth = width;
        this.lastHeight = height;

        setValues(blurStrength);
        framebuffer.bindFramebuffer(true);
        shaderGroup.loadShaderGroup(mc.timer.renderPartialTicks);
        mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.enableAlpha();
    }

    public final void blur(final double x, final double y, final double areaWidth, final double areaHeight, final int blurStrength) {
        final ScaledResolution scaledResolution = new ScaledResolution(mc);

        final int scaleFactor = scaledResolution.getScaleFactor();
        final int width = scaledResolution.getScaledWidth();
        final int height = scaledResolution.getScaledHeight();

        if (sizeHasChanged(scaleFactor, width, height) || framebuffer == null || shaderGroup == null) {
            init();
        }

        this.lastFactor = scaleFactor;
        this.lastWidth = width;
        this.lastHeight = height;

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtil.scissor(x, y, areaWidth, areaHeight);
        framebuffer.bindFramebuffer(true);
        shaderGroup.loadShaderGroup(mc.timer.renderPartialTicks);
        setValues(blurStrength);
        mc.getFramebuffer().bindFramebuffer(false);

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    private boolean sizeHasChanged(final int scaleFactor, final int width, final int height) {
        return (lastFactor != scaleFactor || lastWidth != width || lastHeight != height);
    }

}
