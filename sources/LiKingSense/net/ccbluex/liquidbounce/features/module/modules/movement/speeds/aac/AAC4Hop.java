/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016J\b\u0010\u0005\u001a\u00020\u0004H\u0016J\b\u0010\u0006\u001a\u00020\u0004H\u0016J\u0010\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\tH\u0016J\b\u0010\n\u001a\u00020\u0004H\u0016J\b\u0010\u000b\u001a\u00020\u0004H\u0016\u00a8\u0006\f"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/speeds/aac/AAC4Hop;", "Lnet/ccbluex/liquidbounce/features/module/modules/movement/speeds/SpeedMode;", "()V", "onDisable", "", "onEnable", "onMotion", "onMove", "event", "Lnet/ccbluex/liquidbounce/event/MoveEvent;", "onTick", "onUpdate", "LiKingSense"})
public final class AAC4Hop
extends SpeedMode {
    @Override
    public void onDisable() {
        MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
        MinecraftInstance.mc.getThePlayer().setSpeedInAir(0.02f);
    }

    @Override
    public void onTick() {
    }

    @Override
    public void onMotion() {
    }

    @Override
    public void onUpdate() {
        if (MinecraftInstance.mc.getThePlayer().isInWater()) {
            return;
        }
        if (MovementUtils.isMoving()) {
            if (MinecraftInstance.mc.getThePlayer().getOnGround()) {
                MinecraftInstance.mc.getThePlayer().jump();
                MinecraftInstance.mc.getThePlayer().setSpeedInAir(0.0201f);
                MinecraftInstance.mc.getTimer().setTimerSpeed(0.94f);
            }
            if ((double)MinecraftInstance.mc.getThePlayer().getFallDistance() > 0.7 && (double)MinecraftInstance.mc.getThePlayer().getFallDistance() < 1.3) {
                MinecraftInstance.mc.getThePlayer().setSpeedInAir(0.02f);
                MinecraftInstance.mc.getTimer().setTimerSpeed(2.5f);
            }
            if ((double)MinecraftInstance.mc.getThePlayer().getFallDistance() >= 1.3) {
                MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
                MinecraftInstance.mc.getThePlayer().setSpeedInAir(0.02f);
            }
        } else {
            MinecraftInstance.mc.getThePlayer().setMotionX(0.0);
            MinecraftInstance.mc.getThePlayer().setMotionZ(0.0);
        }
    }

    @Override
    public void onMove(@NotNull MoveEvent event) {
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
    }

    @Override
    public void onEnable() {
    }

    public AAC4Hop() {
        super("AAC4Hop");
    }
}

