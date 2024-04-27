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
    public void onMotion() {
    }

    @Override
    public void onMove(MoveEvent moveEvent) {
    }

    @Override
    public void onUpdate() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        if (!MovementUtils.isMoving() || iEntityPlayerSP2.getRidingEntity() != null || iEntityPlayerSP2.getHurtTime() > 0) {
            return;
        }
        if (iEntityPlayerSP2.getOnGround()) {
            iEntityPlayerSP2.jump();
            iEntityPlayerSP2.setMotionY(0.405);
            IEntityPlayerSP iEntityPlayerSP3 = iEntityPlayerSP2;
            iEntityPlayerSP3.setMotionX(iEntityPlayerSP3.getMotionX() * 1.004);
            IEntityPlayerSP iEntityPlayerSP4 = iEntityPlayerSP2;
            iEntityPlayerSP4.setMotionZ(iEntityPlayerSP4.getMotionZ() * 1.004);
            return;
        }
        double d = (double)MovementUtils.INSTANCE.getSpeed() * 1.0072;
        double d2 = Math.toRadians(iEntityPlayerSP2.getRotationYaw());
        IEntityPlayerSP iEntityPlayerSP5 = iEntityPlayerSP2;
        boolean bl = false;
        double d3 = Math.sin(d2);
        iEntityPlayerSP5.setMotionX(-d3 * d);
        iEntityPlayerSP5 = iEntityPlayerSP2;
        bl = false;
        d3 = Math.cos(d2);
        iEntityPlayerSP5.setMotionZ(d3 * d);
    }

    public AAC7BHop() {
        super("AAC7BHop");
    }
}

