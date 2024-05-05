/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;

public final class AAC6BHop
extends SpeedMode {
    private boolean legitJump;

    @Override
    public void onUpdate() {
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
                    thePlayer.setMotionY(0.4);
                    MovementUtils.strafe(0.15f);
                    thePlayer.setOnGround(false);
                    this.legitJump = false;
                    return;
                }
                thePlayer.setMotionY(0.41);
                MovementUtils.strafe(0.47458485f);
            }
            if (thePlayer.getMotionY() < 0.0 && thePlayer.getMotionY() > -0.2) {
                MinecraftInstance.mc.getTimer().setTimerSpeed((float)(1.2 + thePlayer.getMotionY()));
            }
            thePlayer.setSpeedInAir(0.022151f);
        } else {
            this.legitJump = true;
            thePlayer.setMotionX(0.0);
            thePlayer.setMotionZ(0.0);
        }
    }

    @Override
    public void onMotion() {
    }

    @Override
    public void onMove(MoveEvent event) {
    }

    @Override
    public void onEnable() {
        this.legitJump = true;
    }

    @Override
    public void onDisable() {
        block0: {
            MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) break block0;
            iEntityPlayerSP.setSpeedInAir(0.02f);
        }
    }

    public AAC6BHop() {
        super("AAC6BHop");
    }
}

