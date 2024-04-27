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
    private static ShaderUtil circle;
    private static final ShaderUtil roundedGradientShader;
    public static ShaderUtil roundedShader;
    private static final ShaderUtil roundedTexturedShader;
    public static ShaderUtil roundedOutlineShader;

    private static void setupRoundedRectUniforms(float f, float f2, float f3, float f4, float f5, ShaderUtil shaderUtil) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
        shaderUtil.setUniformf("location", f * (float)scaledResolution.func_78325_e(), (float)Minecraft.func_71410_x().field_71440_d - f4 * (float)scaledResolution.func_78325_e() - f2 * (float)scaledResolution.func_78325_e());
        shaderUtil.setUniformf("rectSize", f3 * (float)scaledResolution.func_78325_e(), f4 * (float)scaledResolution.func_78325_e());
        shaderUtil.setUniformf("radius", f5 * (float)scaledResolution.func_78325_e());
    }

    public static void drawGradientVertical(float f, float f2, float f3, float f4, float f5, Color color, Color color2) {
        RoundedUtil.drawGradientRound(f, f2, f3, f4, f5, color2, color, color2, color);
    }

    public static void drawGradientCircle(float f, float f2, float f3, float f4, float f5, Color color, Color color2, Color color3, Color color4) {
        GlStateManager.func_179117_G();
        GlStateManager.func_179147_l();
        GlStateManager.func_179112_b((int)770, (int)771);
        roundedGradientShader.init();
        RoundedUtil.setupRoundedRectUniforms(f, f2, f3, f4, f5, roundedGradientShader);
        roundedGradientShader.setUniformf("color1", (float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
        roundedGradientShader.setUniformf("color2", (float)color2.getRed() / 255.0f, (float)color2.getGreen() / 255.0f, (float)color2.getBlue() / 255.0f, (float)color2.getAlpha() / 255.0f);
        roundedGradientShader.setUniformf("color3", (float)color3.getRed() / 255.0f, (float)color3.getGreen() / 255.0f, (float)color3.getBlue() / 255.0f, (float)color3.getAlpha() / 255.0f);
        roundedGradientShader.setUniformf("color4", (float)color4.getRed() / 255.0f, (float)color4.getGreen() / 255.0f, (float)color4.getBlue() / 255.0f, (float)color4.getAlpha() / 255.0f);
        ShaderUtil.drawQuads(f - 1.0f, f2 - 1.0f, f3 + 2.0f, f4 + 2.0f);
        roundedGradientShader.unload();
        GlStateManager.func_179084_k();
    }

    public static void drawRoundScale(float f, float f2, float f3, float f4, float f5, Color color, float f6) {
        RoundedUtil.drawRound(f + f3 - f3 * f6, f2 + f4 / 2.0f - f4 / 2.0f * f6, f3 * f6, f4 * f6, f5, false, color);
    }

    public static void drawRoundTextured(float f, float f2, float f3, float f4, float f5, float f6) {
        GlStateManager.func_179117_G();
        roundedTexturedShader.init();
        roundedTexturedShader.setUniformi("textureIn", 0);
        RoundedUtil.setupRoundedRectUniforms(f, f2, f3, f4, f5, roundedTexturedShader);
        roundedTexturedShader.setUniformf("alpha", f6);
        ShaderUtil.drawQuads(f - 1.0f, f2 - 1.0f, f3 + 2.0f, f4 + 2.0f);
        roundedTexturedShader.unload();
        GlStateManager.func_179084_k();
    }

    static {
        roundedTexturedShader = new ShaderUtil("more/shader/fragment/roundrecttextured.frag");
        roundedGradientShader = new ShaderUtil("roundedRectGradient");
        roundedShader = new ShaderUtil("roundedRect");
        circle = new ShaderUtil("circle");
        roundedOutlineShader = new ShaderUtil("more/shader/fragment/roundrectoutline.frag");
    }

    public static void drawCircle(float f, float f2, float f3, float f4, float f5, Color color) {
        GlStateManager.func_179117_G();
        GlStateManager.func_179147_l();
        GlStateManager.func_179112_b((int)770, (int)771);
        circle.init();
        RoundedUtil.setupUniforms(f, f2, f3, f4, f5, circle);
        circle.setUniformf("color", (float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
        ShaderUtil.drawQuads(f - 1.0f, f2 - 1.0f, f3 + 2.0f, f4 + 2.0f);
        circle.unload();
        GlStateManager.func_179084_k();
    }

    public static void drawGradientRound(float f, float f2, float f3, float f4, float f5, Color color, Color color2, Color color3, Color color4) {
        GlStateManager.func_179117_G();
        GlStateManager.func_179147_l();
        GlStateManager.func_179112_b((int)770, (int)771);
        roundedGradientShader.init();
        RoundedUtil.setupRoundedRectUniforms(f, f2, f3, f4, f5, roundedGradientShader);
        roundedGradientShader.setUniformf("color1", (float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
        roundedGradientShader.setUniformf("color2", (float)color2.getRed() / 255.0f, (float)color2.getGreen() / 255.0f, (float)color2.getBlue() / 255.0f, (float)color2.getAlpha() / 255.0f);
        roundedGradientShader.setUniformf("color3", (float)color3.getRed() / 255.0f, (float)color3.getGreen() / 255.0f, (float)color3.getBlue() / 255.0f, (float)color3.getAlpha() / 255.0f);
        roundedGradientShader.setUniformf("color4", (float)color4.getRed() / 255.0f, (float)color4.getGreen() / 255.0f, (float)color4.getBlue() / 255.0f, (float)color4.getAlpha() / 255.0f);
        ShaderUtil.drawQuads(f - 1.0f, f2 - 1.0f, f3 + 2.0f, f4 + 2.0f);
        roundedGradientShader.unload();
        GlStateManager.func_179084_k();
    }

    public static void drawRoundOutline(float f, float f2, float f3, float f4, float f5, float f6, Color color, Color color2) {
        GlStateManager.func_179117_G();
        GlStateManager.func_179147_l();
        GlStateManager.func_179112_b((int)770, (int)771);
        roundedOutlineShader.init();
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
        RoundedUtil.setupRoundedRectUniforms(f, f2, f3, f4, f5, roundedOutlineShader);
        roundedOutlineShader.setUniformf("outlineThickness", f6 * (float)scaledResolution.func_78325_e());
        roundedOutlineShader.setUniformf("color", (float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
        roundedOutlineShader.setUniformf("outlineColor", (float)color2.getRed() / 255.0f, (float)color2.getGreen() / 255.0f, (float)color2.getBlue() / 255.0f, (float)color2.getAlpha() / 255.0f);
        ShaderUtil.drawQuads(f - (2.0f + f6), f2 - (2.0f + f6), f3 + (4.0f + f6 * 2.0f), f4 + (4.0f + f6 * 2.0f));
        roundedOutlineShader.unload();
        GlStateManager.func_179084_k();
    }

    public static void drawRound(float f, float f2, float f3, float f4, float f5, Color color) {
        RoundedUtil.drawRound(f, f2, f3, f4, f5, false, color);
    }

    private static void setupUniforms(float f, float f2, float f3, float f4, float f5, ShaderUtil shaderUtil) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
        shaderUtil.setUniformf("location", f * (float)scaledResolution.func_78325_e(), (float)Minecraft.func_71410_x().field_71440_d - f4 * (float)scaledResolution.func_78325_e() - f2 * (float)scaledResolution.func_78325_e());
        shaderUtil.setUniformf("rectSize", f3 * (float)scaledResolution.func_78325_e(), f4 * (float)scaledResolution.func_78325_e());
        shaderUtil.setUniformf("radius", f5 * (float)scaledResolution.func_78325_e());
    }

    public static void drawGradientHorizontal(float f, float f2, float f3, float f4, float f5, Color color, Color color2) {
        RoundedUtil.drawGradientRound(f, f2, f3, f4, f5, color, color, color2, color2);
    }

    public static void drawRound(float f, float f2, float f3, float f4, float f5, boolean bl, Color color) {
        GlStateManager.func_179117_G();
        GlStateManager.func_179147_l();
        GlStateManager.func_179112_b((int)770, (int)771);
        roundedShader.init();
        RoundedUtil.setupRoundedRectUniforms(f, f2, f3, f4, f5, roundedShader);
        roundedShader.setUniformi("blur", bl ? 1 : 0);
        roundedShader.setUniformf("color", (float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
        ShaderUtil.drawQuads(f - 1.0f, f2 - 1.0f, f3 + 2.0f, f4 + 2.0f);
        roundedShader.unload();
        GlStateManager.func_179084_k();
    }
}

