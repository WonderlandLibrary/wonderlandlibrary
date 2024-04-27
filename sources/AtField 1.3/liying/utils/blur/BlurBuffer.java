/*
 * Decompiled with CFR 0.152.
 */
package liying.utils.blur;

import java.awt.Color;
import liying.utils.blur.GaussianBlur;
import liying.utils.blur.StencilUtil;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;

public class BlurBuffer {
    public static void blurArea(float f, float f2, float f3, float f4) {
        HUD hUD = (HUD)LiquidBounce.moduleManager.getModule(HUD.class);
        StencilUtil.initStencilToWrite();
        RenderUtils.drawRect(f, f2, f + f3, f2 + f4, new Color(-2).getRGB());
        StencilUtil.readStencilBuffer(1);
        GaussianBlur.renderBlur(((Integer)hUD.getRadius().getValue()).floatValue());
        StencilUtil.uninitStencilBuffer();
    }

    public static void blurArea2(float f, float f2, float f3, float f4) {
        HUD hUD = (HUD)LiquidBounce.moduleManager.getModule(HUD.class);
        StencilUtil.initStencilToWrite();
        RenderUtils.drawRect(f, f2, f + (f3 - f), f2 + (f4 - f2), new Color(-2).getRGB());
        StencilUtil.readStencilBuffer(1);
        GaussianBlur.renderBlur(((Integer)hUD.getRadius().getValue()).floatValue());
        StencilUtil.uninitStencilBuffer();
    }

    public static void blurRoundArea(float f, float f2, float f3, float f4, int n) {
        StencilUtil.initStencilToWrite();
        RenderUtils.drawRoundedRect2(f, f2, f + f3, f2 + f4, n, 6, new Color(-2).getRGB());
        StencilUtil.readStencilBuffer(1);
        GaussianBlur.renderBlur(8.0f);
        StencilUtil.uninitStencilBuffer();
    }

    public static void blurAreacustomradius(float f, float f2, float f3, float f4, float f5) {
        HUD hUD = (HUD)LiquidBounce.moduleManager.getModule(HUD.class);
        StencilUtil.initStencilToWrite();
        RenderUtils.drawRect(f, f2, f + f3, f2 + f4, new Color(-2).getRGB());
        StencilUtil.readStencilBuffer(1);
        GaussianBlur.renderBlur(f5);
        StencilUtil.uninitStencilBuffer();
    }
}

