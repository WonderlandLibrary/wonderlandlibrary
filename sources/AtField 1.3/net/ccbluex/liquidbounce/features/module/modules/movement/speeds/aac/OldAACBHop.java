/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;

public final class OldAACBHop
extends SpeedMode {
    @Override
    public void onMotion() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        if (MovementUtils.isMoving()) {
            if (iEntityPlayerSP2.getOnGround()) {
                MovementUtils.strafe(0.56f);
                iEntityPlayerSP2.setMotionY(0.42f);
            } else {
                MovementUtils.strafe(MovementUtils.INSTANCE.getSpeed() * (iEntityPlayerSP2.getFallDistance() > 0.4f ? 1.0f : 1.01f));
            }
        } else {
            iEntityPlayerSP2.setMotionX(0.0);
            iEntityPlayerSP2.setMotionZ(0.0);
        }
    }

    public OldAACBHop() {
        super("OldAACBHop");
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onMove(MoveEvent moveEvent) {
    }
}

