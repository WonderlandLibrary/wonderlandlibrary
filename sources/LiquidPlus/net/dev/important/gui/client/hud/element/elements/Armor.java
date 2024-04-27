/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.material.Material
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.entity.RenderItem
 *  net.minecraft.item.ItemStack
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 */
package net.dev.important.gui.client.hud.element.elements;

import java.awt.Color;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.gui.client.hud.element.Border;
import net.dev.important.gui.client.hud.element.Element;
import net.dev.important.gui.client.hud.element.ElementInfo;
import net.dev.important.gui.client.hud.element.Side;
import net.dev.important.gui.font.Fonts;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.render.RenderUtils;
import net.dev.important.value.FloatValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

@ElementInfo(name="Armor")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B-\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tJ\n\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0016R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2={"Lnet/dev/important/gui/client/hud/element/elements/Armor;", "Lnet/dev/important/gui/client/hud/element/Element;", "x", "", "y", "scale", "", "side", "Lnet/dev/important/gui/client/hud/element/Side;", "(DDFLnet/dev/important/gui/client/hud/element/Side;)V", "blueValue", "Lnet/dev/important/value/IntegerValue;", "brightnessValue", "Lnet/dev/important/value/FloatValue;", "colorModeValue", "Lnet/dev/important/value/ListValue;", "greenValue", "newRainbowIndex", "redValue", "saturationValue", "speed", "drawElement", "Lnet/dev/important/gui/client/hud/element/Border;", "LiquidBounce"})
public final class Armor
extends Element {
    @NotNull
    private final ListValue colorModeValue;
    @NotNull
    private final FloatValue brightnessValue;
    @NotNull
    private final IntegerValue redValue;
    @NotNull
    private final IntegerValue greenValue;
    @NotNull
    private final IntegerValue blueValue;
    @NotNull
    private final IntegerValue newRainbowIndex;
    @NotNull
    private final FloatValue saturationValue;
    @NotNull
    private final IntegerValue speed;

    public Armor(double x, double y, float scale, @NotNull Side side) {
        Intrinsics.checkNotNullParameter(side, "side");
        super(x, y, scale, side);
        String[] stringArray = new String[]{"Custom", "Astolfo"};
        this.colorModeValue = new ListValue("Text-Color", stringArray, "Custom");
        this.brightnessValue = new FloatValue("Brightness", 1.0f, 0.0f, 1.0f);
        this.redValue = new IntegerValue("Text-R", 255, 0, 255);
        this.greenValue = new IntegerValue("Text-G", 255, 0, 255);
        this.blueValue = new IntegerValue("Text-B", 255, 0, 255);
        this.newRainbowIndex = new IntegerValue("NewRainbowOffset", 1, 1, 50);
        this.saturationValue = new FloatValue("Saturation", 0.9f, 0.0f, 1.0f);
        this.speed = new IntegerValue("AllSpeed", 0, 0, 400);
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

    @Override
    @Nullable
    public Border drawElement() {
        int x2 = 0;
        if (MinecraftInstance.mc.field_71442_b.func_78762_g()) {
            int index;
            GL11.glPushMatrix();
            RenderItem renderItem = MinecraftInstance.mc.func_175599_af();
            boolean isInsideWater = MinecraftInstance.mc.field_71439_g.func_70055_a(Material.field_151586_h);
            int x = 1;
            int i = 0;
            int y = isInsideWater ? -10 : 0;
            String colorMode = (String)this.colorModeValue.get();
            int color = new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue()).getRGB();
            boolean rainbow = StringsKt.equals(colorMode, "Rainbow", true);
            int n = 0;
            while (n < 4) {
                if (MinecraftInstance.mc.field_71439_g.field_71071_by.field_70460_b[index = n++] == null) continue;
                x2 += 20;
            }
            RenderUtils.drawRect(-2.0f, -4.0f, 2.0f + (float)x2, 29.0f, new Color(50, 50, 50, 60));
            n = 3;
            do {
                ItemStack stack;
                int colorall;
                index = n--;
                int n2 = rainbow ? 0 : (colorall = StringsKt.equals(colorMode, "Astolfo", true) ? RenderUtils.Astolfo(index * ((Number)this.speed.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue()) : color);
                if (MinecraftInstance.mc.field_71439_g.field_71071_by.field_70460_b[index] == null) continue;
                RenderUtils.drawGradientSidewaysV(x, 0.0, (double)x + (double)18, 17.0, colorall, new Color(140, 140, 140, 40).getRGB());
                Fonts.fontSmall.func_175063_a(String.valueOf(stack.func_77958_k() - stack.func_77952_i()), (float)x + 4.0f, 20.0f, colorall);
                RenderUtils.drawRect((float)x, 25.0f, (float)x + 18.0f, 26.0f, new Color(140, 140, 140, 220).getRGB());
                RenderUtils.drawRect((float)x, 25.0f, (float)x + 18.0f * (float)(stack.func_77958_k() - stack.func_77952_i()) / (float)stack.func_77958_k(), 26.0f, colorall);
                renderItem.func_175042_a(stack, x + 1, y);
                x += 20;
                ++i;
            } while (0 <= n);
            GlStateManager.func_179141_d();
            GlStateManager.func_179084_k();
            GlStateManager.func_179140_f();
            GlStateManager.func_179129_p();
            GL11.glPopMatrix();
        }
        return new Border(-2.0f, -4.0f, 82.0f, 29.0f);
    }

    public Armor() {
        this(0.0, 0.0, 0.0f, null, 15, null);
    }
}

