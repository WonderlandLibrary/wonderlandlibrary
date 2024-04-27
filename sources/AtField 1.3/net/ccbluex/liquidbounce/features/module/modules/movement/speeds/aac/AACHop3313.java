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
    public void onDisable() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        iEntityPlayerSP.setJumpMovementFactor(0.02f);
    }

    @Override
    public void onMotion() {
    }

    public AACHop3313() {
        super("AACHop3.3.13");
    }

    @Override
    public void onUpdate() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        if (!MovementUtils.isMoving() || iEntityPlayerSP2.isInWater() || iEntityPlayerSP2.isInLava() || iEntityPlayerSP2.isOnLadder() || iEntityPlayerSP2.isRiding() || iEntityPlayerSP2.getHurtTime() > 0) {
            return;
        }
        if (iEntityPlayerSP2.getOnGround() && iEntityPlayerSP2.isCollidedVertically()) {
            float f = iEntityPlayerSP2.getRotationYaw() * ((float)Math.PI / 180);
            IEntityPlayerSP iEntityPlayerSP3 = iEntityPlayerSP2;
            double d = iEntityPlayerSP3.getMotionX();
            IEntityPlayerSP iEntityPlayerSP4 = iEntityPlayerSP3;
            boolean bl = false;
            float f2 = (float)Math.sin(f);
            iEntityPlayerSP4.setMotionX(d - (double)(f2 * 0.202f));
            IEntityPlayerSP iEntityPlayerSP5 = iEntityPlayerSP2;
            d = iEntityPlayerSP5.getMotionZ();
            iEntityPlayerSP4 = iEntityPlayerSP5;
            bl = false;
            f2 = (float)Math.cos(f);
            iEntityPlayerSP4.setMotionZ(d + (double)(f2 * 0.202f));
            iEntityPlayerSP2.setMotionY(0.405);
            LiquidBounce.INSTANCE.getEventManager().callEvent(new JumpEvent(0.405f));
            MovementUtils.strafe$default(0.0f, 1, null);
        } else if (iEntityPlayerSP2.getFallDistance() < 0.31f) {
            if (MinecraftInstance.classProvider.isBlockCarpet(BlockUtils.getBlock(iEntityPlayerSP2.getPosition()))) {
                return;
            }
            iEntityPlayerSP2.setJumpMovementFactor(iEntityPlayerSP2.getMoveStrafing() == 0.0f ? 0.027f : 0.021f);
            IEntityPlayerSP iEntityPlayerSP6 = iEntityPlayerSP2;
            iEntityPlayerSP6.setMotionX(iEntityPlayerSP6.getMotionX() * 1.001);
            IEntityPlayerSP iEntityPlayerSP7 = iEntityPlayerSP2;
            iEntityPlayerSP7.setMotionZ(iEntityPlayerSP7.getMotionZ() * 1.001);
            if (!iEntityPlayerSP2.isCollidedHorizontally()) {
                IEntityPlayerSP iEntityPlayerSP8 = iEntityPlayerSP2;
                iEntityPlayerSP8.setMotionY(iEntityPlayerSP8.getMotionY() - (double)0.014999993f);
            }
        } else {
            iEntityPlayerSP2.setJumpMovementFactor(0.02f);
        }
    }

    @Override
    public void onMove(MoveEvent moveEvent) {
    }
}

