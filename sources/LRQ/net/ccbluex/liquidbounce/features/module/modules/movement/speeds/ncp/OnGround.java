/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp;

import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;

public final class OnGround
extends SpeedMode {
    @Override
    public void onMotion() {
        IEntityPlayerSP thePlayer = MinecraftInstance.mc.getThePlayer();
        if (thePlayer == null || !MovementUtils.isMoving()) {
            return;
        }
        if ((double)thePlayer.getFallDistance() > 3.994) {
            return;
        }
        if (thePlayer.isInWater() || thePlayer.isOnLadder() || thePlayer.isCollidedHorizontally()) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP = thePlayer;
        iEntityPlayerSP.setPosY(iEntityPlayerSP.getPosY() - (double)0.3993f);
        thePlayer.setMotionY(-1000.0);
        thePlayer.setCameraPitch(0.3f);
        thePlayer.setDistanceWalkedModified(44.0f);
        MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
        if (thePlayer.getOnGround()) {
            IEntityPlayerSP iEntityPlayerSP2 = thePlayer;
            iEntityPlayerSP2.setPosY(iEntityPlayerSP2.getPosY() + (double)0.3993f);
            thePlayer.setMotionY(0.3993f);
            thePlayer.setDistanceWalkedOnStepModified(44.0f);
            IEntityPlayerSP iEntityPlayerSP3 = thePlayer;
            iEntityPlayerSP3.setMotionX(iEntityPlayerSP3.getMotionX() * (double)1.59f);
            IEntityPlayerSP iEntityPlayerSP4 = thePlayer;
            iEntityPlayerSP4.setMotionZ(iEntityPlayerSP4.getMotionZ() * (double)1.59f);
            thePlayer.setCameraPitch(0.0f);
            MinecraftInstance.mc.getTimer().setTimerSpeed(1.199f);
        }
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onMove(MoveEvent event) {
    }

    public OnGround() {
        super("OnGround");
    }
}

