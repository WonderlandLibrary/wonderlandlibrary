/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;

public final class AACYPort
extends SpeedMode {
    @Override
    public void onMove(MoveEvent moveEvent) {
    }

    public AACYPort() {
        super("AACYPort");
    }

    @Override
    public void onMotion() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        if (MovementUtils.isMoving() && !iEntityPlayerSP2.isSneaking()) {
            iEntityPlayerSP2.setCameraPitch(0.0f);
            if (iEntityPlayerSP2.getOnGround()) {
                iEntityPlayerSP2.setMotionY(0.3425);
                IEntityPlayerSP iEntityPlayerSP3 = iEntityPlayerSP2;
                iEntityPlayerSP3.setMotionX(iEntityPlayerSP3.getMotionX() * 1.5893);
                IEntityPlayerSP iEntityPlayerSP4 = iEntityPlayerSP2;
                iEntityPlayerSP4.setMotionZ(iEntityPlayerSP4.getMotionZ() * 1.5893);
            } else {
                iEntityPlayerSP2.setMotionY(-0.19);
            }
        }
    }

    @Override
    public void onUpdate() {
    }
}

