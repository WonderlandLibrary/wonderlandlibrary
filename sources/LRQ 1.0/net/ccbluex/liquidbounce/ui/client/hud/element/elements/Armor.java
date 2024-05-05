/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.renderer.GlStateManager
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import java.awt.Color;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.enums.MaterialType;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.render.entity.IRenderItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.Side;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

@ElementInfo(name="Armor")
public final class Armor
extends Element {
    private final BoolValue itemDamageValue;
    private final BoolValue bgValue;
    private final IntegerValue alphaValue;
    private final BoolValue cfontValue;
    private final ListValue modeValue;

    public final void shader() {
        if (StringsKt.equals((String)((String)this.modeValue.get()), (String)"Horizontal", (boolean)true)) {
            RenderUtils.drawRect(0.0f, 0.0f, 72.0f, 21.0f, -1);
        } else {
            RenderUtils.drawRect(0.0f, 0.0f, 18.0f, 76.0f, -1);
        }
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public Border drawElement() {
        GL11.glPushMatrix();
        IRenderItem renderItem = MinecraftInstance.mc.getRenderItem();
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        boolean isInsideWater = iEntityPlayerSP.isInsideOfMaterial(MinecraftInstance.classProvider.getMaterialEnum(MaterialType.WATER));
        int x = 1;
        int y = isInsideWater ? -10 : 0;
        String mode = (String)this.modeValue.get();
        if (((Boolean)this.bgValue.get()).booleanValue()) {
            if (StringsKt.equals((String)((String)this.modeValue.get()), (String)"Horizontal", (boolean)true)) {
                RenderUtils.drawRect(0.0f, 0.0f, 72.0f, 21.0f, new Color(0, 0, 0, ((Number)this.alphaValue.get()).intValue()).getRGB());
            } else {
                RenderUtils.drawRect(0.0f, 0.0f, 18.0f, 76.0f, new Color(0, 0, 0, ((Number)this.alphaValue.get()).intValue()).getRGB());
            }
        }
        Gui.func_73734_a((int)0, (int)0, (int)0, (int)0, (int)-1);
        int n = 3;
        boolean bl = false;
        while (n >= 0) {
            void index;
            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP2.getInventory().getArmorInventory().get((int)index) == null) {
            } else {
                IItemStack stack;
                renderItem.renderItemAndEffectIntoGUI(stack, x, y);
                int itemDamage = stack.getMaxDamage() - stack.getItemDamage();
                GlStateManager.func_179094_E();
                GlStateManager.func_179152_a((float)0.5f, (float)0.5f, (float)0.5f);
                Gui.func_73734_a((int)0, (int)0, (int)0, (int)0, (int)-1);
                if (itemDamage > 0 && ((Boolean)this.itemDamageValue.get()).booleanValue() && !((Boolean)this.cfontValue.get()).booleanValue()) {
                    MinecraftInstance.mc.getFontRendererObj().drawCenteredString(String.valueOf(itemDamage), (float)(x + 8) / 0.5f, (float)(y + 16) / 0.5f, -1, true);
                }
                GlStateManager.func_179121_F();
                if (((Boolean)this.cfontValue.get()).booleanValue()) {
                    Fonts.bold35.drawCenteredString(String.valueOf(itemDamage), x + 8, y + 16, -1, true);
                }
                if (StringsKt.equals((String)mode, (String)"Horizontal", (boolean)true)) {
                    x += 18;
                } else if (StringsKt.equals((String)mode, (String)"Vertical", (boolean)true)) {
                    y += 18;
                }
            }
            --index;
        }
        MinecraftInstance.classProvider.getGlStateManager().enableAlpha();
        MinecraftInstance.classProvider.getGlStateManager().disableBlend();
        MinecraftInstance.classProvider.getGlStateManager().disableLighting();
        MinecraftInstance.classProvider.getGlStateManager().disableCull();
        GL11.glPopMatrix();
        return StringsKt.equals((String)((String)this.modeValue.get()), (String)"Horizontal", (boolean)true) ? new Border(0.0f, 0.0f, 72.0f, 21.0f) : new Border(0.0f, 0.0f, 18.0f, 76.0f);
    }

    public Armor(double x, double y, float scale, Side side) {
        super(x, y, scale, side);
        this.itemDamageValue = new BoolValue("Damage", true);
        this.bgValue = new BoolValue("BG", true);
        this.alphaValue = new IntegerValue("Alpha", 120, 0, 255);
        this.cfontValue = new BoolValue("CFont", true);
        this.modeValue = new ListValue("Alignment", new String[]{"Horizontal", "Vertical"}, "Horizontal");
    }

    public /* synthetic */ Armor(double d, double d2, float f, Side side, int n, DefaultConstructorMarker defaultConstructorMarker) {
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

    public Armor() {
        this(0.0, 0.0, 0.0f, null, 15, null);
    }
}

