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
    public void onMove(MoveEvent event) {
    }

    @Override
    public void onTick() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
        if (thePlayer.isInWater()) {
            return;
        }
        if (MovementUtils.isMoving()) {
            if (thePlayer.getOnGround()) {
                if (this.legitJump) {
                    thePlayer.jump();
                    this.legitJump = false;
                    return;
                }
                thePlayer.setMotionY(0.41);
                thePlayer.setOnGround(false);
                MovementUtils.strafe(0.374f);
            } else if (thePlayer.getMotionY() < 0.0) {
                thePlayer.setSpeedInAir(0.0201f);
                MinecraftInstance.mc.getTimer().setTimerSpeed(1.02f);
            } else {
                MinecraftInstance.mc.getTimer().setTimerSpeed(1.01f);
            }
        } else {
            this.legitJump = true;
            thePlayer.setMotionX(0.0);
            thePlayer.setMotionZ(0.0);
        }
    }

    public AAC5BHop() {
        super("AAC5BHop");
    }
}

