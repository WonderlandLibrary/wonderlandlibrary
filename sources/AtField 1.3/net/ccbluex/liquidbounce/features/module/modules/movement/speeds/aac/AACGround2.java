/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.util.ITimer;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;

public final class AACGround2
extends SpeedMode {
    @Override
    public void onMotion() {
    }

    @Override
    public void onUpdate() {
        if (!MovementUtils.isMoving()) {
            return;
        }
        ITimer iTimer = MinecraftInstance.mc.getTimer();
        Speed speed = (Speed)LiquidBounce.INSTANCE.getModuleManager().getModule(Speed.class);
        if (speed == null) {
            Intrinsics.throwNpe();
        }
        iTimer.setTimerSpeed(((Number)speed.getAacGroundTimerValue().get()).floatValue());
        MovementUtils.strafe(0.02f);
    }

    @Override
    public void onMove(MoveEvent moveEvent) {
    }

    public AACGround2() {
        super("AACGround2");
    }

    @Override
    public void onDisable() {
        MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
    }
}

