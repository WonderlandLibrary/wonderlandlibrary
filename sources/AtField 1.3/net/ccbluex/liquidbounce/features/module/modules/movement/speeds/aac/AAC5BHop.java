/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;

public final class AAC5BHop
extends SpeedMode {
    private boolean legitJump;

    @Override
    public void onMotion() {
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onTick() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
        if (iEntityPlayerSP2.isInWater()) {
            return;
        }
        if (MovementUtils.isMoving()) {
            if (iEntityPlayerSP2.getOnGround()) {
                if (this.legitJump) {
                    iEntityPlayerSP2.jump();
                    this.legitJump = false;
                    return;
                }
                iEntityPlayerSP2.setMotionY(0.41);
                iEntityPlayerSP2.setOnGround(false);
                MovementUtils.strafe(0.374f);
            } else if (iEntityPlayerSP2.getMotionY() < 0.0) {
                iEntityPlayerSP2.setSpeedInAir(0.0201f);
                MinecraftInstance.mc.getTimer().setTimerSpeed(1.02f);
            } else {
                MinecraftInstance.mc.getTimer().setTimerSpeed(1.01f);
            }
        } else {
            this.legitJump = true;
            iEntityPlayerSP2.setMotionX(0.0);
            iEntityPlayerSP2.setMotionZ(0.0);
        }
    }

    public AAC5BHop() {
        super("AAC5BHop");
    }

    @Override
    public void onMove(MoveEvent moveEvent) {
    }
}

