/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
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
import net.dev.important.gui.client.hud.element.Border;
import net.dev.important.gui.client.hud.element.Element;
import net.dev.important.gui.client.hud.element.ElementInfo;
import net.dev.important.gui.font.Fonts;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.render.ColorUtils;
import net.dev.important.utils.render.GLUtils;
import net.dev.important.utils.render.RenderUtils;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

@ElementInfo(name="Inventory")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B#\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\n\u0010\u0016\u001a\u0004\u0018\u00010\u0017H\u0016R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2={"Lnet/dev/important/gui/client/hud/element/elements/Inventory;", "Lnet/dev/important/gui/client/hud/element/Element;", "x", "", "y", "scale", "", "(DDF)V", "brightnessValue", "Lnet/dev/important/value/FloatValue;", "cRainbowSecValue", "Lnet/dev/important/value/IntegerValue;", "colorBlueValue", "colorGreenValue", "colorRedValue", "distanceValue", "gradientAmountValue", "lineValue", "Lnet/dev/important/value/BoolValue;", "rainbowList", "Lnet/dev/important/value/ListValue;", "saturationValue", "drawElement", "Lnet/dev/important/gui/client/hud/element/Border;", "LiquidBounce"})
public final class Inventory
extends Element {
    @NotNull
    private final BoolValue lineValue;
    @NotNull
    private final ListValue rainbowList;
    @NotNull
    private final FloatValue saturationValue;
    @NotNull
    private final FloatValue brightnessValue;
    @NotNull
    private final IntegerValue cRainbowSecValue;
    @NotNull
    private final IntegerValue distanceValue;
    @NotNull
    private final IntegerValue gradientAmountValue;
    @NotNull
    private final IntegerValue colorRedValue;
    @NotNull
    private final IntegerValue colorGreenValue;
    @NotNull
    private final IntegerValue colorBlueValue;

    public Inventory(double x, double y, float scale) {
        super(x, y, scale, null, 8, null);
        this.lineValue = new BoolValue("Line", true);
        String[] stringArray = new String[]{"Off", "CRainbow", "Sky", "LiquidSlowly", "Fade"};
        this.rainbowList = new ListValue("Rainbow", stringArray, "LiquidSlowly");
        this.saturationValue = new FloatValue("Saturation", 0.3f, 0.0f, 1.0f);
        this.brightnessValue = new FloatValue("Brightness", 1.0f, 0.0f, 1.0f);
        this.cRainbowSecValue = new IntegerValue("Seconds", 2, 1, 10);
        this.distanceValue = new IntegerValue("Line-Distance", 14, 0, 400);
        this.gradientAmountValue = new IntegerValue("Gradient-Amount", 35, 1, 50);
        this.colorRedValue = new IntegerValue("Red", 0, 0, 255);
        this.colorGreenValue = new IntegerValue("Green", 160, 0, 255);
        this.colorBlueValue = new IntegerValue("Blue", 255, 0, 255);
    }

    public /* synthetic */ Inventory(double d, double d2, float f, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 1) != 0) {
            d = 10.0;
        }
        if ((n & 2) != 0) {
            d2 = 10.0;
        }
        if ((n & 4) != 0) {
            f = 1.0f;
        }
        this(d, d2, f);
    }

    @Override
    @Nullable
    public Border drawElement() {
        block38: {
            int i;
            int n;
            int n2;
            RenderUtils.drawRect(8.0f, 25.0f, 173.0f, 95.0f, new Color(0, 0, 0, 120).getRGB());
            Fonts.fontSFUI40.func_78276_b("Inventory", 11, 29, new Color(0xFFFFFF).getRGB());
            if (!((Boolean)this.lineValue.get()).booleanValue() || (n2 = 0) > (n = ((Number)this.gradientAmountValue.get()).intValue() - 1)) break block38;
            do {
                int n3;
                int n4;
                String rainbowType;
                i = n2++;
                int color = new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue()).getRGB();
                switch (rainbowType = (String)this.rainbowList.get()) {
                    case "CRainbow": {
                        n4 = RenderUtils.getRainbowOpaque(((Number)this.cRainbowSecValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), i * ((Number)this.distanceValue.get()).intValue());
                        break;
                    }
                    case "Sky": {
                        n4 = RenderUtils.SkyRainbow(i * ((Number)this.distanceValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                        break;
                    }
                    case "LiquidSlowly": {
                        Color color2 = ColorUtils.LiquidSlowly(System.nanoTime(), i * ((Number)this.distanceValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                        Intrinsics.checkNotNull(color2);
                        n4 = color2.getRGB();
                        break;
                    }
                    case "Fade": {
                        n4 = ColorUtils.fade(new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue()), i * ((Number)this.distanceValue.get()).intValue(), 100).getRGB();
                        break;
                    }
                    default: {
                        n4 = color;
                    }
                }
                switch (rainbowType) {
                    case "CRainbow": {
                        n3 = RenderUtils.getRainbowOpaque(((Number)this.cRainbowSecValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), (i + 1) * ((Number)this.distanceValue.get()).intValue());
                        break;
                    }
                    case "Sky": {
                        n3 = RenderUtils.SkyRainbow((i + 1) * ((Number)this.distanceValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                        break;
                    }
                    case "LiquidSlowly": {
                        Color color3 = ColorUtils.LiquidSlowly(System.nanoTime(), (i + 1) * ((Number)this.distanceValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                        Intrinsics.checkNotNull(color3);
                        n3 = color3.getRGB();
                        break;
                    }
                    case "Fade": {
                        n3 = ColorUtils.fade(new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue()), (i + 1) * ((Number)this.distanceValue.get()).intValue(), 100).getRGB();
                        break;
                    }
                    default: {
                        n3 = color;
                    }
                }
                RenderUtils.drawGradientSideways(8.0, 25.0, 173.0, 26.0, n4, n3);
            } while (i != n);
        }
        int itemX = 10;
        int itemY = 40;
        int airs = 0;
        int n = 0;
        int n5 = MinecraftInstance.mc.field_71439_g.field_71071_by.field_70462_a.length;
        while (n < n5) {
            int i;
            if ((i = n++) < 9) continue;
            ItemStack stack = MinecraftInstance.mc.field_71439_g.field_71071_by.field_70462_a[i];
            if (stack == null) {
                int n6 = airs;
                airs = n6 + 1;
            }
            ScaledResolution res = new ScaledResolution(MinecraftInstance.mc);
            GL11.glPushMatrix();
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            if (MinecraftInstance.mc.field_71441_e != null) {
                GLUtils.enableGUIStandardItemLighting();
            }
            GlStateManager.func_179094_E();
            GlStateManager.func_179118_c();
            GlStateManager.func_179086_m((int)256);
            MinecraftInstance.mc.func_175599_af().field_77023_b = -150.0f;
            MinecraftInstance.mc.func_175599_af().func_180450_b(stack, itemX, itemY);
            MinecraftInstance.mc.func_175599_af().func_175030_a(MinecraftInstance.mc.field_71466_p, stack, itemX, itemY);
            MinecraftInstance.mc.func_175599_af().field_77023_b = 0.0f;
            GlStateManager.func_179084_k();
            GlStateManager.func_179139_a((double)0.5, (double)0.5, (double)0.5);
            GlStateManager.func_179097_i();
            GlStateManager.func_179140_f();
            GlStateManager.func_179126_j();
            GlStateManager.func_179152_a((float)2.0f, (float)2.0f, (float)2.0f);
            GlStateManager.func_179141_d();
            GlStateManager.func_179121_F();
            GL11.glPopMatrix();
            if (itemX < 152) {
                itemX += 18;
                continue;
            }
            itemX = 10;
            itemY += 18;
        }
        if (airs == 27) {
            Fonts.font40.func_78276_b("Your inventory is empty...", 28, 56, new Color(255, 255, 255).getRGB());
        }
        return new Border(8.0f, 40.0f, 171.0f, 95.0f);
    }

    public Inventory() {
        this(0.0, 0.0, 0.0f, 7, null);
    }
}

