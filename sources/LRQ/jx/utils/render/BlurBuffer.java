/*
 * Decompiled with CFR 0.152.
 */
package jx.utils.render;

import java.awt.Color;
import jx.utils.blur.GaussianBlur;
import jx.utils.blur.StencilUtil;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;

public class BlurBuffer {
    public static void blurArea(float x, float y, float width, float height) {
        HUD hud = (HUD)LiquidBounce.moduleManager.getModule(HUD.class);
        StencilUtil.initStencilToWrite();
        RenderUtils.drawRect(x, y, x + width, y + height, new Color(-2).getRGB());
        StencilUtil.readStencilBuffer(1);
        GaussianBlur.renderBlur(((Integer)hud.getRadius().getValue()).floatValue());
        StencilUtil.uninitStencilBuffer();
    }

    public static void blurArea2(float x, float y, float x2, float y2) {
        HUD hud = (HUD)LiquidBounce.moduleManager.getModule(HUD.class);
        StencilUtil.initStencilToWrite();
        RenderUtils.drawRect(x, y, x + (x2 - x), y + (y2 - y), new Color(-2).getRGB());
        StencilUtil.readStencilBuffer(1);
        GaussianBlur.renderBlur(((Integer)hud.getRadius().getValue()).floatValue());
        StencilUtil.uninitStencilBuffer();
    }

    public static void blurAreacustomradius(float x, float y, float width, float height, float radius) {
        HUD hud = (HUD)LiquidBounce.moduleManager.getModule(HUD.class);
        StencilUtil.initStencilToWrite();
        RenderUtils.drawRect(x, y, x + width, y + height, new Color(-2).getRGB());
        StencilUtil.readStencilBuffer(1);
        GaussianBlur.renderBlur(radius);
        StencilUtil.uninitStencilBuffer();
    }

    public static void blurRoundArea(float x, float y, float width, float height, int radius) {
        StencilUtil.initStencilToWrite();
        RenderUtils.drawRoundedRect2(x, y, x + width, y + height, radius, 6, new Color(-2).getRGB());
        StencilUtil.readStencilBuffer(1);
        GaussianBlur.renderBlur(8.0f);
        StencilUtil.uninitStencilBuffer();
    }
}

