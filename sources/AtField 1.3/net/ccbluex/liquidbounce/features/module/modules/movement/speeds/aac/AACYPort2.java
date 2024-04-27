/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;

public final class AACYPort2
extends SpeedMode {
    @Override
    public void onMotion() {
        if (MovementUtils.isMoving()) {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                return;
            }
            IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
            iEntityPlayerSP2.setCameraPitch(0.0f);
            if (iEntityPlayerSP2.getOnGround()) {
                iEntityPlayerSP2.jump();
                iEntityPlayerSP2.setMotionY(0.3851);
                IEntityPlayerSP iEntityPlayerSP3 = iEntityPlayerSP2;
                iEntityPlayerSP3.setMotionX(iEntityPlayerSP3.getMotionX() * 1.01);
                IEntityPlayerSP iEntityPlayerSP4 = iEntityPlayerSP2;
                iEntityPlayerSP4.setMotionZ(iEntityPlayerSP4.getMotionZ() * 1.01);
            } else {
                iEntityPlayerSP2.setMotionY(-0.21);
            }
        }
    }

    @Override
    public void onMove(MoveEvent moveEvent) {
    }

    public AACYPort2() {
        super("AACYPort2");
    }

    @Override
    public void onUpdate() {
    }
}

