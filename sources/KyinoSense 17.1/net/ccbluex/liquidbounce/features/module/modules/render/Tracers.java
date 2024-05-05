/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.Vec3
 *  org.jetbrains.annotations.NotNull
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import java.awt.Color;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name="Tracers", description="Draws a line to targets around you.", category=ModuleCategory.RENDER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0002J\u0010\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\u000fH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/Tracers;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "distanceColorValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "thicknessValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "drawTraces", "", "entity", "Lnet/minecraft/entity/Entity;", "color", "Ljava/awt/Color;", "onRender3D", "event", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "KyinoClient"})
public final class Tracers
extends Module {
    private final FloatValue thicknessValue = new FloatValue("Thickness", 2.0f, 1.0f, 5.0f);
    private final BoolValue distanceColorValue = new BoolValue("DistanceColor", false);

    @EventTarget
    public final void onRender3D(@NotNull Render3DEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)((Number)this.thicknessValue.get()).floatValue());
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glBegin((int)1);
        for (Entity entity : Tracers.access$getMc$p$s1046033730().field_71441_e.field_72996_f) {
            if (entity == null || !(Intrinsics.areEqual(entity, Tracers.access$getMc$p$s1046033730().field_71439_g) ^ true) || !EntityUtils.isSelected(entity, false)) continue;
            int dist = (int)(Tracers.access$getMc$p$s1046033730().field_71439_g.func_70032_d(entity) * (float)2);
            if (dist > 255) {
                dist = 255;
            }
            Color color = EntityUtils.isFriend(entity) ? new Color(0, 0, 255, 150) : ((Boolean)this.distanceColorValue.get() != false ? new Color(255 - dist, dist, 0, 150) : new Color(255, 255, 255, 150));
            this.drawTraces(entity, color);
        }
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GlStateManager.func_179117_G();
    }

    private final void drawTraces(Entity entity, Color color) {
        double d = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * (double)Tracers.access$getMc$p$s1046033730().field_71428_T.field_74281_c;
        Minecraft minecraft = Tracers.access$getMc$p$s1046033730();
        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
        double x = d - minecraft.func_175598_ae().field_78725_b;
        double d2 = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * (double)Tracers.access$getMc$p$s1046033730().field_71428_T.field_74281_c;
        Minecraft minecraft2 = Tracers.access$getMc$p$s1046033730();
        Intrinsics.checkExpressionValueIsNotNull(minecraft2, "mc");
        double y = d2 - minecraft2.func_175598_ae().field_78726_c;
        double d3 = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * (double)Tracers.access$getMc$p$s1046033730().field_71428_T.field_74281_c;
        Minecraft minecraft3 = Tracers.access$getMc$p$s1046033730();
        Intrinsics.checkExpressionValueIsNotNull(minecraft3, "mc");
        double z = d3 - minecraft3.func_175598_ae().field_78723_d;
        Vec3 eyeVector = new Vec3(0.0, 0.0, 1.0).func_178789_a((float)(-Math.toRadians(Tracers.access$getMc$p$s1046033730().field_71439_g.field_70125_A))).func_178785_b((float)(-Math.toRadians(Tracers.access$getMc$p$s1046033730().field_71439_g.field_70177_z)));
        RenderUtils.glColor(color);
        GL11.glVertex3d((double)eyeVector.field_72450_a, (double)((double)Tracers.access$getMc$p$s1046033730().field_71439_g.func_70047_e() + eyeVector.field_72448_b), (double)eyeVector.field_72449_c);
        GL11.glVertex3d((double)x, (double)y, (double)z);
        GL11.glVertex3d((double)x, (double)y, (double)z);
        GL11.glVertex3d((double)x, (double)(y + (double)entity.field_70131_O), (double)z);
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

