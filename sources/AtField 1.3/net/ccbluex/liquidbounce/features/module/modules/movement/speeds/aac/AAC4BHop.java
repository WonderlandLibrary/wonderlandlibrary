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
    public void onUpdate() {
    }

    @Override
    public void onTick() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        if (MovementUtils.isMoving()) {
            if (this.legitHop) {
                if (iEntityPlayerSP2.getOnGround()) {
                    iEntityPlayerSP2.jump();
                    iEntityPlayerSP2.setOnGround(false);
                    this.legitHop = false;
                }
                return;
            }
            if (iEntityPlayerSP2.getOnGround()) {
                iEntityPlayerSP2.setOnGround(false);
                MovementUtils.strafe(0.375f);
                iEntityPlayerSP2.jump();
                iEntityPlayerSP2.setMotionY(0.41);
            } else {
                iEntityPlayerSP2.setSpeedInAir(0.0211f);
            }
        } else {
            iEntityPlayerSP2.setMotionX(0.0);
            iEntityPlayerSP2.setMotionZ(0.0);
            this.legitHop = true;
        }
    }

    @Override
    public void onMove(MoveEvent moveEvent) {
    }

    @Override
    public void onDisable() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        iEntityPlayerSP.setSpeedInAir(0.02f);
    }

    @Override
    public void onEnable() {
        this.legitHop = true;
    }

    public AAC4BHop() {
        super("AAC4BHop");
    }

    @Override
    public void onMotion() {
    }
}

