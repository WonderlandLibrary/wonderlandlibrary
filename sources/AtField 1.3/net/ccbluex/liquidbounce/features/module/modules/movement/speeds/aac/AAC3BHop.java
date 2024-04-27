/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;

public final class AAC3BHop
extends SpeedMode {
    private boolean legitJump;

    @Override
    public void onUpdate() {
    }

    public AAC3BHop() {
        super("AAC3BHop");
    }

    @Override
    public void onMotion() {
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
                iEntityPlayerSP2.setMotionY(0.3852);
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

    @Override
    public void onMove(MoveEvent moveEvent) {
    }
}

