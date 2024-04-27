/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;

public final class AACBHop
extends SpeedMode {
    @Override
    public void onMove(MoveEvent moveEvent) {
    }

    @Override
    public void onDisable() {
        MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
    }

    @Override
    public void onMotion() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        if (iEntityPlayerSP2.isInWater()) {
            return;
        }
        if (MovementUtils.isMoving()) {
            MinecraftInstance.mc.getTimer().setTimerSpeed(1.08f);
            if (iEntityPlayerSP2.getOnGround()) {
                iEntityPlayerSP2.setMotionY(0.399);
                float f = iEntityPlayerSP2.getRotationYaw() * ((float)Math.PI / 180);
                IEntityPlayerSP iEntityPlayerSP3 = iEntityPlayerSP2;
                double d = iEntityPlayerSP3.getMotionX();
                IEntityPlayerSP iEntityPlayerSP4 = iEntityPlayerSP3;
                boolean bl = false;
                float f2 = (float)Math.sin(f);
                iEntityPlayerSP4.setMotionX(d - (double)(f2 * 0.2f));
                IEntityPlayerSP iEntityPlayerSP5 = iEntityPlayerSP2;
                d = iEntityPlayerSP5.getMotionZ();
                iEntityPlayerSP4 = iEntityPlayerSP5;
                bl = false;
                f2 = (float)Math.cos(f);
                iEntityPlayerSP4.setMotionZ(d + (double)(f2 * 0.2f));
                MinecraftInstance.mc.getTimer().setTimerSpeed(2.0f);
            } else {
                IEntityPlayerSP iEntityPlayerSP6 = iEntityPlayerSP2;
                iEntityPlayerSP6.setMotionY(iEntityPlayerSP6.getMotionY() * 0.97);
                IEntityPlayerSP iEntityPlayerSP7 = iEntityPlayerSP2;
                iEntityPlayerSP7.setMotionX(iEntityPlayerSP7.getMotionX() * 1.008);
                IEntityPlayerSP iEntityPlayerSP8 = iEntityPlayerSP2;
                iEntityPlayerSP8.setMotionZ(iEntityPlayerSP8.getMotionZ() * 1.008);
            }
        } else {
            iEntityPlayerSP2.setMotionX(0.0);
            iEntityPlayerSP2.setMotionZ(0.0);
            MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
        }
    }

    @Override
    public void onUpdate() {
    }

    public AACBHop() {
        super("AACBHop");
    }
}

