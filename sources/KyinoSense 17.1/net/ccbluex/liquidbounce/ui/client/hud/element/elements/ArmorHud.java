/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.material.Material
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.PlayerControllerMP
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.entity.RenderItem
 *  net.minecraft.item.ItemStack
 *  org.jetbrains.annotations.NotNull
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import java.awt.Color;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import me.report.liquidware.modules.render.HudColors;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.Side;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.LnkRenderUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.ccbluex.liquidbounce.value.Value;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

@ElementInfo(name="ArmorHUD2")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B-\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tJ\b\u0010\u0012\u001a\u00020\u0013H\u0016R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/ArmorHud;", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Element;", "x", "", "y", "scale", "", "side", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Side;", "(DDFLnet/ccbluex/liquidbounce/ui/client/hud/element/Side;)V", "blueValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "colorModeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "greenValue", "rectColorBlueAlpha", "redValue", "speed", "drawElement", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Border;", "KyinoClient"})
public final class ArmorHud
extends Element {
    private final ListValue colorModeValue;
    private final IntegerValue redValue;
    private final IntegerValue greenValue;
    private final IntegerValue blueValue;
    private final IntegerValue rectColorBlueAlpha;
    private final IntegerValue speed;

    /*
     * WARNING - void declaration
     */
    @Override
    @NotNull
    public Border drawElement() {
        int x2 = 0;
        PlayerControllerMP playerControllerMP = ArmorHud.access$getMc$p$s1046033730().field_71442_b;
        Intrinsics.checkExpressionValueIsNotNull(playerControllerMP, "mc.playerController");
        if (playerControllerMP.func_78762_g()) {
            GL11.glPushMatrix();
            Minecraft minecraft = ArmorHud.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
            RenderItem renderItem = minecraft.func_175599_af();
            boolean isInsideWater = ArmorHud.access$getMc$p$s1046033730().field_71439_g.func_70055_a(Material.field_151586_h);
            int x = 1;
            int i = 0;
            int y = isInsideWater ? 0 : 0;
            String colorMode = (String)this.colorModeValue.get();
            int color = new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue()).getRGB();
            boolean rainbow = StringsKt.equals(colorMode, "Rainbow", true);
            int n = 0;
            int n2 = 3;
            while (n <= n2) {
                void index;
                if (ArmorHud.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70460_b[index] != null) {
                    x2 += 20;
                }
                ++index;
            }
            HudColors hud = (HudColors)LiquidBounce.INSTANCE.getModuleManager().getModule(HudColors.class);
            n2 = 3;
            boolean bl = false;
            while (n2 >= 0) {
                Integer colorall;
                void index;
                Integer n3;
                if (rainbow) {
                    n3 = 0;
                } else if (StringsKt.equals(colorMode, "Astolfo", true)) {
                    Object object = hud;
                    if (object != null && (object = ((HudColors)object).getSaturationValue()) != null) {
                        Object object2 = object;
                        boolean bl2 = false;
                        boolean bl3 = false;
                        Object it = object2;
                        boolean bl4 = false;
                        n3 = LnkRenderUtils.Astolfo((int)(index * ((Number)this.speed.get()).intValue()), ((Number)((Value)it).get()).floatValue(), ((Number)hud.getBrightnessValue().get()).floatValue());
                    } else {
                        n3 = null;
                    }
                } else {
                    n3 = colorall = StringsKt.equals(colorMode, "Bainbow", true) ? Integer.valueOf(ColorUtils.fade(new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue(), ((Number)this.rectColorBlueAlpha.get()).intValue()), (int)(index * ((Number)this.speed.get()).intValue()), 100).getRGB()) : Integer.valueOf(color);
                }
                if (ArmorHud.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70460_b[index] == null) {
                } else {
                    ItemStack stack;
                    if (colorall != null) {
                        RenderUtils.drawGradientSidewaysV(x, 0.0, (double)x + (double)18, 17.0, colorall, new Color(140, 140, 140, 40).getRGB());
                    }
                    if (colorall != null) {
                        Fonts.minecraftFont.func_175063_a(String.valueOf(stack.func_77958_k() - stack.func_77952_i()), (float)x + 1.0f, 20.0f, colorall.intValue());
                    }
                    renderItem.func_175042_a(stack, x + 1, y);
                    RenderUtils.drawExhiEnchants(stack, x, y);
                    x += 20;
                    ++i;
                }
                --index;
            }
            GlStateManager.func_179141_d();
            GlStateManager.func_179084_k();
            GlStateManager.func_179140_f();
            GlStateManager.func_179129_p();
            GL11.glPopMatrix();
        }
        return new Border(-2.0f, -4.0f, 82.0f, 29.0f);
    }

    public ArmorHud(double x, double y, float scale, @NotNull Side side) {
        Intrinsics.checkParameterIsNotNull(side, "side");
        super(x, y, scale, side);
        this.colorModeValue = new ListValue("Text Color", new String[]{"Custom", "Astolfo", "Bainbow"}, "Custom");
        this.redValue = new IntegerValue("Red", 255, 0, 255);
        this.greenValue = new IntegerValue("Green", 255, 0, 255);
        this.blueValue = new IntegerValue("Blue", 255, 0, 255);
        this.rectColorBlueAlpha = new IntegerValue("Color Blue Alpha", 255, 0, 255);
        this.speed = new IntegerValue("Speed", 0, 0, 400);
    }

    public /* synthetic */ ArmorHud(double d, double d2, float f, Side side, int n, DefaultConstructorMarker defaultConstructorMarker) {
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

    public ArmorHud() {
        this(0.0, 0.0, 0.0f, null, 15, null);
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

