/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderHelper
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import java.awt.Color;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.features.module.modules.render.CustomColor;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.RoundedUtil;
import net.ccbluex.liquidbounce.utils.render.tenacity.ColorUtil;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;

@ElementInfo(name="Inventory")
public final class Inventory
extends Element {
    private final ListValue rectValue;

    private final void renderInv(int n, int n2, int n3, int n4) {
        int n5 = n3;
        int n6 = n;
        int n7 = n2;
        if (n6 <= n7) {
            while (true) {
                n5 += 18;
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP.getInventoryContainer().getSlot(n6).getStack() == null) {
                } else {
                    IItemStack iItemStack;
                    MinecraftInstance.mc.getRenderItem().renderItemAndEffectIntoGUI(iItemStack, n5 - 18, n4);
                    MinecraftInstance.mc.getRenderItem().renderItemOverlays(Fonts.posterama30, iItemStack, n5 - 18, n4);
                }
                if (n6 == n7) break;
                ++n6;
            }
        }
    }

    public Inventory(double d, double d2, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 1) != 0) {
            d = 300.0;
        }
        if ((n & 2) != 0) {
            d2 = 50.0;
        }
        this(d, d2);
    }

    public Inventory() {
        this(0.0, 0.0, 3, (DefaultConstructorMarker)null);
    }

    public Inventory(double d, double d2) {
        super(d, d2, 0.0f, null, 12, null);
        this.rectValue = new ListValue("RectMode", new String[]{"SB", "None"}, "SB");
    }

    @Override
    public Border drawElement() {
        Color color = new Color(((Number)CustomColor.r.get()).intValue(), ((Number)CustomColor.g.get()).intValue(), ((Number)CustomColor.b.get()).intValue(), ((Number)CustomColor.a.get()).intValue());
        Color color2 = new Color(((Number)CustomColor.r.get()).intValue(), ((Number)CustomColor.g.get()).intValue(), ((Number)CustomColor.b.get()).intValue(), ((Number)CustomColor.a.get()).intValue());
        Color color3 = new Color(((Number)CustomColor.r2.get()).intValue(), ((Number)CustomColor.g2.get()).intValue(), ((Number)CustomColor.b2.get()).intValue(), ((Number)CustomColor.a2.get()).intValue());
        Color color4 = new Color(((Number)CustomColor.r2.get()).intValue(), ((Number)CustomColor.g2.get()).intValue(), ((Number)CustomColor.b2.get()).intValue(), ((Number)CustomColor.a2.get()).intValue());
        double d = -12.0;
        RoundedUtil.drawGradientRound(0.0f, (float)d, 174.0f, 76.0f, ((Number)CustomColor.ra.get()).floatValue(), ColorUtil.applyOpacity(color4, 0.85f), color, color3, color2);
        Fonts.font40.drawString("Inventory", 5.0f, (float)(d + (double)(((String)this.rectValue.get()).equals("Onetap") || ((String)this.rectValue.get()).equals("Rainbow") ? 8.0f : 4.0f)), -1);
        RenderHelper.func_74520_c();
        this.renderInv(9, 17, 6, 6);
        this.renderInv(18, 26, 6, 24);
        this.renderInv(27, 35, 6, 42);
        RenderHelper.func_74518_a();
        GlStateManager.func_179141_d();
        GlStateManager.func_179084_k();
        GlStateManager.func_179140_f();
        return new Border(0.0f, (float)d, 174.0f, 66.0f);
    }
}

