/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;

public final class AAC7BHop
extends SpeedMode {
    @Override
    public void onUpdate() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (!MovementUtils.isMoving() || thePlayer.getRidingEntity() != null || thePlayer.getHurtTime() > 0) {
            return;
        }
        if (thePlayer.getOnGround()) {
            thePlayer.jump();
            thePlayer.setMotionY(0.405);
            IEntityPlayerSP iEntityPlayerSP2 = thePlayer;
            iEntityPlayerSP2.setMotionX(iEntityPlayerSP2.getMotionX() * 1.004);
            IEntityPlayerSP iEntityPlayerSP3 = thePlayer;
            iEntityPlayerSP3.setMotionZ(iEntityPlayerSP3.getMotionZ() * 1.004);
            return;
        }
        double speed = (double)MovementUtils.INSTANCE.getSpeed() * 1.0072;
        double yaw = Math.toRadians(thePlayer.getRotationYaw());
        IEntityPlayerSP iEntityPlayerSP4 = thePlayer;
        boolean bl = false;
        double d = Math.sin(yaw);
        iEntityPlayerSP4.setMotionX(-d * speed);
        iEntityPlayerSP4 = thePlayer;
        bl = false;
        d = Math.cos(yaw);
        iEntityPlayerSP4.setMotionZ(d * speed);
    }

    @Override
    public void onMotion() {
    }

    @Override
    public void onMove(MoveEvent event) {
    }

    public AAC7BHop() {
        super("AAC7BHop");
    }
}

