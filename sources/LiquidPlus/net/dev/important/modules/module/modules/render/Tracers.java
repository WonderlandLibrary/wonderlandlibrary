/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.Vec3
 *  org.jetbrains.annotations.NotNull
 *  org.lwjgl.opengl.GL11
 */
package net.dev.important.modules.module.modules.render;

import java.awt.Color;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.event.EventTarget;
import net.dev.important.event.Render3DEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.EntityUtils;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.render.ColorUtils;
import net.dev.important.utils.render.RenderUtils;
import net.dev.important.value.FloatValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

@Info(name="Tracers", description="Draws a line to targets around you.", category=Category.RENDER, cnName="\u73a9\u5bb6\u8fde\u7ebf")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012J\u0010\u0010\u0013\u001a\u00020\f2\u0006\u0010\u0014\u001a\u00020\u0015H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2={"Lnet/dev/important/modules/module/modules/render/Tracers;", "Lnet/dev/important/modules/module/Module;", "()V", "colorBlueValue", "Lnet/dev/important/value/IntegerValue;", "colorGreenValue", "colorMode", "Lnet/dev/important/value/ListValue;", "colorRedValue", "thicknessValue", "Lnet/dev/important/value/FloatValue;", "drawTraces", "", "entity", "Lnet/minecraft/entity/Entity;", "color", "Ljava/awt/Color;", "drawHeight", "", "onRender3D", "event", "Lnet/dev/important/event/Render3DEvent;", "LiquidBounce"})
public final class Tracers
extends Module {
    @NotNull
    private final ListValue colorMode;
    @NotNull
    private final FloatValue thicknessValue;
    @NotNull
    private final IntegerValue colorRedValue;
    @NotNull
    private final IntegerValue colorGreenValue;
    @NotNull
    private final IntegerValue colorBlueValue;

    public Tracers() {
        String[] stringArray = new String[]{"Custom", "DistanceColor", "Rainbow"};
        this.colorMode = new ListValue("Color", stringArray, "Custom");
        this.thicknessValue = new FloatValue("Thickness", 2.0f, 1.0f, 5.0f);
        this.colorRedValue = new IntegerValue("R", 0, 0, 255);
        this.colorGreenValue = new IntegerValue("G", 160, 0, 255);
        this.colorBlueValue = new IntegerValue("B", 255, 0, 255);
    }

    @EventTarget
    public final void onRender3D(@NotNull Render3DEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)((Number)this.thicknessValue.get()).floatValue());
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glBegin((int)1);
        for (Entity entity : MinecraftInstance.mc.field_71441_e.field_72996_f) {
            if (entity == null || Intrinsics.areEqual(entity, MinecraftInstance.mc.field_71439_g) || !EntityUtils.isSelected(entity, false)) continue;
            int dist = (int)(MinecraftInstance.mc.field_71439_g.func_70032_d(entity) * (float)2);
            if (dist > 255) {
                dist = 255;
            }
            String string = ((String)this.colorMode.get()).toLowerCase();
            Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase()");
            String colorMode = string;
            Color color = EntityUtils.isFriend(entity) ? new Color(0, 0, 255, 150) : (colorMode.equals("custom") ? new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue(), 150) : (colorMode.equals("distancecolor") ? new Color(255 - dist, dist, 0, 150) : (colorMode.equals("rainbow") ? ColorUtils.rainbow() : new Color(255, 255, 255, 150))));
            this.drawTraces(entity, color, true);
        }
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GlStateManager.func_179117_G();
    }

    public final void drawTraces(@NotNull Entity entity, @NotNull Color color, boolean drawHeight) {
        Intrinsics.checkNotNullParameter(entity, "entity");
        Intrinsics.checkNotNullParameter(color, "color");
        double x = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * (double)MinecraftInstance.mc.field_71428_T.field_74281_c - MinecraftInstance.mc.func_175598_ae().field_78725_b;
        double y = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * (double)MinecraftInstance.mc.field_71428_T.field_74281_c - MinecraftInstance.mc.func_175598_ae().field_78726_c;
        double z = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * (double)MinecraftInstance.mc.field_71428_T.field_74281_c - MinecraftInstance.mc.func_175598_ae().field_78723_d;
        Vec3 eyeVector = new Vec3(0.0, 0.0, 1.0).func_178789_a((float)(-Math.toRadians(MinecraftInstance.mc.field_71439_g.field_70125_A))).func_178785_b((float)(-Math.toRadians(MinecraftInstance.mc.field_71439_g.field_70177_z)));
        RenderUtils.glColor(color);
        GL11.glVertex3d((double)eyeVector.field_72450_a, (double)((double)MinecraftInstance.mc.field_71439_g.func_70047_e() + eyeVector.field_72448_b), (double)eyeVector.field_72449_c);
        if (drawHeight) {
            GL11.glVertex3d((double)x, (double)y, (double)z);
            GL11.glVertex3d((double)x, (double)y, (double)z);
            GL11.glVertex3d((double)x, (double)(y + (double)entity.field_70131_O), (double)z);
        } else {
            GL11.glVertex3d((double)x, (double)(y + (double)entity.field_70131_O / 2.0), (double)z);
        }
    }
}

