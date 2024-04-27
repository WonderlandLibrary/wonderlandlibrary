/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.potion.IPotionEffect;
import net.ccbluex.liquidbounce.api.minecraft.potion.PotionType;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;

public final class HypixelHop
extends SpeedMode {
    @Override
    public void onMotion() {
        if (MovementUtils.isMoving()) {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP.getOnGround()) {
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP2.jump();
                float speed = MovementUtils.INSTANCE.getSpeed() < 0.56f ? MovementUtils.INSTANCE.getSpeed() * 1.045f : 0.56f;
                IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP3 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP3.getOnGround()) {
                    IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP4 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP4.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED))) {
                        IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP5 == null) {
                            Intrinsics.throwNpe();
                        }
                        IPotionEffect iPotionEffect = iEntityPlayerSP5.getActivePotionEffect(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED));
                        if (iPotionEffect == null) {
                            Intrinsics.throwNpe();
                        }
                        speed *= 1.0f + 0.13f * (float)(1 + iPotionEffect.getAmplifier());
                    }
                }
                MovementUtils.strafe(speed);
                return;
            }
            IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP6 == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP6.getMotionY() < 0.2) {
                IEntityPlayerSP iEntityPlayerSP7 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP7 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP7.setMotionY(iEntityPlayerSP7.getMotionY() - 0.02);
            }
            MovementUtils.strafe(MovementUtils.INSTANCE.getSpeed() * 1.01889f);
        } else {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP.setMotionZ(0.0);
            IEntityPlayerSP iEntityPlayerSP8 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP8 == null) {
                Intrinsics.throwNpe();
            }
            IEntityPlayerSP iEntityPlayerSP9 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP9 == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP8.setMotionX(iEntityPlayerSP9.getMotionZ());
        }
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onMove(MoveEvent event) {
    }

    public HypixelHop() {
        super("HypixelHop");
    }
}

