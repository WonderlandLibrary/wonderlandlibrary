package net.ccbluex.liquidbounce.features.module.modules.world;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="Timer", description="Changes the speed of the entire game.", category=ModuleCategory.WORLD)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000,\n\n\n\b\n\n\u0000\n\n\u0000\n\n\b\n\n\u0000\n\n\u0000\b\u000020B¢J\b0\bHJ\t0\b2\n0HJ\f0\b2\n0\rHR0X¢\n\u0000R0X¢\n\u0000¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/world/Timer;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "onMoveValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "speedValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "onDisable", "", "onUpdate", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "onWorld", "Lnet/ccbluex/liquidbounce/event/WorldEvent;", "Pride"})
public final class Timer
extends Module {
    private final FloatValue speedValue = new FloatValue("Speed", 2.0f, 0.1f, 10.0f);
    private final BoolValue onMoveValue = new BoolValue("OnMove", true);

    @Override
    public void onDisable() {
        if (MinecraftInstance.mc.getThePlayer() == null) {
            return;
        }
        MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (MovementUtils.isMoving() || !((Boolean)this.onMoveValue.get()).booleanValue()) {
            MinecraftInstance.mc.getTimer().setTimerSpeed(((Number)this.speedValue.get()).floatValue());
            return;
        }
        MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
    }

    @EventTarget
    public final void onWorld(@NotNull WorldEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (event.getWorldClient() != null) {
            return;
        }
        this.setState(false);
    }
}
