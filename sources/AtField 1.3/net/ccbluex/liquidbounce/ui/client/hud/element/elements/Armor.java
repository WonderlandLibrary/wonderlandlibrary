/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.item.ItemStack
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import java.awt.Color;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import liying.fonts.impl.Fonts;
import net.ccbluex.liquidbounce.api.enums.MaterialType;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.render.entity.IRenderItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.features.module.modules.render.CustomColor;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.Side;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.RoundedUtil;
import net.ccbluex.liquidbounce.utils.render.tenacity.ColorUtil;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

@ElementInfo(name="Armor")
public final class Armor
extends Element {
    private final IntegerValue b;
    private final IntegerValue a;
    private final IntegerValue g;
    private final ListValue modeValue;
    private final IntegerValue r;

    public Armor(double d, double d2, float f, Side side, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 1) != 0) {
            d = -8.0;
        }
        if ((n & 2) != 0) {
            d2 = 57.0;
        }
        if ((n & 4) != 0) {
            f = 1.0f;
        }
        if ((n & 8) != 0) {
            side = new Side(Side.Horizontal.MIDDLE, Side.Vertical.DOWN);
        }
        this(d, d2, f, side);
    }

    public Armor(double d, double d2, float f, Side side) {
        super(d, d2, f, side);
        this.modeValue = new ListValue("Alignment", new String[]{"Horizontal", "Vertical"}, "Horizontal");
        this.r = new IntegerValue("Red", 255, 0, 255);
        this.g = new IntegerValue("Green", 255, 0, 255);
        this.b = new IntegerValue("Blue", 255, 0, 255);
        this.a = new IntegerValue("Alpha", 255, 0, 255);
    }

    public final IntegerValue getR() {
        return this.r;
    }

    public final IntegerValue getG() {
        return this.g;
    }

    public final IntegerValue getB() {
        return this.b;
    }

    @Override
    public Border drawElement() {
        if (MinecraftInstance.mc.getPlayerController().isNotCreative()) {
            GL11.glPushMatrix();
            IRenderItem iRenderItem = MinecraftInstance.mc.getRenderItem();
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            boolean bl = iEntityPlayerSP.isInsideOfMaterial(MinecraftInstance.classProvider.getMaterialEnum(MaterialType.WATER));
            int n = new Color(((Number)this.r.get()).intValue(), ((Number)this.g.get()).intValue(), ((Number)this.b.get()).intValue(), ((Number)this.a.get()).intValue()).getRGB();
            int n2 = 1;
            int n3 = bl ? -10 : 0;
            Color color = new Color(((Number)CustomColor.r.get()).intValue(), ((Number)CustomColor.g.get()).intValue(), ((Number)CustomColor.b.get()).intValue(), ((Number)CustomColor.a.get()).intValue());
            Color color2 = new Color(((Number)CustomColor.r.get()).intValue(), ((Number)CustomColor.g.get()).intValue(), ((Number)CustomColor.b.get()).intValue(), ((Number)CustomColor.a.get()).intValue());
            Color color3 = new Color(((Number)CustomColor.r2.get()).intValue(), ((Number)CustomColor.g2.get()).intValue(), ((Number)CustomColor.b2.get()).intValue(), ((Number)CustomColor.a2.get()).intValue());
            Color color4 = new Color(((Number)CustomColor.r2.get()).intValue(), ((Number)CustomColor.g2.get()).intValue(), ((Number)CustomColor.b2.get()).intValue(), ((Number)CustomColor.a2.get()).intValue());
            String string = (String)this.modeValue.get();
            RoundedUtil.drawGradientRound((float)n2 - 2.0f, -12.0f, 75.0f, 40.0f, ((Number)CustomColor.ra.get()).floatValue(), ColorUtil.applyOpacity(color4, 0.85f), color, color3, color2);
            Fonts.SF.SF_18.SF_18.drawString((CharSequence)"Armor", (float)n2, -8.0f, n);
            boolean bl2 = false;
            for (int i = 3; i >= 0; --i) {
                IItemStack iItemStack;
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                if ((IItemStack)iEntityPlayerSP2.getInventory().getArmorInventory().get(i) == null) {
                    continue;
                }
                ItemStack itemStack = (ItemStack)MinecraftInstance.mc2.field_71439_g.field_71071_by.field_70460_b.get(i);
                iRenderItem.renderItemIntoGUI(iItemStack, n2, n3);
                iRenderItem.renderItemOverlays(MinecraftInstance.mc.getFontRendererObj(), iItemStack, n2, n3);
                GlStateManager.func_179094_E();
                Fonts.SF.SF_15.SF_15.drawString((CharSequence)String.valueOf(itemStack.func_77958_k() - iItemStack.getItemDamage()), (float)n2 + 2.0f, (float)n3 + 15.0f + (float)Fonts.SF.SF_15.SF_15.getHeight(), n);
                GlStateManager.func_179121_F();
                if (StringsKt.equals((String)string, (String)"Horizontal", (boolean)true)) {
                    n2 += 18;
                    continue;
                }
                if (!StringsKt.equals((String)string, (String)"Vertical", (boolean)true)) continue;
                n3 += 18;
            }
            MinecraftInstance.classProvider.getGlStateManager().enableAlpha();
            MinecraftInstance.classProvider.getGlStateManager().disableBlend();
            MinecraftInstance.classProvider.getGlStateManager().disableLighting();
            MinecraftInstance.classProvider.getGlStateManager().disableCull();
            GL11.glPopMatrix();
        }
        return StringsKt.equals((String)((String)this.modeValue.get()), (String)"Horizontal", (boolean)true) ? new Border(0.0f, 0.0f, 72.0f, 17.0f) : new Border(0.0f, 0.0f, 18.0f, 72.0f);
    }

    public Armor() {
        this(0.0, 0.0, 0.0f, null, 15, null);
    }

    public final IntegerValue getA() {
        return this.a;
    }
}

