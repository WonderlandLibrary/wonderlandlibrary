// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.ScaledResolution;
import java.awt.Color;
import ru.tuskevich.util.shader.ShaderUtility;
import ru.tuskevich.util.Utility;

public class GradientUtility implements Utility
{
    private static final ShaderUtility gradientMaskShader;
    private static final ShaderUtility gradientShader;
    
    public static void drawGradient(final float x, final float y, final float width, final float height, final float alpha, final Color bottomLeft, final Color topLeft, final Color bottomRight, final Color topRight) {
        final ScaledResolution sr = new ScaledResolution(GradientUtility.mc);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GradientUtility.gradientShader.init();
        GradientUtility.gradientShader.setUniformf("location", x * ScaledResolution.getScaleFactor(), Minecraft.getMinecraft().displayHeight - height * ScaledResolution.getScaleFactor() - y * ScaledResolution.getScaleFactor());
        GradientUtility.gradientShader.setUniformf("rectSize", width * ScaledResolution.getScaleFactor(), height * ScaledResolution.getScaleFactor());
        GradientUtility.gradientShader.setUniformf("alpha", alpha);
        GradientUtility.gradientShader.setUniformf("color1", bottomLeft.getRed() / 255.0f, bottomLeft.getGreen() / 255.0f, bottomLeft.getBlue() / 255.0f);
        GradientUtility.gradientShader.setUniformf("color2", topLeft.getRed() / 255.0f, topLeft.getGreen() / 255.0f, topLeft.getBlue() / 255.0f);
        GradientUtility.gradientShader.setUniformf("color3", bottomRight.getRed() / 255.0f, bottomRight.getGreen() / 255.0f, bottomRight.getBlue() / 255.0f);
        GradientUtility.gradientShader.setUniformf("color4", topRight.getRed() / 255.0f, topRight.getGreen() / 255.0f, topRight.getBlue() / 255.0f);
        ShaderUtility.drawQuads(x, y, width, height);
        GradientUtility.gradientShader.unload();
        GlStateManager.disableBlend();
    }
    
    public static void drawGradientLR(final float x, final float y, final float width, final float height, final float alpha, final Color left, final Color right) {
        drawGradient(x, y, width, height, alpha, left, left, right, right);
    }
    
    public static void drawGradientTB(final float x, final float y, final float width, final float height, final float alpha, final Color top, final Color bottom) {
        drawGradient(x, y, width, height, alpha, bottom, top, bottom, top);
    }
    
    public static void applyGradientHorizontal(final float x, final float y, final float width, final float height, final float alpha, final Color left, final Color right, final Runnable content) {
        applyGradient(x, y, width, height, alpha, left, left, right, right, content);
    }
    
    public static void applyGradientVertical(final float x, final float y, final float width, final float height, final float alpha, final Color top, final Color bottom, final Runnable content) {
        applyGradient(x, y, width, height, alpha, bottom, top, bottom, top, content);
    }
    
    public static void applyGradient(final float x, final float y, final float width, final float height, final float alpha, final Color bottomLeft, final Color topLeft, final Color bottomRight, final Color topRight, final Runnable content) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GradientUtility.gradientMaskShader.init();
        final ScaledResolution sr = new ScaledResolution(GradientUtility.mc);
        GradientUtility.gradientMaskShader.setUniformf("location", x * ScaledResolution.getScaleFactor(), Minecraft.getMinecraft().displayHeight - height * ScaledResolution.getScaleFactor() - y * ScaledResolution.getScaleFactor());
        GradientUtility.gradientMaskShader.setUniformf("rectSize", width * ScaledResolution.getScaleFactor(), height * ScaledResolution.getScaleFactor());
        GradientUtility.gradientMaskShader.setUniformf("alpha", alpha);
        GradientUtility.gradientMaskShader.setUniformi("tex", 0);
        GradientUtility.gradientMaskShader.setUniformf("color1", bottomLeft.getRed() / 255.0f, bottomLeft.getGreen() / 255.0f, bottomLeft.getBlue() / 255.0f);
        GradientUtility.gradientMaskShader.setUniformf("color2", topLeft.getRed() / 255.0f, topLeft.getGreen() / 255.0f, topLeft.getBlue() / 255.0f);
        GradientUtility.gradientMaskShader.setUniformf("color3", bottomRight.getRed() / 255.0f, bottomRight.getGreen() / 255.0f, bottomRight.getBlue() / 255.0f);
        GradientUtility.gradientMaskShader.setUniformf("color4", topRight.getRed() / 255.0f, topRight.getGreen() / 255.0f, topRight.getBlue() / 255.0f);
        content.run();
        GradientUtility.gradientMaskShader.unload();
        GlStateManager.disableBlend();
    }
    
    static {
        gradientMaskShader = new ShaderUtility("shaders/gradientMask.frag");
        gradientShader = new ShaderUtility("shaders/gradient.frag");
    }
}
