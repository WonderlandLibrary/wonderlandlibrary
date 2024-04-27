/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityLivingBase
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.movement.speeds.other;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.Client;
import net.dev.important.event.MoveEvent;
import net.dev.important.modules.module.Module;
import net.dev.important.modules.module.modules.combat.KillAura;
import net.dev.important.modules.module.modules.movement.speeds.SpeedMode;
import net.dev.important.utils.MovementUtils;
import net.minecraft.entity.EntityLivingBase;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\b\u0010\b\u001a\u00020\u0004H\u0016\u00a8\u0006\t"}, d2={"Lnet/dev/important/modules/module/modules/movement/speeds/other/VulcanLowHop;", "Lnet/dev/important/modules/module/modules/movement/speeds/SpeedMode;", "()V", "onMotion", "", "onMove", "event", "Lnet/dev/important/event/MoveEvent;", "onUpdate", "LiquidBounce"})
public final class VulcanLowHop
extends SpeedMode {
    public VulcanLowHop() {
        super("VulcanLowHop");
    }

    @Override
    public void onMotion() {
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onMove(@NotNull MoveEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        Module module2 = Client.INSTANCE.getModuleManager().getModule(KillAura.class);
        if (module2 == null) {
            throw new NullPointerException("null cannot be cast to non-null type net.dev.important.modules.module.modules.combat.KillAura");
        }
        EntityLivingBase target = ((KillAura)module2).getTarget();
        if (!(target == null || target.field_70737_aN <= 0 || SpeedMode.mc.field_71439_g.field_70134_J || SpeedMode.mc.field_71439_g.func_180799_ab() || SpeedMode.mc.field_71439_g.func_70090_H() || SpeedMode.mc.field_71439_g.func_70617_f_() || SpeedMode.mc.field_71439_g.field_70154_o != null || !MovementUtils.isMoving())) {
            SpeedMode.mc.field_71474_y.field_74314_A.field_74513_e = false;
            if (SpeedMode.mc.field_71439_g.field_70122_E) {
                SpeedMode.mc.field_71439_g.func_70664_aZ();
                SpeedMode.mc.field_71439_g.field_70181_x = 0.0;
                MovementUtils.strafe(0.65f);
                event.setY(0.42);
            }
            MovementUtils.strafe();
        }
    }
}

