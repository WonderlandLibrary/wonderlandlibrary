/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.movement;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.event.EventTarget;
import net.dev.important.event.UpdateEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.MovementUtils;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.NotNull;

@Info(name="Parkour", description="Automatically jumps when reaching the edge of a block.", category=Category.MOVEMENT, cnName="\u65b9\u5757\u8fb9\u7f18\u8df3\u8dc3")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u00a8\u0006\u0007"}, d2={"Lnet/dev/important/modules/module/modules/movement/Parkour;", "Lnet/dev/important/modules/module/Module;", "()V", "onUpdate", "", "event", "Lnet/dev/important/event/UpdateEvent;", "LiquidBounce"})
public final class Parkour
extends Module {
    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (MovementUtils.isMoving() && MinecraftInstance.mc.field_71439_g.field_70122_E && !MinecraftInstance.mc.field_71439_g.func_70093_af() && !MinecraftInstance.mc.field_71474_y.field_74311_E.func_151470_d() && !MinecraftInstance.mc.field_71474_y.field_74314_A.func_151470_d() && MinecraftInstance.mc.field_71441_e.func_72945_a((Entity)MinecraftInstance.mc.field_71439_g, MinecraftInstance.mc.field_71439_g.func_174813_aQ().func_72317_d(0.0, -0.5, 0.0).func_72314_b(-0.001, 0.0, -0.001)).isEmpty()) {
            MinecraftInstance.mc.field_71439_g.func_70664_aZ();
        }
    }
}

