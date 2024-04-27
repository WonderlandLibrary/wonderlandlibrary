/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.JvmStatic
 *  net.minecraft.client.renderer.GlStateManager
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.utils.render;

import java.awt.Color;
import kotlin.TypeCastException;
import kotlin.jvm.JvmStatic;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.modules.render.CustomColor;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public final class RenderUtilsKt {
    public static final RenderUtilsKt INSTANCE;

    private RenderUtilsKt() {
    }

    static {
        RenderUtilsKt renderUtilsKt;
        INSTANCE = renderUtilsKt = new RenderUtilsKt();
    }

    @JvmStatic
    public static final void drawCircle(float f, float f2, float f3, float f4, float f5) {
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)2.0f);
        GL11.glBegin((int)3);
        Module module = LiquidBounce.INSTANCE.getModuleManager().get(HUD.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.render.HUD");
        }
        HUD hUD = (HUD)module;
        for (float f6 = f5; f6 >= f4; f6 -= 4.0f) {
            double d = (double)System.currentTimeMillis() / 360.0 + (double)(f6 * (float)34 / (float)360 * (float)56 / (float)100);
            Color color = new Color(((Number)CustomColor.r2.get()).intValue(), ((Number)CustomColor.g2.get()).intValue(), ((Number)CustomColor.b2.get()).intValue(), 1);
            Color color2 = new Color(((Number)CustomColor.r.get()).intValue(), ((Number)CustomColor.g.get()).intValue(), ((Number)CustomColor.b.get()).intValue());
            boolean bl = false;
            double d2 = Math.abs(d);
            int n = RenderUtils.getGradientOffset(color2, color, d2 / (double)10).getRGB();
            float f7 = (float)(n >> 24 & 0xFF) / 255.0f;
            float f8 = (float)(n >> 16 & 0xFF) / 255.0f;
            float f9 = (float)(n >> 8 & 0xFF) / 255.0f;
            float f10 = (float)(n & 0xFF) / 255.0f;
            GlStateManager.func_179131_c((float)f8, (float)f9, (float)f10, (float)f7);
            double d3 = (double)f6 * Math.PI / (double)180;
            double d4 = f;
            boolean bl2 = false;
            d2 = Math.cos(d3);
            d3 = (double)f6 * Math.PI / (double)180;
            double d5 = f2;
            float f11 = (float)(d4 + d2 * (double)(f3 * 1.001f));
            bl2 = false;
            double d6 = Math.sin(d3);
            GL11.glVertex2f((float)f11, (float)((float)(d5 + d6 * (double)(f3 * 1.001f))));
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }
}

