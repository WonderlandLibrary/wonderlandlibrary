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
    public void onUpdate() {
    }

    @Override
    public void onMove(MoveEvent moveEvent) {
    }

    public AAC2BHop() {
        super("AAC2BHop");
    }

    @Override
    public void onMotion() {
        IEntityPlayerSP iEntityPlayerSP;
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        if ((iEntityPlayerSP = iEntityPlayerSP2).isInWater()) {
            return;
        }
        if (MovementUtils.isMoving()) {
            if (iEntityPlayerSP.getOnGround()) {
                iEntityPlayerSP.jump();
                IEntityPlayerSP iEntityPlayerSP3 = iEntityPlayerSP;
                iEntityPlayerSP3.setMotionX(iEntityPlayerSP3.getMotionX() * 1.02);
                IEntityPlayerSP iEntityPlayerSP4 = iEntityPlayerSP;
                iEntityPlayerSP4.setMotionZ(iEntityPlayerSP4.getMotionZ() * 1.02);
            } else if (iEntityPlayerSP.getMotionY() > -0.2) {
                iEntityPlayerSP.setJumpMovementFactor(0.08f);
                IEntityPlayerSP iEntityPlayerSP5 = iEntityPlayerSP;
                iEntityPlayerSP5.setMotionY(iEntityPlayerSP5.getMotionY() + 0.01431);
                iEntityPlayerSP.setJumpMovementFactor(0.07f);
            }
        } else {
            iEntityPlayerSP.setMotionX(0.0);
            iEntityPlayerSP.setMotionZ(0.0);
        }
    }
}

