// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.render;

import ru.tuskevich.util.shader.ShaderUtility;
import net.minecraft.client.Minecraft;
import ru.tuskevich.util.shader.BloomUtil;
import java.awt.Color;
import net.minecraft.client.shader.Shader;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.ScaledResolution;
import ru.tuskevich.util.shader.GaussianBlur;
import ru.tuskevich.util.shader.StencilUtil;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.shader.Framebuffer;
import ru.tuskevich.util.Utility;

public class BlurUtility implements Utility
{
    public static Framebuffer bloomFramebuffer;
    private static int lastScale;
    private static int lastScaleWidth;
    private static int lastScaleHeight;
    private static Framebuffer buffer;
    private static final ResourceLocation shader;
    private static ShaderGroup blurShader;
    
    public static void drawBlur(final Runnable data, final float radius) {
        StencilUtil.initStencilToWrite();
        data.run();
        StencilUtil.readStencilBuffer(1);
        GaussianBlur.renderBlur(radius);
        StencilUtil.uninitStencilBuffer();
    }
    
    public static void blurAreaBoarder(final int x, final int y, final int width, final int height, final float intensity) {
        final ScaledResolution scale = new ScaledResolution(BlurUtility.mc);
        final int factor = ScaledResolution.getScaleFactor();
        final int factor2 = scale.getScaledWidth();
        final int factor3 = scale.getScaledHeight();
        if (BlurUtility.lastScale != factor || BlurUtility.lastScaleWidth != factor2 || BlurUtility.lastScaleHeight != factor3 || BlurUtility.buffer == null || BlurUtility.blurShader == null) {
            inShaderFBO();
        }
        BlurUtility.lastScale = factor;
        BlurUtility.lastScaleWidth = factor2;
        BlurUtility.lastScaleHeight = factor3;
        GL11.glScissor(x * factor, BlurUtility.mc.displayHeight - y * factor - height * factor, width * factor, height * factor);
        GL11.glEnable(3089);
        shaderConfigFix(intensity, 1.0f, 0.0f);
        BlurUtility.buffer.bindFramebuffer(true);
        BlurUtility.blurShader.render(BlurUtility.mc.timer.renderPartialTicks);
        BlurUtility.mc.getFramebuffer().bindFramebuffer(true);
        GL11.glDisable(3089);
    }
    
    private static void shaderConfigFix(final float intensity, final float blurWidth, final float blurHeight) {
        BlurUtility.blurShader.getShaders().get(0).getShaderManager().getShaderUniform("Radius").set(intensity);
        BlurUtility.blurShader.getShaders().get(1).getShaderManager().getShaderUniform("Radius").set(intensity);
        BlurUtility.blurShader.getShaders().get(0).getShaderManager().getShaderUniform("BlurDir").set(blurWidth, blurHeight);
        BlurUtility.blurShader.getShaders().get(1).getShaderManager().getShaderUniform("BlurDir").set(blurHeight, blurWidth);
    }
    
    public static void inShaderFBO() {
        try {
            (BlurUtility.blurShader = new ShaderGroup(BlurUtility.mc.getTextureManager(), BlurUtility.mc.getResourceManager(), BlurUtility.mc.getFramebuffer(), BlurUtility.shader)).createBindFramebuffers(BlurUtility.mc.displayWidth, BlurUtility.mc.displayHeight);
            BlurUtility.buffer = BlurUtility.blurShader.mainFramebuffer;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void renderBlur(final int x, final int y, final int width, final int height, final int blurRadius) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2884);
        blurAreaBoarder(x, y, width, height, (float)blurRadius);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2884);
        GL11.glEnable(3008);
        GL11.glEnable(3553);
        GL11.glEnable(3042);
    }
    
    public static void drawShadow(final float radius, final float des, final Runnable data, final Color c) {
        BlurUtility.bloomFramebuffer.framebufferClear();
        BlurUtility.bloomFramebuffer.bindFramebuffer(true);
        data.run();
        BlurUtility.bloomFramebuffer.unbindFramebuffer();
        BloomUtil.renderBlur(BlurUtility.bloomFramebuffer.framebufferTexture, (int)radius, 1, c, des, true);
    }
    
    static {
        BlurUtility.bloomFramebuffer = ShaderUtility.createFrameBuffer(new Framebuffer(new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth(), new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight(), true));
        shader = new ResourceLocation("shaders/post/blur.json");
    }
}
