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
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (MovementUtils.isMoving()) {
            if (thePlayer.getOnGround()) {
                MovementUtils.strafe(0.56f);
                thePlayer.setMotionY(0.42f);
            } else {
                MovementUtils.strafe(MovementUtils.INSTANCE.getSpeed() * (thePlayer.getFallDistance() > 0.4f ? 1.0f : 1.01f));
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

    public OldAACBHop() {
        super("OldAACBHop");
    }
}

