/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.JvmStatic
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.renderer.GlStateManager
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.utils.render;

import java.awt.Color;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0005\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J0\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u0006H\u0007\u00a8\u0006\u000b"}, d2={"Lnet/ccbluex/liquidbounce/utils/render/RenderUtilsKt;", "", "()V", "drawCircle", "", "x", "", "y", "radius", "start", "end", "LiKingSense"})
public final class RenderUtilsKt {
    public static final RenderUtilsKt INSTANCE;

    @JvmStatic
    public static final void drawCircle(float x, float y, float radius, float start, float end) {
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        int n = 0;
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)2.0f);
        GL11.glBegin((int)3);
        Module module = LiquidBounce.INSTANCE.getModuleManager().get(HUD.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.render.HUD");
        }
        HUD hud = (HUD)module;
        for (float i = end; i >= start; i -= 4.0f) {
            double d = (double)System.currentTimeMillis() / 360.0 + (double)(i * (float)34 / (float)360 * (float)56 / (float)100);
            Color color = new Color(((Number)hud.getR2().get()).intValue(), ((Number)hud.getG2().get()).intValue(), ((Number)hud.getB2().get()).intValue(), 1);
            Color color2 = new Color(((Number)hud.getR().get()).intValue(), ((Number)hud.getG().get()).intValue(), ((Number)hud.getB().get()).intValue());
            boolean bl = false;
            double d2 = Math.abs(d);
            Color color3 = RenderUtils.getGradientOffset(color2, color, d2 / (double)10);
            Intrinsics.checkExpressionValueIsNotNull((Object)color3, (String)"RenderUtils.getGradientO\u2026 100) / 10)\n            )");
            int c = color3.getRGB();
            float f2 = (float)(c >> 24 & 0xFF) / 255.0f;
            float f22 = (float)(c >> 16 & 0xFF) / 255.0f;
            float f3 = (float)(c >> 8 & 0xFF) / 255.0f;
            float f4 = (float)(c & 0xFF) / 255.0f;
            GlStateManager.func_179131_c((float)f22, (float)f3, (float)f4, (float)f2);
            double d3 = (double)i * Math.PI / (double)180;
            double d4 = x;
            boolean bl2 = false;
            d2 = Math.cos(d3);
            d3 = (double)i * Math.PI / (double)180;
            double d5 = y;
            float f = (float)(d4 + d2 * (double)(radius * 1.001f));
            bl2 = false;
            double d6 = Math.sin(d3);
            GL11.glVertex2f((float)f, (float)((float)(d5 + d6 * (double)(radius * 1.001f))));
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    private RenderUtilsKt() {
    }

    static {
        RenderUtilsKt renderUtilsKt;
        INSTANCE = renderUtilsKt = new RenderUtilsKt();
    }
}

