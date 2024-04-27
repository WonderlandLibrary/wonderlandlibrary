/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.MathHelper
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.render;

import java.awt.Color;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.event.EventTarget;
import net.dev.important.event.Render2DEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.EntityUtils;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.render.RenderUtils;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import org.jetbrains.annotations.NotNull;

@Info(name="PointerESP", spacedName="Pointer ESP", description="Tracers but it's arrow.", category=Category.RENDER, cnName="\u73a9\u5bb6\u900f\u89c6")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2={"Lnet/dev/important/modules/module/modules/render/PointerESP;", "Lnet/dev/important/modules/module/Module;", "()V", "alphaValue", "Lnet/dev/important/value/IntegerValue;", "blueValue", "greenValue", "modeValue", "Lnet/dev/important/value/ListValue;", "noInViewValue", "Lnet/dev/important/value/BoolValue;", "radiusValue", "Lnet/dev/important/value/FloatValue;", "redValue", "sizeValue", "onRender2D", "", "event", "Lnet/dev/important/event/Render2DEvent;", "LiquidBounce"})
public final class PointerESP
extends Module {
    @NotNull
    private final ListValue modeValue;
    @NotNull
    private final IntegerValue redValue;
    @NotNull
    private final IntegerValue greenValue;
    @NotNull
    private final IntegerValue blueValue;
    @NotNull
    private final IntegerValue alphaValue;
    @NotNull
    private final IntegerValue sizeValue;
    @NotNull
    private final FloatValue radiusValue;
    @NotNull
    private final BoolValue noInViewValue;

    public PointerESP() {
        String[] stringArray = new String[]{"Solid", "Line"};
        this.modeValue = new ListValue("Mode", stringArray, "Solid");
        this.redValue = new IntegerValue("Red", 140, 0, 255);
        this.greenValue = new IntegerValue("Green", 140, 0, 255);
        this.blueValue = new IntegerValue("Blue", 255, 0, 255);
        this.alphaValue = new IntegerValue("Alpha", 255, 0, 255);
        this.sizeValue = new IntegerValue("Size", 100, 50, 200);
        this.radiusValue = new FloatValue("TriangleRadius", 2.2f, 1.0f, 10.0f, "m");
        this.noInViewValue = new BoolValue("NoEntityInView", true);
    }

    @EventTarget
    public final void onRender2D(@NotNull Render2DEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        ScaledResolution sr = new ScaledResolution(MinecraftInstance.mc);
        Color color = new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue(), ((Number)this.alphaValue.get()).intValue());
        GlStateManager.func_179094_E();
        int size = 50 + ((Number)this.sizeValue.get()).intValue();
        double xOffset = (double)(sr.func_78326_a() / 2) - 24.5 - ((Number)this.sizeValue.get()).doubleValue() / 2.0;
        double yOffset = (double)(sr.func_78328_b() / 2) - 25.2 - ((Number)this.sizeValue.get()).doubleValue() / 2.0;
        double playerOffsetX = MinecraftInstance.mc.field_71439_g.field_70165_t;
        double playerOffSetZ = MinecraftInstance.mc.field_71439_g.field_70161_v;
        for (Entity entity : MinecraftInstance.mc.field_71441_e.field_72996_f) {
            double rotY;
            double var9;
            double sin;
            if (!EntityUtils.isSelected(entity, true) || ((Boolean)this.noInViewValue.get()).booleanValue() && RenderUtils.isInViewFrustrum(entity)) continue;
            double pos1 = (entity.field_70165_t + (entity.field_70165_t - entity.field_70142_S) * (double)MinecraftInstance.mc.field_71428_T.field_74281_c - playerOffsetX) * 0.2;
            double pos2 = (entity.field_70161_v + (entity.field_70161_v - entity.field_70136_U) * (double)MinecraftInstance.mc.field_71428_T.field_74281_c - playerOffSetZ) * 0.2;
            double cos = Math.cos((double)MinecraftInstance.mc.field_71439_g.field_70177_z * (Math.PI / 180));
            double rotX = -(pos1 * cos + pos2 * (sin = Math.sin((double)MinecraftInstance.mc.field_71439_g.field_70177_z * (Math.PI / 180))));
            double var7 = 0.0 - rotX;
            if (!(MathHelper.func_76133_a((double)(var7 * var7 + (var9 = 0.0 - (rotY = -(pos2 * cos - pos1 * sin))) * var9)) < (float)(size / 2 - 4))) continue;
            float angle = (float)(Math.atan2(rotY - 0.0, rotX - 0.0) * (double)180 / Math.PI);
            double x = (double)(size / 2) * Math.cos(Math.toRadians(angle)) + xOffset + (double)(size / 2);
            double y = (double)(size / 2) * Math.sin(Math.toRadians(angle)) + yOffset + (double)(size / 2);
            GlStateManager.func_179094_E();
            GlStateManager.func_179137_b((double)x, (double)y, (double)0.0);
            GlStateManager.func_179114_b((float)angle, (float)0.0f, (float)0.0f, (float)1.0f);
            GlStateManager.func_179139_a((double)1.0, (double)1.0, (double)1.0);
            RenderUtils.drawTriAngle(0.0f, 0.0f, ((Number)this.radiusValue.get()).floatValue(), 3.0f, color, StringsKt.equals((String)this.modeValue.get(), "solid", true));
            GlStateManager.func_179121_F();
        }
        GlStateManager.func_179121_F();
    }
}

