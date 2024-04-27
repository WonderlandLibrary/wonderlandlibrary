/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.JumpEvent;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;

public final class AACHop3313
extends SpeedMode {
    @Override
    public void onMotion() {
    }

    @Override
    public void onUpdate() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (!MovementUtils.isMoving() || thePlayer.isInWater() || thePlayer.isInLava() || thePlayer.isOnLadder() || thePlayer.isRiding() || thePlayer.getHurtTime() > 0) {
            return;
        }
        if (thePlayer.getOnGround() && thePlayer.isCollidedVertically()) {
            float yawRad = thePlayer.getRotationYaw() * ((float)Math.PI / 180);
            IEntityPlayerSP iEntityPlayerSP2 = thePlayer;
            double d = iEntityPlayerSP2.getMotionX();
            IEntityPlayerSP iEntityPlayerSP3 = iEntityPlayerSP2;
            boolean bl = false;
            float f = (float)Math.sin(yawRad);
            iEntityPlayerSP3.setMotionX(d - (double)(f * 0.202f));
            IEntityPlayerSP iEntityPlayerSP4 = thePlayer;
            d = iEntityPlayerSP4.getMotionZ();
            iEntityPlayerSP3 = iEntityPlayerSP4;
            bl = false;
            f = (float)Math.cos(yawRad);
            iEntityPlayerSP3.setMotionZ(d + (double)(f * 0.202f));
            thePlayer.setMotionY(0.405);
            LiquidBounce.INSTANCE.getEventManager().callEvent(new JumpEvent(0.405f));
            MovementUtils.strafe$default(0.0f, 1, null);
        } else if (thePlayer.getFallDistance() < 0.31f) {
            if (MinecraftInstance.classProvider.isBlockCarpet(BlockUtils.getBlock(thePlayer.getPosition()))) {
                return;
            }
            thePlayer.setJumpMovementFactor(thePlayer.getMoveStrafing() == 0.0f ? 0.027f : 0.021f);
            IEntityPlayerSP iEntityPlayerSP5 = thePlayer;
            iEntityPlayerSP5.setMotionX(iEntityPlayerSP5.getMotionX() * 1.001);
            IEntityPlayerSP iEntityPlayerSP6 = thePlayer;
            iEntityPlayerSP6.setMotionZ(iEntityPlayerSP6.getMotionZ() * 1.001);
            if (!thePlayer.isCollidedHorizontally()) {
                IEntityPlayerSP iEntityPlayerSP7 = thePlayer;
                iEntityPlayerSP7.setMotionY(iEntityPlayerSP7.getMotionY() - (double)0.014999993f);
            }
        } else {
            thePlayer.setJumpMovementFactor(0.02f);
        }
    }

    @Override
    public void onMove(MoveEvent event) {
    }

    @Override
    public void onDisable() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        iEntityPlayerSP.setJumpMovementFactor(0.02f);
    }

    public AACHop3313() {
        super("AACHop3.3.13");
    }
}

