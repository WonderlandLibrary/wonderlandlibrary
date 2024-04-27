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
import net.dev.important.event.MoveEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.value.BoolValue;
import org.jetbrains.annotations.NotNull;

@Info(name="SafeWalk", spacedName="Safe Walk", description="Prevents you from falling down as if you were sneaking.", category=Category.MOVEMENT, cnName="\u5b89\u5168\u79fb\u52a8")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2={"Lnet/dev/important/modules/module/modules/movement/SafeWalk;", "Lnet/dev/important/modules/module/Module;", "()V", "airSafeValue", "Lnet/dev/important/value/BoolValue;", "onMove", "", "event", "Lnet/dev/important/event/MoveEvent;", "LiquidBounce"})
public final class SafeWalk
extends Module {
    @NotNull
    private final BoolValue airSafeValue = new BoolValue("AirSafe", false);

    @EventTarget
    public final void onMove(@NotNull MoveEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (((Boolean)this.airSafeValue.get()).booleanValue() || MinecraftInstance.mc.field_71439_g.field_70122_E) {
            event.setSafeWalk(true);
        }
    }
}

