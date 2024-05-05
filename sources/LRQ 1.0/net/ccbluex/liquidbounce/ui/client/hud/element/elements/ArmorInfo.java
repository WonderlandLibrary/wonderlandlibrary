/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import java.awt.Color;
import jx.utils.BlurUtils;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.render.entity.IRenderItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import org.lwjgl.opengl.GL11;

@ElementInfo(name="ArmorInfo")
public final class ArmorInfo
extends Element {
    private final FloatValue radiusValue;
    private final IntegerValue bgalphaValue;
    private final BoolValue blurValue;
    private final FloatValue blurStrength;

    @Override
    public Border drawElement() {
        int index;
        float width = 70.0f;
        float height = 0.0f;
        IRenderItem renderItem = MinecraftInstance.mc.getRenderItem();
        int y = 1;
        int n = 0;
        int n2 = 3;
        while (n <= n2) {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP.getInventory().getArmorInventory().get(index) != null) {
                height += (float)25;
            }
            ++index;
        }
        if (((Boolean)this.blurValue.get()).booleanValue()) {
            GL11.glTranslated((double)(-this.getRenderX()), (double)(-this.getRenderY()), (double)0.0);
            GL11.glPushMatrix();
            BlurUtils.blurArea((float)this.getRenderX(), (float)this.getRenderY(), width, 100.0f, ((Number)this.blurStrength.get()).floatValue());
            GL11.glPopMatrix();
            GL11.glTranslated((double)this.getRenderX(), (double)this.getRenderY(), (double)0.0);
        }
        RenderUtils.originalRoundedRect(0.0f, 0.0f, width, 0.0f + height, ((Number)this.radiusValue.get()).floatValue(), new Color(0, 0, 0, ((Number)this.bgalphaValue.get()).intValue()).getRGB());
        n2 = 0;
        for (index = 3; index >= 0; --index) {
            IItemStack stack;
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP.getInventory().getArmorInventory().get(index) == null) {
                continue;
            }
            double armorValue = (float)(stack.getMaxDamage() - stack.getItemDamage()) / (float)stack.getMaxDamage() * 100.0f;
            float armorValue2 = (float)(stack.getMaxDamage() - stack.getItemDamage()) / (float)stack.getMaxDamage();
            Fonts.bold28.drawString(String.valueOf(Math.round(armorValue)) + "%", 29, y + 8, Color.WHITE.getRGB());
            renderItem.renderItemIntoGUI(stack, 5, y + 5);
            RenderUtils.drawRect((int)22.5, y + 10, (int)23.5, y + 20, new Color(132, 125, 125, 200).getRGB());
            RenderUtils.originalRoundedRect(30.0f, Fonts.bold28.getFontHeight() + y + 5, 30.0f + armorValue2 * 30.0f, (float)((double)(Fonts.bold28.getFontHeight() + y + 5) + 3.5), 1.0f, (double)armorValue2 > 0.5 ? new Color(10, 111, 82).getRGB() : new Color(222, 191, 15).getRGB());
            y += 25;
        }
        return new Border(0.0f, 0.0f, width, 100.0f);
    }

    public ArmorInfo(double x, double y, float scale) {
        super(0.0, 0.0, 0.0f, null, 15, null);
        this.radiusValue = new FloatValue("Radius", 4.25f, 0.0f, 10.0f);
        this.bgalphaValue = new IntegerValue("Bg-Alpha", 150, 60, 255);
        this.blurValue = new BoolValue("Blur-Value", true);
        this.blurStrength = new FloatValue("BlurStrength-Value", 10.0f, 0.0f, 40.0f);
    }

    public /* synthetic */ ArmorInfo(double d, double d2, float f, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 1) != 0) {
            d = -8.0;
        }
        if ((n & 2) != 0) {
            d2 = 57.0;
        }
        if ((n & 4) != 0) {
            f = 1.0f;
        }
        this(d, d2, f);
    }

    public ArmorInfo() {
        this(0.0, 0.0, 0.0f, 7, null);
    }
}

