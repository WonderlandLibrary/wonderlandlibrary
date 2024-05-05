// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.util.render;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Shader;
import java.io.IOException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.Minecraft;

public class BlurUtils
{
    private static final Minecraft mc;
    private static ShaderGroup shaderGroup;
    private static Framebuffer framebuffer;
    private ShaderGroup shaderGroup2;
    private Framebuffer framebuffer2;
    private static int lastFactor;
    private static int lastWidth;
    private static int lastHeight;
    public static BlurUtils instance;
    
    public static BlurUtils getInstance() {
        return BlurUtils.instance;
    }
    
    public static void init() {
        try {
            (BlurUtils.shaderGroup = new ShaderGroup(BlurUtils.mc.getTextureManager(), BlurUtils.mc.getResourceManager(), BlurUtils.mc.getFramebuffer(), new ResourceLocation("resolute/blur.json"))).createBindFramebuffers(BlurUtils.mc.displayWidth, BlurUtils.mc.displayHeight);
            BlurUtils.framebuffer = BlurUtils.shaderGroup.mainFramebuffer;
        }
        catch (JsonSyntaxException | IOException ex2) {
            final Exception ex;
            final Exception e = ex;
            e.printStackTrace();
        }
    }
    
    private static void setValues(final int strength) {
        for (int i = 0; i < 3; ++i) {
            BlurUtils.shaderGroup.getShaders().get(i).getShaderManager().getShaderUniform("Radius").set((float)strength);
        }
    }
    
    public static void blur(final double x, final double y, final double areaWidth, final double areaHeight, final int blurStrength) {
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        Stencil.write(false);
        Draw.drawRoundedRect(x, y, areaWidth, areaHeight, 1.0f);
        Stencil.erase(true);
        GlStateManager.enableBlend();
        blur(blurStrength);
        Stencil.dispose();
    }
    
    private static boolean sizeHasChanged(final int scaleFactor, final int width, final int height) {
        return BlurUtils.lastFactor != scaleFactor || BlurUtils.lastWidth != width || BlurUtils.lastHeight != height;
    }
    
    public static void blur(final int blurStrength) {
        final ScaledResolution scaledResolution = new ScaledResolution(BlurUtils.mc);
        final int scaleFactor = scaledResolution.getScaleFactor();
        final int width = scaledResolution.getScaledWidth();
        final int height = scaledResolution.getScaledHeight();
        if (sizeHasChanged(scaleFactor, width, height) || BlurUtils.framebuffer == null || BlurUtils.shaderGroup == null) {
            init();
        }
        BlurUtils.lastFactor = scaleFactor;
        BlurUtils.lastWidth = width;
        BlurUtils.lastHeight = height;
        setValues(blurStrength);
        BlurUtils.framebuffer.bindFramebuffer(true);
        BlurUtils.shaderGroup.loadShaderGroup(BlurUtils.mc.timer.renderPartialTicks);
        BlurUtils.mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.enableAlpha();
    }
    
    public final void blurWholeScreen(final int blurStrength) {
        final ScaledResolution scaledResolution = new ScaledResolution(BlurUtils.mc);
        final int scaleFactor = scaledResolution.getScaleFactor();
        final int width = scaledResolution.getScaledWidth();
        final int height = scaledResolution.getScaledHeight();
        if (sizeHasChanged(scaleFactor, width, height) || BlurUtils.framebuffer == null || BlurUtils.shaderGroup == null) {
            init();
        }
        BlurUtils.lastFactor = scaleFactor;
        BlurUtils.lastWidth = width;
        BlurUtils.lastHeight = height;
        this.framebuffer2.bindFramebuffer(true);
        this.shaderGroup2.loadShaderGroup(BlurUtils.mc.timer.renderPartialTicks);
        this.shaderGroup2.getShaders().get(0).getShaderManager().getShaderUniform("Radius").set((float)blurStrength);
        this.shaderGroup2.getShaders().get(1).getShaderManager().getShaderUniform("Radius").set((float)blurStrength);
        BlurUtils.mc.getFramebuffer().bindFramebuffer(false);
    }
    
    public static void blurMove(final double x, final double y, final double areaWidth, final double areaHeight, final int blurStrength) {
        final ScaledResolution scaledResolution = new ScaledResolution(BlurUtils.mc);
        final int scaleFactor = scaledResolution.getScaleFactor();
        final int width = scaledResolution.getScaledWidth();
        final int height = scaledResolution.getScaledHeight();
        if (sizeHasChanged(scaleFactor, width, height) || BlurUtils.framebuffer == null || BlurUtils.shaderGroup == null) {
            init();
        }
        BlurUtils.lastFactor = scaleFactor;
        BlurUtils.lastWidth = width;
        BlurUtils.lastHeight = height;
        setValues(blurStrength);
        BlurUtils.framebuffer.bindFramebuffer(true);
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        Stencil.write(false);
        Draw.drawRoundedRect(x, y, areaWidth, areaHeight, 1.0f);
        Stencil.erase(true);
        GlStateManager.enableBlend();
        BlurUtils.shaderGroup.loadShaderGroup(BlurUtils.mc.timer.renderPartialTicks);
        Stencil.dispose();
        BlurUtils.shaderGroup.loadShaderGroup(BlurUtils.mc.timer.renderPartialTicks);
        BlurUtils.mc.getFramebuffer().bindFramebuffer(true);
        GL11.glDisable(3089);
        BlurUtils.mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.enableAlpha();
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
}
