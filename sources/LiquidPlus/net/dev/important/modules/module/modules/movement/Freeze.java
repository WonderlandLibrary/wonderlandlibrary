/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
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
import org.jetbrains.annotations.NotNull;

@Info(name="Freeze", description="Allows you to stay stuck in mid air.", category=Category.MOVEMENT, cnName="\u7a7a\u4e2d\u505c\u6ede")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007H\u0007\u00a8\u0006\b"}, d2={"Lnet/dev/important/modules/module/modules/movement/Freeze;", "Lnet/dev/important/modules/module/Module;", "()V", "onDisable", "", "onUpdate", "event", "Lnet/dev/important/event/UpdateEvent;", "LiquidBounce"})
public final class Freeze
extends Module {
    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        MinecraftInstance.mc.field_71439_g.field_70128_L = true;
        MinecraftInstance.mc.field_71439_g.field_71109_bG = MinecraftInstance.mc.field_71439_g.field_70177_z;
        MinecraftInstance.mc.field_71439_g.field_70726_aT = MinecraftInstance.mc.field_71439_g.field_70125_A;
    }

    @Override
    public void onDisable() {
        if (MinecraftInstance.mc.field_71439_g != null) {
            MinecraftInstance.mc.field_71439_g.field_70128_L = false;
        }
    }
}

