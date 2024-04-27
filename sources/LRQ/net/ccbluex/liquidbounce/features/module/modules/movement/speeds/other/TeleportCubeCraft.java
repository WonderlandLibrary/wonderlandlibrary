/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;

public final class TeleportCubeCraft
extends SpeedMode {
    private final MSTimer timer = new MSTimer();

    @Override
    public void onMotion() {
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onMove(MoveEvent event) {
        if (MovementUtils.isMoving()) {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP.getOnGround() && this.timer.hasTimePassed(300L)) {
                double yaw = MovementUtils.getDirection();
                Speed speed = (Speed)LiquidBounce.INSTANCE.getModuleManager().getModule(Speed.class);
                if (speed == null) {
                    Intrinsics.throwNpe();
                }
                float length = ((Number)speed.getCubecraftPortLengthValue().get()).floatValue();
                MoveEvent moveEvent = event;
                boolean bl = false;
                double d = Math.sin(yaw);
                moveEvent.setX(-d * (double)length);
                moveEvent = event;
                bl = false;
                d = Math.cos(yaw);
                moveEvent.setZ(d * (double)length);
                this.timer.reset();
            }
        }
    }

    public TeleportCubeCraft() {
        super("TeleportCubeCraft");
    }
}

