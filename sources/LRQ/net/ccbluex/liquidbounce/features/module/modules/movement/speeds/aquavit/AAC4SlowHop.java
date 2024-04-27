/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aquavit;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;

public final class AAC4SlowHop
extends SpeedMode {
    @Override
    public void onDisable() {
        MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        iEntityPlayerSP.setSpeedInAir(0.02f);
    }

    @Override
    public void onTick() {
    }

    @Override
    public void onMotion() {
    }

    @Override
    public void onUpdate() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.isInWater()) {
            return;
        }
        if (MovementUtils.isMoving()) {
            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP2.getOnGround()) {
                MinecraftInstance.mc.getGameSettings().getKeyBindJump().setPressed(false);
                IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP3 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP3.jump();
            }
            IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP4 == null) {
                Intrinsics.throwNpe();
            }
            if (!iEntityPlayerSP4.getOnGround()) {
                IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP5 == null) {
                    Intrinsics.throwNpe();
                }
                if ((double)iEntityPlayerSP5.getFallDistance() <= 0.1) {
                    IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP6 == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP6.setSpeedInAir(0.02f);
                    MinecraftInstance.mc.getTimer().setTimerSpeed(1.4f);
                }
            }
            IEntityPlayerSP iEntityPlayerSP7 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP7 == null) {
                Intrinsics.throwNpe();
            }
            if ((double)iEntityPlayerSP7.getFallDistance() > 0.1) {
                IEntityPlayerSP iEntityPlayerSP8 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP8 == null) {
                    Intrinsics.throwNpe();
                }
                if ((double)iEntityPlayerSP8.getFallDistance() < 1.3) {
                    IEntityPlayerSP iEntityPlayerSP9 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP9 == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP9.setSpeedInAir(0.0205f);
                    MinecraftInstance.mc.getTimer().setTimerSpeed(0.65f);
                }
            }
            IEntityPlayerSP iEntityPlayerSP10 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP10 == null) {
                Intrinsics.throwNpe();
            }
            if ((double)iEntityPlayerSP10.getFallDistance() >= 1.3) {
                MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
                IEntityPlayerSP iEntityPlayerSP11 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP11 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP11.setSpeedInAir(0.02f);
            }
        } else {
            IEntityPlayerSP iEntityPlayerSP12 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP12 == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP12.setMotionX(0.0);
            IEntityPlayerSP iEntityPlayerSP13 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP13 == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP13.setMotionZ(0.0);
        }
    }

    @Override
    public void onMove(MoveEvent event) {
    }

    @Override
    public void onEnable() {
    }

    public AAC4SlowHop() {
        super("AAC4SlowHop");
    }
}

