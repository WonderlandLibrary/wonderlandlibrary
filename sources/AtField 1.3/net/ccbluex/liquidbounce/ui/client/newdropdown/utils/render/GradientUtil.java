/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 */
package net.ccbluex.liquidbounce.ui.client.newdropdown.utils.render;

import java.awt.Color;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.normal.Utils;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.render.DrRenderUtils;
import net.ccbluex.liquidbounce.utils.render.blur.ShaderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class GradientUtil
implements Utils {
    private static final ShaderUtil gradientShader;
    private static final ShaderUtil gradientMaskShader;

    public static void drawGradient(float f, float f2, float f3, float f4, float f5, Color color, Color color2, Color color3, Color color4) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
        DrRenderUtils.resetColor();
        GlStateManager.func_179147_l();
        GlStateManager.func_179112_b((int)770, (int)771);
        gradientShader.init();
        gradientShader.setUniformf("location", f * (float)scaledResolution.func_78325_e(), (float)Minecraft.func_71410_x().field_71440_d - f4 * (float)scaledResolution.func_78325_e() - f2 * (float)scaledResolution.func_78325_e());
        gradientShader.setUniformf("rectSize", f3 * (float)scaledResolution.func_78325_e(), f4 * (float)scaledResolution.func_78325_e());
        gradientShader.setUniformf("alpha", f5);
        gradientShader.setUniformf("color1", (float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f);
        gradientShader.setUniformf("color2", (float)color2.getRed() / 255.0f, (float)color2.getGreen() / 255.0f, (float)color2.getBlue() / 255.0f);
        gradientShader.setUniformf("color3", (float)color3.getRed() / 255.0f, (float)color3.getGreen() / 255.0f, (float)color3.getBlue() / 255.0f);
        gradientShader.setUniformf("color4", (float)color4.getRed() / 255.0f, (float)color4.getGreen() / 255.0f, (float)color4.getBlue() / 255.0f);
        ShaderUtil.drawQuads(f, f2, f3, f4);
        gradientShader.unload();
        GlStateManager.func_179084_k();
    }

    public static void applyGradientHorizontal(float f, float f2, float f3, float f4, float f5, Color color, Color color2, Runnable runnable) {
        GradientUtil.applyGradient(f, f2, f3, f4, f5, color, color, color2, color2, runnable);
    }

    public static void drawGradientLR(float f, float f2, float f3, float f4, float f5, Color color, Color color2) {
        GradientUtil.drawGradient(f, f2, f3, f4, f5, color, color, color2, color2);
    }

    static {
        gradientMaskShader = new ShaderUtil("More/Shaderss/gradientmask.frag");
        gradientShader = new ShaderUtil("More/Shaderss/gradient.frag");
    }

    public static void drawGradientTB(float f, float f2, float f3, float f4, float f5, Color color, Color color2) {
        GradientUtil.drawGradient(f, f2, f3, f4, f5, color2, color, color2, color);
    }

    public static void applyGradientVertical(float f, float f2, float f3, float f4, float f5, Color color, Color color2, Runnable runnable) {
        GradientUtil.applyGradient(f, f2, f3, f4, f5, color2, color, color2, color, runnable);
    }

    public static void applyGradient(float f, float f2, float f3, float f4, float f5, Color color, Color color2, Color color3, Color color4, Runnable runnable) {
        DrRenderUtils.resetColor();
        GlStateManager.func_179147_l();
        GlStateManager.func_179112_b((int)770, (int)771);
        gradientMaskShader.init();
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
        gradientMaskShader.setUniformf("location", f * (float)scaledResolution.func_78325_e(), (float)Minecraft.func_71410_x().field_71440_d - f4 * (float)scaledResolution.func_78325_e() - f2 * (float)scaledResolution.func_78325_e());
        gradientMaskShader.setUniformf("rectSize", f3 * (float)scaledResolution.func_78325_e(), f4 * (float)scaledResolution.func_78325_e());
        gradientMaskShader.setUniformf("alpha", f5);
        gradientMaskShader.setUniformi("tex", 0);
        gradientMaskShader.setUniformf("color1", (float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f);
        gradientMaskShader.setUniformf("color2", (float)color2.getRed() / 255.0f, (float)color2.getGreen() / 255.0f, (float)color2.getBlue() / 255.0f);
        gradientMaskShader.setUniformf("color3", (float)color3.getRed() / 255.0f, (float)color3.getGreen() / 255.0f, (float)color3.getBlue() / 255.0f);
        gradientMaskShader.setUniformf("color4", (float)color4.getRed() / 255.0f, (float)color4.getGreen() / 255.0f, (float)color4.getBlue() / 255.0f);
        runnable.run();
        gradientMaskShader.unload();
        GlStateManager.func_179084_k();
    }

    public static void applyGradientCornerRL(float f, float f2, float f3, float f4, float f5, Color color, Color color2, Runnable runnable) {
        Color color3 = DrRenderUtils.interpolateColorC(color2, color, 0.5f);
        GradientUtil.applyGradient(f, f2, f3, f4, f5, color, color3, color3, color2, runnable);
    }
}

