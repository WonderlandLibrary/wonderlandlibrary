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
    public void onMotion() {
    }

    public AAC6BHop() {
        super("AAC6BHop");
    }

    @Override
    public void onMove(MoveEvent moveEvent) {
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

    @Override
    public void onUpdate() {
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
                    iEntityPlayerSP2.setMotionY(0.4);
                    MovementUtils.strafe(0.15f);
                    iEntityPlayerSP2.setOnGround(false);
                    this.legitJump = false;
                    return;
                }
                iEntityPlayerSP2.setMotionY(0.41);
                MovementUtils.strafe(0.47458485f);
            }
            if (iEntityPlayerSP2.getMotionY() < 0.0 && iEntityPlayerSP2.getMotionY() > -0.2) {
                MinecraftInstance.mc.getTimer().setTimerSpeed((float)(1.2 + iEntityPlayerSP2.getMotionY()));
            }
            iEntityPlayerSP2.setSpeedInAir(0.022151f);
        } else {
            this.legitJump = true;
            iEntityPlayerSP2.setMotionX(0.0);
            iEntityPlayerSP2.setMotionZ(0.0);
        }
    }
}

