// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.utils.render;

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

public class BlurUtil
{
    private static final Minecraft mc;
    private static ShaderGroup shaderGroup;
    private static Framebuffer framebuffer;
    private static int lastFactor;
    private static int lastWidth;
    private static int lastHeight;
    
    static {
        mc = Minecraft.getMinecraft();
    }
    
    public static void init() {
        try {
            (BlurUtil.shaderGroup = new ShaderGroup(BlurUtil.mc.getTextureManager(), BlurUtil.mc.getResourceManager(), BlurUtil.mc.getFramebuffer(), new ResourceLocation("Lavish/blur.json"))).createBindFramebuffers(BlurUtil.mc.displayWidth, BlurUtil.mc.displayHeight);
            BlurUtil.framebuffer = BlurUtil.shaderGroup.mainFramebuffer;
        }
        catch (JsonSyntaxException | IOException ex2) {
            final Exception ex;
            final Exception e = ex;
            e.printStackTrace();
        }
    }
    
    private static void setValues(final int strength) {
        for (int i = 0; i < 3; ++i) {
            BlurUtil.shaderGroup.getShaders().get(i).getShaderManager().getShaderUniform("Radius").set((float)strength);
        }
    }
    
    public static void blur(final double x, final double y, final double areaWidth, final double areaHeight, final int blurStrength) {
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        Stencil.write(false);
        Render.drawRoundedRect(x, y, areaWidth, areaHeight, 1.0f);
        Stencil.erase(true);
        GlStateManager.enableBlend();
        blur(blurStrength);
        Stencil.dispose();
    }
    
    private static boolean sizeHasChanged(final int scaleFactor, final int width, final int height) {
        return BlurUtil.lastFactor != scaleFactor || BlurUtil.lastWidth != width || BlurUtil.lastHeight != height;
    }
    
    public static void blur(final int blurStrength) {
        final ScaledResolution scaledResolution = new ScaledResolution(BlurUtil.mc);
        final int scaleFactor = scaledResolution.getScaleFactor();
        final int width = scaledResolution.getScaledWidth();
        final int height = scaledResolution.getScaledHeight();
        if (sizeHasChanged(scaleFactor, width, height) || BlurUtil.framebuffer == null || BlurUtil.shaderGroup == null) {
            init();
        }
        BlurUtil.lastFactor = scaleFactor;
        BlurUtil.lastWidth = width;
        BlurUtil.lastHeight = height;
        setValues(blurStrength);
        BlurUtil.framebuffer.bindFramebuffer(true);
        BlurUtil.shaderGroup.loadShaderGroup(BlurUtil.mc.timer.renderPartialTicks);
        BlurUtil.mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.enableAlpha();
    }
    
    public static void blurMove(final double x, final double y, final double areaWidth, final double areaHeight, final int blurStrength) {
        final ScaledResolution scaledResolution = new ScaledResolution(BlurUtil.mc);
        final int scaleFactor = scaledResolution.getScaleFactor();
        final int width = scaledResolution.getScaledWidth();
        final int height = scaledResolution.getScaledHeight();
        if (sizeHasChanged(scaleFactor, width, height) || BlurUtil.framebuffer == null || BlurUtil.shaderGroup == null) {
            init();
        }
        BlurUtil.lastFactor = scaleFactor;
        BlurUtil.lastWidth = width;
        BlurUtil.lastHeight = height;
        setValues(blurStrength);
        BlurUtil.framebuffer.bindFramebuffer(true);
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        Stencil.write(false);
        Render.drawRoundedRect(x, y, areaWidth, areaHeight, 1.0f);
        Stencil.erase(true);
        GlStateManager.enableBlend();
        BlurUtil.shaderGroup.loadShaderGroup(BlurUtil.mc.timer.renderPartialTicks);
        Stencil.dispose();
        BlurUtil.shaderGroup.loadShaderGroup(BlurUtil.mc.timer.renderPartialTicks);
        BlurUtil.mc.getFramebuffer().bindFramebuffer(true);
        GL11.glDisable(3089);
        BlurUtil.mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.enableAlpha();
    }
}
