/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;

public final class AAC2BHop
extends SpeedMode {
    @Override
    public void onMotion() {
        IEntityPlayerSP thePlayer;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if ((thePlayer = iEntityPlayerSP).isInWater()) {
            return;
        }
        if (MovementUtils.isMoving()) {
            if (thePlayer.getOnGround()) {
                thePlayer.jump();
                IEntityPlayerSP iEntityPlayerSP2 = thePlayer;
                iEntityPlayerSP2.setMotionX(iEntityPlayerSP2.getMotionX() * 1.02);
                IEntityPlayerSP iEntityPlayerSP3 = thePlayer;
                iEntityPlayerSP3.setMotionZ(iEntityPlayerSP3.getMotionZ() * 1.02);
            } else if (thePlayer.getMotionY() > -0.2) {
                thePlayer.setJumpMovementFactor(0.08f);
                IEntityPlayerSP iEntityPlayerSP4 = thePlayer;
                iEntityPlayerSP4.setMotionY(iEntityPlayerSP4.getMotionY() + 0.01431);
                thePlayer.setJumpMovementFactor(0.07f);
            }
        } else {
            thePlayer.setMotionX(0.0);
            thePlayer.setMotionZ(0.0);
        }
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onMove(MoveEvent event) {
    }

    public AAC2BHop() {
        super("AAC2BHop");
    }
}

