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
    public void onMotion() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (MovementUtils.isMoving() && !thePlayer.isSneaking()) {
            thePlayer.setCameraPitch(0.0f);
            if (thePlayer.getOnGround()) {
                thePlayer.setMotionY(0.3425);
                IEntityPlayerSP iEntityPlayerSP2 = thePlayer;
                iEntityPlayerSP2.setMotionX(iEntityPlayerSP2.getMotionX() * 1.5893);
                IEntityPlayerSP iEntityPlayerSP3 = thePlayer;
                iEntityPlayerSP3.setMotionZ(iEntityPlayerSP3.getMotionZ() * 1.5893);
            } else {
                thePlayer.setMotionY(-0.19);
            }
        }
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onMove(MoveEvent event) {
    }

    public AACYPort() {
        super("AACYPort");
    }
}

