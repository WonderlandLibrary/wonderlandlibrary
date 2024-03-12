// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.render;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import java.awt.Color;

public class RoundedUtil
{
    public static ShaderUtil roundedShader;
    public static ShaderUtil roundedOutlineShader;
    private static final ShaderUtil roundedTexturedShader;
    private static final ShaderUtil roundedGradientShader;
    
    public static void drawRound(final float x, final float y, final float width, final float height, final float radius, final Color color) {
        drawRound(x, y, width, height, radius, false, color);
    }
    
    public static void drawGradientHorizontal(final float x, final float y, final float width, final float height, final float radius, final Color left, final Color right) {
        drawGradientRound(x, y, width, height, radius, left, left, right, right);
    }
    
    public static void drawGradientVertical(final float x, final float y, final float width, final float height, final float radius, final Color top, final Color bottom) {
        drawGradientRound(x, y, width, height, radius, bottom, top, bottom, top);
    }
    
    public static void drawGradientCornerLR(final float x, final float y, final float width, final float height, final float radius, final Color topLeft, final Color bottomRight) {
        final Color mixedColor = ColorUtil.interpolateColorC(topLeft, bottomRight, 0.5f);
        drawGradientRound(x, y, width, height, radius, mixedColor, topLeft, bottomRight, mixedColor);
    }
    
    public static void drawGradientCornerRL(final float x, final float y, final float width, final float height, final float radius, final Color bottomLeft, final Color topRight) {
        final Color mixedColor = ColorUtil.interpolateColorC(topRight, bottomLeft, 0.5f);
        drawGradientRound(x, y, width, height, radius, bottomLeft, mixedColor, mixedColor, topRight);
    }
    
    public static void drawGradientRound(final float x, final float y, final float width, final float height, final float radius, final Color bottomLeft, final Color topLeft, final Color bottomRight, final Color topRight) {
        RenderUtil.setAlphaLimit(0.0f);
        RenderUtil.resetColor();
        GLUtil.startBlend();
        RoundedUtil.roundedGradientShader.init();
        setupRoundedRectUniforms(x, y, width, height, radius, RoundedUtil.roundedGradientShader);
        RoundedUtil.roundedGradientShader.setUniformf("color1", topLeft.getRed() / 255.0f, topLeft.getGreen() / 255.0f, topLeft.getBlue() / 255.0f, topLeft.getAlpha() / 255.0f);
        RoundedUtil.roundedGradientShader.setUniformf("color2", bottomLeft.getRed() / 255.0f, bottomLeft.getGreen() / 255.0f, bottomLeft.getBlue() / 255.0f, bottomLeft.getAlpha() / 255.0f);
        RoundedUtil.roundedGradientShader.setUniformf("color3", topRight.getRed() / 255.0f, topRight.getGreen() / 255.0f, topRight.getBlue() / 255.0f, topRight.getAlpha() / 255.0f);
        RoundedUtil.roundedGradientShader.setUniformf("color4", bottomRight.getRed() / 255.0f, bottomRight.getGreen() / 255.0f, bottomRight.getBlue() / 255.0f, bottomRight.getAlpha() / 255.0f);
        ShaderUtil.drawQuads(x - 1.0f, y - 1.0f, width + 2.0f, height + 2.0f);
        RoundedUtil.roundedGradientShader.unload();
        GLUtil.endBlend();
    }
    
    public static void drawRound(final float x, final float y, final float width, final float height, final float radius, final boolean blur, final Color color) {
        RenderUtil.resetColor();
        GLUtil.startBlend();
        GL11.glBlendFunc(770, 771);
        RenderUtil.setAlphaLimit(0.0f);
        RoundedUtil.roundedShader.init();
        setupRoundedRectUniforms(x, y, width, height, radius, RoundedUtil.roundedShader);
        RoundedUtil.roundedShader.setUniformi("blur", blur ? 1 : 0);
        RoundedUtil.roundedShader.setUniformf("color", color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
        ShaderUtil.drawQuads(x - 1.0f, y - 1.0f, width + 2.0f, height + 2.0f);
        RoundedUtil.roundedShader.unload();
        GLUtil.endBlend();
    }
    
    public static void drawRoundOutline(final float x, final float y, final float width, final float height, final float radius, final float outlineThickness, final Color color, final Color outlineColor) {
        RenderUtil.resetColor();
        GLUtil.startBlend();
        GL11.glBlendFunc(770, 771);
        RenderUtil.setAlphaLimit(0.0f);
        RoundedUtil.roundedOutlineShader.init();
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        setupRoundedRectUniforms(x, y, width, height, radius, RoundedUtil.roundedOutlineShader);
        RoundedUtil.roundedOutlineShader.setUniformf("outlineThickness", outlineThickness * sr.getScaleFactor());
        RoundedUtil.roundedOutlineShader.setUniformf("color", color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
        RoundedUtil.roundedOutlineShader.setUniformf("outlineColor", outlineColor.getRed() / 255.0f, outlineColor.getGreen() / 255.0f, outlineColor.getBlue() / 255.0f, outlineColor.getAlpha() / 255.0f);
        ShaderUtil.drawQuads(x - (2.0f + outlineThickness), y - (2.0f + outlineThickness), width + (4.0f + outlineThickness * 2.0f), height + (4.0f + outlineThickness * 2.0f));
        RoundedUtil.roundedOutlineShader.unload();
        GLUtil.endBlend();
    }
    
    public static void drawRoundTextured(final float x, final float y, final float width, final float height, final float radius, final float alpha) {
        RenderUtil.resetColor();
        RenderUtil.setAlphaLimit(0.0f);
        GLUtil.startBlend();
        RoundedUtil.roundedTexturedShader.init();
        RoundedUtil.roundedTexturedShader.setUniformi("textureIn", 0);
        setupRoundedRectUniforms(x, y, width, height, radius, RoundedUtil.roundedTexturedShader);
        RoundedUtil.roundedTexturedShader.setUniformf("alpha", alpha);
        ShaderUtil.drawQuads(x - 1.0f, y - 1.0f, width + 2.0f, height + 2.0f);
        RoundedUtil.roundedTexturedShader.unload();
        GLUtil.endBlend();
    }
    
    private static void setupRoundedRectUniforms(final float x, final float y, final float width, final float height, final float radius, final ShaderUtil roundedTexturedShader) {
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        roundedTexturedShader.setUniformf("location", x * sr.getScaleFactor(), Minecraft.getMinecraft().displayHeight - height * sr.getScaleFactor() - y * sr.getScaleFactor());
        roundedTexturedShader.setUniformf("rectSize", width * sr.getScaleFactor(), height * sr.getScaleFactor());
        roundedTexturedShader.setUniformf("radius", radius * sr.getScaleFactor());
    }
    
    static {
        RoundedUtil.roundedShader = new ShaderUtil("roundedRect");
        RoundedUtil.roundedOutlineShader = new ShaderUtil("roundRectOutline");
        roundedTexturedShader = new ShaderUtil("roundRectTexture");
        roundedGradientShader = new ShaderUtil("roundedRectGradient");
    }
}
