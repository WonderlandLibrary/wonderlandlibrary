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

public final class AAC4BHop
extends SpeedMode {
    private boolean legitHop;

    @Override
    public void onDisable() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        iEntityPlayerSP.setSpeedInAir(0.02f);
    }

    @Override
    public void onTick() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (MovementUtils.isMoving()) {
            if (this.legitHop) {
                if (thePlayer.getOnGround()) {
                    thePlayer.jump();
                    thePlayer.setOnGround(false);
                    this.legitHop = false;
                }
                return;
            }
            if (thePlayer.getOnGround()) {
                thePlayer.setOnGround(false);
                MovementUtils.strafe(0.375f);
                thePlayer.jump();
                thePlayer.setMotionY(0.41);
            } else {
                thePlayer.setSpeedInAir(0.0211f);
            }
        } else {
            thePlayer.setMotionX(0.0);
            thePlayer.setMotionZ(0.0);
            this.legitHop = true;
        }
    }

    @Override
    public void onMotion() {
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onMove(MoveEvent event) {
    }

    @Override
    public void onEnable() {
        this.legitHop = true;
    }

    public AAC4BHop() {
        super("AAC4BHop");
    }
}

