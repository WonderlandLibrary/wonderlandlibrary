// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import java.awt.Color;
import dev.tenacity.utils.Utils;

public class GradientUtil implements Utils
{
    private static final ShaderUtil gradientMaskShader;
    private static final ShaderUtil gradientShader;
    
    public static void drawGradient(final float x, final float y, final float width, final float height, final float alpha, final Color bottomLeft, final Color topLeft, final Color bottomRight, final Color topRight) {
        final ScaledResolution sr = new ScaledResolution(GradientUtil.mc);
        RenderUtil.setAlphaLimit(0.0f);
        RenderUtil.resetColor();
        GLUtil.startBlend();
        GradientUtil.gradientShader.init();
        GradientUtil.gradientShader.setUniformf("location", x * sr.getScaleFactor(), Minecraft.getMinecraft().displayHeight - height * sr.getScaleFactor() - y * sr.getScaleFactor());
        GradientUtil.gradientShader.setUniformf("rectSize", width * sr.getScaleFactor(), height * sr.getScaleFactor());
        GradientUtil.gradientShader.setUniformf("color1", bottomLeft.getRed() / 255.0f, bottomLeft.getGreen() / 255.0f, bottomLeft.getBlue() / 255.0f, alpha);
        GradientUtil.gradientShader.setUniformf("color2", topLeft.getRed() / 255.0f, topLeft.getGreen() / 255.0f, topLeft.getBlue() / 255.0f, alpha);
        GradientUtil.gradientShader.setUniformf("color3", bottomRight.getRed() / 255.0f, bottomRight.getGreen() / 255.0f, bottomRight.getBlue() / 255.0f, alpha);
        GradientUtil.gradientShader.setUniformf("color4", topRight.getRed() / 255.0f, topRight.getGreen() / 255.0f, topRight.getBlue() / 255.0f, alpha);
        ShaderUtil.drawQuads(x, y, width, height);
        GradientUtil.gradientShader.unload();
        GLUtil.endBlend();
    }
    
    public static void drawGradient(final float x, final float y, final float width, final float height, final Color bottomLeft, final Color topLeft, final Color bottomRight, final Color topRight) {
        final ScaledResolution sr = new ScaledResolution(GradientUtil.mc);
        RenderUtil.resetColor();
        GLUtil.startBlend();
        GradientUtil.gradientShader.init();
        GradientUtil.gradientShader.setUniformf("location", x * sr.getScaleFactor(), Minecraft.getMinecraft().displayHeight - height * sr.getScaleFactor() - y * sr.getScaleFactor());
        GradientUtil.gradientShader.setUniformf("rectSize", width * sr.getScaleFactor(), height * sr.getScaleFactor());
        GradientUtil.gradientShader.setUniformf("color1", bottomLeft.getRed() / 255.0f, bottomLeft.getGreen() / 255.0f, bottomLeft.getBlue() / 255.0f, bottomLeft.getAlpha() / 255.0f);
        GradientUtil.gradientShader.setUniformf("color2", topLeft.getRed() / 255.0f, topLeft.getGreen() / 255.0f, topLeft.getBlue() / 255.0f, topLeft.getAlpha() / 255.0f);
        GradientUtil.gradientShader.setUniformf("color3", bottomRight.getRed() / 255.0f, bottomRight.getGreen() / 255.0f, bottomRight.getBlue() / 255.0f, bottomRight.getAlpha() / 255.0f);
        GradientUtil.gradientShader.setUniformf("color4", topRight.getRed() / 255.0f, topRight.getGreen() / 255.0f, topRight.getBlue() / 255.0f, topRight.getAlpha() / 255.0f);
        ShaderUtil.drawQuads(x, y, width, height);
        GradientUtil.gradientShader.unload();
        GLUtil.endBlend();
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
    
    public static void applyGradientCornerRL(final float x, final float y, final float width, final float height, final float alpha, final Color bottomLeft, final Color topRight, final Runnable content) {
        final Color mixedColor = ColorUtil.interpolateColorC(topRight, bottomLeft, 0.5f);
        applyGradient(x, y, width, height, alpha, bottomLeft, mixedColor, mixedColor, topRight, content);
    }
    
    public static void applyGradientCornerLR(final float x, final float y, final float width, final float height, final float alpha, final Color bottomRight, final Color topLeft, final Runnable content) {
        final Color mixedColor = ColorUtil.interpolateColorC(bottomRight, topLeft, 0.5f);
        applyGradient(x, y, width, height, alpha, mixedColor, topLeft, bottomRight, mixedColor, content);
    }
    
    public static void applyGradient(final float x, final float y, final float width, final float height, final float alpha, final Color bottomLeft, final Color topLeft, final Color bottomRight, final Color topRight, final Runnable content) {
        RenderUtil.resetColor();
        GLUtil.startBlend();
        GradientUtil.gradientMaskShader.init();
        final ScaledResolution sr = new ScaledResolution(GradientUtil.mc);
        GradientUtil.gradientMaskShader.setUniformf("location", x * sr.getScaleFactor(), Minecraft.getMinecraft().displayHeight - height * sr.getScaleFactor() - y * sr.getScaleFactor());
        GradientUtil.gradientMaskShader.setUniformf("rectSize", width * sr.getScaleFactor(), height * sr.getScaleFactor());
        GradientUtil.gradientMaskShader.setUniformf("alpha", alpha);
        GradientUtil.gradientMaskShader.setUniformi("tex", 0);
        GradientUtil.gradientMaskShader.setUniformf("color1", bottomLeft.getRed() / 255.0f, bottomLeft.getGreen() / 255.0f, bottomLeft.getBlue() / 255.0f);
        GradientUtil.gradientMaskShader.setUniformf("color2", topLeft.getRed() / 255.0f, topLeft.getGreen() / 255.0f, topLeft.getBlue() / 255.0f);
        GradientUtil.gradientMaskShader.setUniformf("color3", bottomRight.getRed() / 255.0f, bottomRight.getGreen() / 255.0f, bottomRight.getBlue() / 255.0f);
        GradientUtil.gradientMaskShader.setUniformf("color4", topRight.getRed() / 255.0f, topRight.getGreen() / 255.0f, topRight.getBlue() / 255.0f);
        content.run();
        GradientUtil.gradientMaskShader.unload();
        GLUtil.endBlend();
    }
    
    static {
        gradientMaskShader = new ShaderUtil("gradientMask");
        gradientShader = new ShaderUtil("gradient");
    }
}
