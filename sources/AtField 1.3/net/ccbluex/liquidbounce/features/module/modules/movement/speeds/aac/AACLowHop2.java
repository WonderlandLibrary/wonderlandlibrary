/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;

public final class AACLowHop2
extends SpeedMode {
    private boolean legitJump;

    @Override
    public void onEnable() {
        this.legitJump = true;
        MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
    }

    @Override
    public void onMotion() {
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
            MinecraftInstance.mc.getTimer().setTimerSpeed(1.09f);
            if (iEntityPlayerSP2.getOnGround()) {
                if (this.legitJump) {
                    iEntityPlayerSP2.jump();
                    this.legitJump = false;
                    return;
                }
                iEntityPlayerSP2.setMotionY(0.343);
                MovementUtils.strafe(0.534f);
            }
        } else {
            this.legitJump = true;
            iEntityPlayerSP2.setMotionX(0.0);
            iEntityPlayerSP2.setMotionZ(0.0);
        }
    }

    public AACLowHop2() {
        super("AACLowHop2");
    }

    @Override
    public void onDisable() {
        MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
    }

    @Override
    public void onMove(MoveEvent moveEvent) {
    }

    @Override
    public void onUpdate() {
    }
}

