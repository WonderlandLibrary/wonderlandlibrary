/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.world;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.event.EventTarget;
import net.dev.important.event.UpdateEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.modules.module.modules.world.Fucker;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.value.FloatValue;
import org.jetbrains.annotations.NotNull;

@Info(name="FastBreak", spacedName="Fast Break", description="Allows you to break blocks faster.", category=Category.WORLD, cnName="\u5feb\u901f\u51fb\u6253\u65b9\u5757")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2={"Lnet/dev/important/modules/module/modules/world/FastBreak;", "Lnet/dev/important/modules/module/Module;", "()V", "breakDamage", "Lnet/dev/important/value/FloatValue;", "onUpdate", "", "event", "Lnet/dev/important/event/UpdateEvent;", "LiquidBounce"})
public final class FastBreak
extends Module {
    @NotNull
    private final FloatValue breakDamage = new FloatValue("BreakDamage", 0.8f, 0.1f, 1.0f);

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        MinecraftInstance.mc.field_71442_b.field_78781_i = 0;
        if (MinecraftInstance.mc.field_71442_b.field_78770_f > ((Number)this.breakDamage.get()).floatValue()) {
            MinecraftInstance.mc.field_71442_b.field_78770_f = 1.0f;
        }
        if (Fucker.INSTANCE.getCurrentDamage() > ((Number)this.breakDamage.get()).floatValue()) {
            Fucker.INSTANCE.setCurrentDamage(1.0f);
        }
    }
}

