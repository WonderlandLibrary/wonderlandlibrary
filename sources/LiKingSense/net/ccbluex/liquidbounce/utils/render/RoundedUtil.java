/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 */
package net.ccbluex.liquidbounce.utils.render;

import java.awt.Color;
import net.ccbluex.liquidbounce.utils.render.ShaderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class RoundedUtil {
    private static final ShaderUtil roundedTexturedShader = new ShaderUtil("likingsense/shader/fragment/roundrecttextured.frag");
    private static final ShaderUtil roundedGradientShader = new ShaderUtil("roundedRectGradient");
    public static ShaderUtil roundedShader = new ShaderUtil("roundedRect");
    public static ShaderUtil roundedOutlineShader = new ShaderUtil("likingsense/shader/fragment/roundrectoutline.frag");

    public static void drawRound(float x, float y, float width, float height, float radius, Color color) {
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
        GlStateManager.func_179117_G();
        GlStateManager.func_179147_l();
        int n = 0;
        roundedGradientShader.init();
        RoundedUtil.setupRoundedRectUniforms(x, y, width, height, radius, roundedGradientShader);
        roundedGradientShader.setUniformf("color1", (float)bottomLeft.getRed() / 255.0f, (float)bottomLeft.getGreen() / 255.0f, (float)bottomLeft.getBlue() / 255.0f, (float)bottomLeft.getAlpha() / 255.0f);
        roundedGradientShader.setUniformf("color2", (float)topLeft.getRed() / 255.0f, (float)topLeft.getGreen() / 255.0f, (float)topLeft.getBlue() / 255.0f, (float)topLeft.getAlpha() / 255.0f);
        roundedGradientShader.setUniformf("color3", (float)bottomRight.getRed() / 255.0f, (float)bottomRight.getGreen() / 255.0f, (float)bottomRight.getBlue() / 255.0f, (float)bottomRight.getAlpha() / 255.0f);
        roundedGradientShader.setUniformf("color4", (float)topRight.getRed() / 255.0f, (float)topRight.getGreen() / 255.0f, (float)topRight.getBlue() / 255.0f, (float)topRight.getAlpha() / 255.0f);
        ShaderUtil.drawQuads(x - 1.0f, y - 1.0f, width + 2.0f, height + 2.0f);
        roundedGradientShader.unload();
        GlStateManager.func_179084_k();
    }

    public static void drawRound(float x, float y, float width, float height, float radius, boolean blur, Color color) {
        GlStateManager.func_179117_G();
        GlStateManager.func_179147_l();
        int n = 0;
        roundedShader.init();
        RoundedUtil.setupRoundedRectUniforms(x, y, width, height, radius, roundedShader);
        roundedShader.setUniformi("blur", blur ? 1 : 0);
        roundedShader.setUniformf("color", (float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
        ShaderUtil.drawQuads(x - 1.0f, y - 1.0f, width + 2.0f, height + 2.0f);
        roundedShader.unload();
        GlStateManager.func_179084_k();
    }

    public static void drawRoundOutline(float x, float y, float width, float height, float radius, float outlineThickness, Color color, Color outlineColor) {
        GlStateManager.func_179117_G();
        GlStateManager.func_179147_l();
        int n = 0;
        roundedOutlineShader.init();
        ScaledResolution sr = new ScaledResolution(Minecraft.func_71410_x());
        RoundedUtil.setupRoundedRectUniforms(x, y, width, height, radius, roundedOutlineShader);
        roundedOutlineShader.setUniformf("outlineThickness", outlineThickness * (float)sr.func_78325_e());
        roundedOutlineShader.setUniformf("color", (float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
        roundedOutlineShader.setUniformf("outlineColor", (float)outlineColor.getRed() / 255.0f, (float)outlineColor.getGreen() / 255.0f, (float)outlineColor.getBlue() / 255.0f, (float)outlineColor.getAlpha() / 255.0f);
        ShaderUtil.drawQuads(x - (2.0f + outlineThickness), y - (2.0f + outlineThickness), width + (4.0f + outlineThickness * 2.0f), height + (4.0f + outlineThickness * 2.0f));
        roundedOutlineShader.unload();
        GlStateManager.func_179084_k();
    }

    public static void drawRoundTextured(float x, float y, float width, float height, float radius, float alpha) {
        GlStateManager.func_179117_G();
        roundedTexturedShader.init();
        ShaderUtil shaderUtil = roundedTexturedShader;
        String string = "textureIn";
        int[] nArray = new int[1];
        nArray.setUniformi((String)nArray, (int[])0);
        RoundedUtil.setupRoundedRectUniforms(x, y, width, height, radius, roundedTexturedShader);
        roundedTexturedShader.setUniformf("alpha", alpha);
        ShaderUtil.drawQuads(x - 1.0f, y - 1.0f, width + 2.0f, height + 2.0f);
        roundedTexturedShader.unload();
        GlStateManager.func_179084_k();
    }

    private static void setupRoundedRectUniforms(float x, float y, float width, float height, float radius, ShaderUtil roundedTexturedShader) {
        ScaledResolution sr = new ScaledResolution(Minecraft.func_71410_x());
        roundedTexturedShader.setUniformf("location", x * (float)sr.func_78325_e(), (float)Minecraft.func_71410_x().field_71440_d - height * (float)sr.func_78325_e() - y * (float)sr.func_78325_e());
        roundedTexturedShader.setUniformf("rectSize", width * (float)sr.func_78325_e(), height * (float)sr.func_78325_e());
        roundedTexturedShader.setUniformf("radius", radius * (float)sr.func_78325_e());
    }
}

