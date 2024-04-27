/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.render.blur;

import java.awt.Color;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.blur.GaussianBlur;
import net.ccbluex.liquidbounce.utils.render.blur.StencilUtil;

public class BlurUtils {
    public static void CustomBlurRoundArea(float f, float f2, float f3, float f4, float f5, float f6) {
        HUD hUD = (HUD)LiquidBounce.moduleManager.getModule(HUD.class);
        StencilUtil.initStencilToWrite();
        RenderUtils.drawRoundedRect2(f, f2, f + f3, f2 + f4, f5, 6, new Color(-2).getRGB());
        StencilUtil.readStencilBuffer(1);
        GaussianBlur.renderBlur(f6);
        StencilUtil.uninitStencilBuffer();
    }

    public static void blurArea(float f, float f2, float f3, float f4) {
        HUD hUD = (HUD)LiquidBounce.moduleManager.getModule(HUD.class);
        StencilUtil.initStencilToWrite();
        RenderUtils.drawRect(f, f2, f + f3, f2 + f4, new Color(-2).getRGB());
        StencilUtil.readStencilBuffer(1);
        GaussianBlur.renderBlur(((Integer)hUD.getBlurStrength().get()).floatValue());
        StencilUtil.uninitStencilBuffer();
    }

    public static void shapeBlur(Runnable runnable) {
        HUD hUD = (HUD)LiquidBounce.moduleManager.getModule(HUD.class);
        StencilUtil.initStencilToWrite();
        runnable.run();
        StencilUtil.readStencilBuffer(1);
        GaussianBlur.renderBlur(((Integer)hUD.getBlurStrength().get()).intValue());
        StencilUtil.uninitStencilBuffer();
    }

    public static void blurRoundArea(float f, float f2, float f3, float f4, float f5) {
        HUD hUD = (HUD)LiquidBounce.moduleManager.getModule(HUD.class);
        StencilUtil.initStencilToWrite();
        RenderUtils.drawRoundedRect2(f, f2, f + f3, f2 + f4, f5, 6, new Color(-2).getRGB());
        StencilUtil.readStencilBuffer(1);
        GaussianBlur.renderBlur(((Integer)hUD.getBlurStrength().get()).floatValue());
        StencilUtil.uninitStencilBuffer();
    }

    public static void CustomBlurArea(float f, float f2, float f3, float f4, float f5) {
        StencilUtil.initStencilToWrite();
        RenderUtils.drawRect(f, f2, f + f3, f2 + f4, new Color(-2).getRGB());
        StencilUtil.readStencilBuffer(1);
        GaussianBlur.renderBlur(f5);
        StencilUtil.uninitStencilBuffer();
    }
}

