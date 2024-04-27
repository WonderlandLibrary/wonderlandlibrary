package me.utils.render;

import java.awt.Color;
import net.ccbluex.liquidbounce.utils.blur.ShaderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class RoundedUtil {
    public static ShaderUtil roundedShader = new ShaderUtil("roundedRect");
    public static ShaderUtil roundedOutlineShader = new ShaderUtil("tShaders/roundRectOutline.frag");
    private static final ShaderUtil roundedTexturedShader = new ShaderUtil("Shaders/roundRectTextured.frag");
    private static final ShaderUtil roundedGradientShader = new ShaderUtil("roundedRectGradient");

    public static void resetColor() {
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void drawRound(float x, float y, float width, float height, float radius, Color color) {
        RoundedUtil.drawRound(x, y, width, height, radius, false, color);
    }

    public static void drawRound1(float x, float y, float width, float height, float radius, Color color) {
        RoundedUtil.drawRound(x, y, width, height, radius, false, color);
    }

    public static void drawRoundScale(float x, float y, float width, float height, float radius, Color color, float scale) {
        RoundedUtil.drawRound(x + width - width * scale, y + height / 2.0f - height / 2.0f * scale, width * scale, height * scale, radius, false, color);
    }

    public static void drawGradientHorizontal(float x, float y, float width, float height, float radius, Color left, Color right) {
        RoundedUtil.drawGradientRound(x, y, width, height, radius, left, left, right, right);
    }

    public static void drawGradientVertical(float x, float y, float width, float height, float radius, Color top, Color bottom) {
        RoundedUtil.drawGradientRound(x, y, width, height, radius, bottom, top, bottom, top);
    }

    public static void drawGradientRound(float x, float y, float width, float height, float radius, Color bottomLeft, Color topLeft, Color bottomRight, Color topRight) {
        RoundedUtil.resetColor();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc((int)770, (int)771);
        roundedGradientShader.init();
        RoundedUtil.setupRoundedRectUniforms(x, y, width, height, radius, roundedGradientShader);
        roundedGradientShader.setUniformf("color1", (float)bottomLeft.getRed() / 255.0f, (float)bottomLeft.getGreen() / 255.0f, (float)bottomLeft.getBlue() / 255.0f, (float)bottomLeft.getAlpha() / 255.0f);
        roundedGradientShader.setUniformf("color2", (float)topLeft.getRed() / 255.0f, (float)topLeft.getGreen() / 255.0f, (float)topLeft.getBlue() / 255.0f, (float)topLeft.getAlpha() / 255.0f);
        roundedGradientShader.setUniformf("color3", (float)bottomRight.getRed() / 255.0f, (float)bottomRight.getGreen() / 255.0f, (float)bottomRight.getBlue() / 255.0f, (float)bottomRight.getAlpha() / 255.0f);
        roundedGradientShader.setUniformf("color4", (float)topRight.getRed() / 255.0f, (float)topRight.getGreen() / 255.0f, (float)topRight.getBlue() / 255.0f, (float)topRight.getAlpha() / 255.0f);
        RoundedUtil.drawQuads(x - 1.0f, y - 1.0f, width + 2.0f, height + 2.0f);
        roundedGradientShader.unload();
        GlStateManager.disableBlend();
    }

    public static void drawRound(float x, float y, float width, float height, float radius, boolean blur, Color color) {
        RoundedUtil.resetColor();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc((int)770, (int)771);
        roundedShader.init();
        RoundedUtil.setupRoundedRectUniforms(x, y, width, height, radius, roundedShader);
        roundedShader.setUniformi("blur", blur ? 1 : 0);
        roundedShader.setUniformf("color", (float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
        RoundedUtil.drawQuads(x - 1.0f, y - 1.0f, width + 2.0f, height + 2.0f);
        roundedShader.unload();
        GlStateManager.disableBlend();
    }

    public static void drawRoundOutline(float x, float y, float width, float height, float radius, float outlineThickness, Color color, Color outlineColor) {
        RoundedUtil.resetColor();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc((int)770, (int)771);
        roundedOutlineShader.init();
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        RoundedUtil.setupRoundedRectUniforms(x, y, width, height, radius, roundedOutlineShader);
        roundedOutlineShader.setUniformf("outlineThickness", outlineThickness * (float)sr.getScaleFactor());
        roundedOutlineShader.setUniformf("color", (float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
        roundedOutlineShader.setUniformf("outlineColor", (float)outlineColor.getRed() / 255.0f, (float)outlineColor.getGreen() / 255.0f, (float)outlineColor.getBlue() / 255.0f, (float)outlineColor.getAlpha() / 255.0f);
        RoundedUtil.drawQuads(x - (2.0f + outlineThickness), y - (2.0f + outlineThickness), width + (4.0f + outlineThickness * 2.0f), height + (4.0f + outlineThickness * 2.0f));
        roundedOutlineShader.unload();
        GlStateManager.disableBlend();
    }

    public static void drawQuads(float x, float y, float widht, float height) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        GL11.glBegin((int)7);
        GL11.glTexCoord2f((float)0.0f, (float)1.0f);
        GL11.glVertex2f((float)0.0f, (float)0.0f);
        GL11.glTexCoord2f((float)0.0f, (float)0.0f);
        GL11.glVertex2f((float)0.0f, (float)height);
        GL11.glTexCoord2f((float)1.0f, (float)0.0f);
        GL11.glVertex2f((float)widht, (float)height);
        GL11.glTexCoord2f((float)1.0f, (float)1.0f);
        GL11.glVertex2f((float)widht, (float)0.0f);
        GL11.glEnd();
    }

    public static void drawRoundTextured(float x, float y, float width, float height, float radius, float alpha) {
        RoundedUtil.resetColor();
        roundedTexturedShader.init();
        roundedTexturedShader.setUniformi("textureIn", 0);
        RoundedUtil.setupRoundedRectUniforms(x, y, width, height, radius, roundedTexturedShader);
        roundedTexturedShader.setUniformf("alpha", alpha);
        RoundedUtil.drawQuads(x - 1.0f, y - 1.0f, width + 2.0f, height + 2.0f);
        roundedTexturedShader.unload();
        GlStateManager.disableBlend();
    }

    private static void setupRoundedRectUniforms(float x, float y, float width, float height, float radius, ShaderUtil roundedTexturedShader) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        roundedTexturedShader.setUniformf("location", x * (float)sr.getScaleFactor(), (float)Minecraft.getMinecraft().displayHeight - height * (float)sr.getScaleFactor() - y * (float)sr.getScaleFactor());
        roundedTexturedShader.setUniformf("rectSize", width * (float)sr.getScaleFactor(), height * (float)sr.getScaleFactor());
        roundedTexturedShader.setUniformf("radius", radius * (float)sr.getScaleFactor());
    }
}
