/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;

public final class CustomSpeed
extends SpeedMode {
    @Override
    public void onMotion() {
        if (MovementUtils.isMoving()) {
            Speed speed = (Speed)LiquidBounce.INSTANCE.getModuleManager().getModule(Speed.class);
            if (speed == null) {
                return;
            }
            Speed speed2 = speed;
            MinecraftInstance.mc.getTimer().setTimerSpeed(((Number)speed2.getCustomTimerValue().get()).floatValue());
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP.getOnGround()) {
                MovementUtils.strafe(((Number)speed2.getCustomSpeedValue().get()).floatValue());
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP2.setMotionY(((Number)speed2.getCustomYValue().get()).floatValue());
            } else if (((Boolean)speed2.getCustomStrafeValue().get()).booleanValue()) {
                MovementUtils.strafe(((Number)speed2.getCustomSpeedValue().get()).floatValue());
            } else {
                MovementUtils.strafe$default(0.0f, 1, null);
            }
        } else {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP.setMotionZ(0.0);
            IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP3 == null) {
                Intrinsics.throwNpe();
            }
            IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP4 == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP3.setMotionX(iEntityPlayerSP4.getMotionZ());
        }
    }

    @Override
    public void onEnable() {
        Speed speed = (Speed)LiquidBounce.INSTANCE.getModuleManager().getModule(Speed.class);
        if (speed == null) {
            return;
        }
        Speed speed2 = speed;
        if (((Boolean)speed2.getResetXZValue().get()).booleanValue()) {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP.setMotionZ(0.0);
            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP3 == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP2.setMotionX(iEntityPlayerSP3.getMotionZ());
        }
        if (((Boolean)speed2.getResetYValue().get()).booleanValue()) {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP.setMotionY(0.0);
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
        super.onDisable();
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onMove(MoveEvent event) {
    }

    public CustomSpeed() {
        super("Custom");
    }
}

