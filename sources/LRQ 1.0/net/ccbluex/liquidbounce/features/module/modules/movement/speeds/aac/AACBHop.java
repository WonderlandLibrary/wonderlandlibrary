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
    public void onMotion() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (thePlayer.isInWater()) {
            return;
        }
        if (MovementUtils.isMoving()) {
            MinecraftInstance.mc.getTimer().setTimerSpeed(1.08f);
            if (thePlayer.getOnGround()) {
                thePlayer.setMotionY(0.399);
                float f = thePlayer.getRotationYaw() * ((float)Math.PI / 180);
                IEntityPlayerSP iEntityPlayerSP2 = thePlayer;
                double d = iEntityPlayerSP2.getMotionX();
                IEntityPlayerSP iEntityPlayerSP3 = iEntityPlayerSP2;
                boolean bl = false;
                float f2 = (float)Math.sin(f);
                iEntityPlayerSP3.setMotionX(d - (double)(f2 * 0.2f));
                IEntityPlayerSP iEntityPlayerSP4 = thePlayer;
                d = iEntityPlayerSP4.getMotionZ();
                iEntityPlayerSP3 = iEntityPlayerSP4;
                bl = false;
                f2 = (float)Math.cos(f);
                iEntityPlayerSP3.setMotionZ(d + (double)(f2 * 0.2f));
                MinecraftInstance.mc.getTimer().setTimerSpeed(2.0f);
            } else {
                IEntityPlayerSP iEntityPlayerSP5 = thePlayer;
                iEntityPlayerSP5.setMotionY(iEntityPlayerSP5.getMotionY() * 0.97);
                IEntityPlayerSP iEntityPlayerSP6 = thePlayer;
                iEntityPlayerSP6.setMotionX(iEntityPlayerSP6.getMotionX() * 1.008);
                IEntityPlayerSP iEntityPlayerSP7 = thePlayer;
                iEntityPlayerSP7.setMotionZ(iEntityPlayerSP7.getMotionZ() * 1.008);
            }
        } else {
            thePlayer.setMotionX(0.0);
            thePlayer.setMotionZ(0.0);
            MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
        }
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onMove(MoveEvent event) {
    }

    @Override
    public void onDisable() {
        MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
    }

    public AACBHop() {
        super("AACBHop");
    }
}

