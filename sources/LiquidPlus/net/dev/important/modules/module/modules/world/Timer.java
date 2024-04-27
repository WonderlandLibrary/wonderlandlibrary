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
import net.dev.important.event.WorldEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.MovementUtils;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import org.jetbrains.annotations.NotNull;

@Info(name="Timer", description="Changes the speed of the entire game.", category=Category.WORLD, cnName="\u4e16\u754c\u901f\u5ea6")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\b\u001a\u00020\tH\u0016J\u0010\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\fH\u0007J\u0010\u0010\r\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\u000eH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2={"Lnet/dev/important/modules/module/modules/world/Timer;", "Lnet/dev/important/modules/module/Module;", "()V", "autoDisableValue", "Lnet/dev/important/value/BoolValue;", "onMoveValue", "speedValue", "Lnet/dev/important/value/FloatValue;", "onDisable", "", "onUpdate", "event", "Lnet/dev/important/event/UpdateEvent;", "onWorld", "Lnet/dev/important/event/WorldEvent;", "LiquidBounce"})
public final class Timer
extends Module {
    @NotNull
    private final FloatValue speedValue = new FloatValue("Speed", 2.0f, 0.1f, 10.0f, "x");
    @NotNull
    private final BoolValue onMoveValue = new BoolValue("OnMove", true);
    @NotNull
    private final BoolValue autoDisableValue = new BoolValue("AutoDisable", true);

    @Override
    public void onDisable() {
        MinecraftInstance.mc.field_71428_T.field_74278_d = 1.0f;
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (MinecraftInstance.mc.field_71439_g == null || MinecraftInstance.mc.field_71441_e == null) {
            return;
        }
        if (MovementUtils.isMoving() || !((Boolean)this.onMoveValue.get()).booleanValue()) {
            MinecraftInstance.mc.field_71428_T.field_74278_d = ((Number)this.speedValue.get()).floatValue();
            return;
        }
        MinecraftInstance.mc.field_71428_T.field_74278_d = 1.0f;
    }

    @EventTarget
    public final void onWorld(@NotNull WorldEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (event.getWorldClient() != null) {
            return;
        }
        if (((Boolean)this.autoDisableValue.get()).booleanValue()) {
            this.setState(false);
        }
    }
}

