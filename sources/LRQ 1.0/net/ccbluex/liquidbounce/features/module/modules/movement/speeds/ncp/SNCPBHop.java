/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.ranges.RangesKt
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.potion.IPotionEffect;
import net.ccbluex.liquidbounce.api.minecraft.potion.PotionType;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.api.minecraft.util.IMovementInput;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;

public final class SNCPBHop
extends SpeedMode {
    private int level = 1;
    private double moveSpeed = 0.2873;
    private double lastDist;
    private int timerDelay;

    @Override
    public void onEnable() {
        MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
        this.lastDist = 0.0;
        this.moveSpeed = 0.0;
        this.level = 4;
    }

    @Override
    public void onDisable() {
        MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
        this.moveSpeed = this.getBaseMoveSpeed();
        this.level = 0;
    }

    @Override
    public void onMotion() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        double d = iEntityPlayerSP.getPosX();
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        double xDist = d - iEntityPlayerSP2.getPrevPosX();
        IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP3 == null) {
            Intrinsics.throwNpe();
        }
        double d2 = iEntityPlayerSP3.getPosZ();
        IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP4 == null) {
            Intrinsics.throwNpe();
        }
        double zDist = d2 - iEntityPlayerSP4.getPrevPosZ();
        this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onMove(MoveEvent event) {
        block47: {
            block55: {
                block54: {
                    block51: {
                        block53: {
                            block52: {
                                block50: {
                                    block49: {
                                        block48: {
                                            block45: {
                                                block46: {
                                                    SNCPBHop sNCPBHop = this;
                                                    ++sNCPBHop.timerDelay;
                                                    int cfr_ignored_0 = sNCPBHop.timerDelay;
                                                    this.timerDelay %= 5;
                                                    if (this.timerDelay != 0) {
                                                        MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
                                                    } else {
                                                        if (MovementUtils.isMoving()) {
                                                            MinecraftInstance.mc.getTimer().setTimerSpeed(32767.0f);
                                                        }
                                                        if (MovementUtils.isMoving()) {
                                                            MinecraftInstance.mc.getTimer().setTimerSpeed(1.3f);
                                                            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                                                            if (iEntityPlayerSP == null) {
                                                                Intrinsics.throwNpe();
                                                            }
                                                            iEntityPlayerSP.setMotionX(iEntityPlayerSP.getMotionX() * (double)1.02f);
                                                            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                                                            if (iEntityPlayerSP2 == null) {
                                                                Intrinsics.throwNpe();
                                                            }
                                                            iEntityPlayerSP2.setMotionZ(iEntityPlayerSP2.getMotionZ() * (double)1.02f);
                                                        }
                                                    }
                                                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                                                    if (iEntityPlayerSP == null) {
                                                        Intrinsics.throwNpe();
                                                    }
                                                    if (iEntityPlayerSP.getOnGround() && MovementUtils.isMoving()) {
                                                        this.level = 2;
                                                    }
                                                    IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                                                    if (iEntityPlayerSP3 == null) {
                                                        Intrinsics.throwNpe();
                                                    }
                                                    double d = iEntityPlayerSP3.getPosY();
                                                    IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                                                    if (iEntityPlayerSP4 == null) {
                                                        Intrinsics.throwNpe();
                                                    }
                                                    if (this.round(d - (double)((int)iEntityPlayerSP4.getPosY())) == this.round(0.138)) {
                                                        IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
                                                        if (iEntityPlayerSP5 == null) {
                                                            Intrinsics.throwNpe();
                                                        }
                                                        iEntityPlayerSP5.setMotionY(iEntityPlayerSP5.getMotionY() - 0.08);
                                                        event.setY(event.getY() - 0.09316090325960147);
                                                        IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
                                                        if (iEntityPlayerSP6 == null) {
                                                            Intrinsics.throwNpe();
                                                        }
                                                        iEntityPlayerSP6.setPosY(iEntityPlayerSP6.getPosY() - 0.09316090325960147);
                                                    }
                                                    if (this.level != 1) break block45;
                                                    IEntityPlayerSP iEntityPlayerSP7 = MinecraftInstance.mc.getThePlayer();
                                                    if (iEntityPlayerSP7 == null) {
                                                        Intrinsics.throwNpe();
                                                    }
                                                    if (iEntityPlayerSP7.getMoveForward() != 0.0f) break block46;
                                                    IEntityPlayerSP iEntityPlayerSP8 = MinecraftInstance.mc.getThePlayer();
                                                    if (iEntityPlayerSP8 == null) {
                                                        Intrinsics.throwNpe();
                                                    }
                                                    if (iEntityPlayerSP8.getMoveStrafing() == 0.0f) break block45;
                                                }
                                                this.level = 2;
                                                this.moveSpeed = 1.35 * this.getBaseMoveSpeed() - 0.01;
                                                break block47;
                                            }
                                            if (this.level != 2) break block48;
                                            this.level = 3;
                                            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                                            if (iEntityPlayerSP == null) {
                                                Intrinsics.throwNpe();
                                            }
                                            iEntityPlayerSP.setMotionY(0.3994f);
                                            event.setY(0.3994f);
                                            this.moveSpeed *= 2.149;
                                            break block47;
                                        }
                                        if (this.level != 3) break block49;
                                        this.level = 4;
                                        double difference = 0.66 * (this.lastDist - this.getBaseMoveSpeed());
                                        this.moveSpeed = this.lastDist - difference;
                                        break block47;
                                    }
                                    if (this.level != 88) break block50;
                                    this.moveSpeed = this.getBaseMoveSpeed();
                                    this.lastDist = 0.0;
                                    this.level = 89;
                                    break block47;
                                }
                                if (this.level != 89) break block51;
                                IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                                if (iWorldClient == null) {
                                    Intrinsics.throwNpe();
                                }
                                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                                if (iEntityPlayerSP == null) {
                                    Intrinsics.throwNpe();
                                }
                                IEntity iEntity = iEntityPlayerSP;
                                IEntityPlayerSP iEntityPlayerSP9 = MinecraftInstance.mc.getThePlayer();
                                if (iEntityPlayerSP9 == null) {
                                    Intrinsics.throwNpe();
                                }
                                IAxisAlignedBB iAxisAlignedBB = iEntityPlayerSP9.getEntityBoundingBox();
                                IEntityPlayerSP iEntityPlayerSP10 = MinecraftInstance.mc.getThePlayer();
                                if (iEntityPlayerSP10 == null) {
                                    Intrinsics.throwNpe();
                                }
                                Collection<IAxisAlignedBB> difference = iWorldClient.getCollidingBoundingBoxes(iEntity, iAxisAlignedBB.offset(0.0, iEntityPlayerSP10.getMotionY(), 0.0));
                                boolean bl = false;
                                if (!difference.isEmpty()) break block52;
                                IEntityPlayerSP iEntityPlayerSP11 = MinecraftInstance.mc.getThePlayer();
                                if (iEntityPlayerSP11 == null) {
                                    Intrinsics.throwNpe();
                                }
                                if (!iEntityPlayerSP11.isCollidedVertically()) break block53;
                            }
                            this.level = 1;
                        }
                        this.lastDist = 0.0;
                        this.moveSpeed = this.getBaseMoveSpeed();
                        return;
                    }
                    IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                    if (iWorldClient == null) {
                        Intrinsics.throwNpe();
                    }
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    IEntity iEntity = iEntityPlayerSP;
                    IEntityPlayerSP iEntityPlayerSP12 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP12 == null) {
                        Intrinsics.throwNpe();
                    }
                    IAxisAlignedBB iAxisAlignedBB = iEntityPlayerSP12.getEntityBoundingBox();
                    IEntityPlayerSP iEntityPlayerSP13 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP13 == null) {
                        Intrinsics.throwNpe();
                    }
                    Collection<IAxisAlignedBB> difference = iWorldClient.getCollidingBoundingBoxes(iEntity, iAxisAlignedBB.offset(0.0, iEntityPlayerSP13.getMotionY(), 0.0));
                    boolean bl = false;
                    if (!difference.isEmpty()) break block54;
                    IEntityPlayerSP iEntityPlayerSP14 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP14 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!iEntityPlayerSP14.isCollidedVertically()) break block55;
                }
                this.moveSpeed = this.getBaseMoveSpeed();
                this.lastDist = 0.0;
                this.level = 88;
                return;
            }
            this.moveSpeed = this.lastDist - this.lastDist / 159.0;
        }
        this.moveSpeed = RangesKt.coerceAtLeast((double)this.moveSpeed, (double)this.getBaseMoveSpeed());
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        IMovementInput movementInput = iEntityPlayerSP.getMovementInput();
        float forward = movementInput.getMoveForward();
        float strafe = movementInput.getMoveStrafe();
        IEntityPlayerSP iEntityPlayerSP15 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP15 == null) {
            Intrinsics.throwNpe();
        }
        float yaw = iEntityPlayerSP15.getRotationYaw();
        if (forward == 0.0f && strafe == 0.0f) {
            event.setX(0.0);
            event.setZ(0.0);
        } else if (forward != 0.0f) {
            if (strafe >= 1.0f) {
                yaw += (float)(forward > 0.0f ? -45 : 45);
                strafe = 0.0f;
            } else if (strafe <= -1.0f) {
                yaw += (float)(forward > 0.0f ? 45 : -45);
                strafe = 0.0f;
            }
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        double mx2 = Math.cos(Math.toRadians((double)yaw + (double)90.0f));
        double mz2 = Math.sin(Math.toRadians((double)yaw + (double)90.0f));
        event.setX((double)forward * this.moveSpeed * mx2 + (double)strafe * this.moveSpeed * mz2);
        event.setZ((double)forward * this.moveSpeed * mz2 - (double)strafe * this.moveSpeed * mx2);
        IEntityPlayerSP iEntityPlayerSP16 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP16 == null) {
            Intrinsics.throwNpe();
        }
        iEntityPlayerSP16.setStepHeight(0.6f);
        if (forward == 0.0f && strafe == 0.0f) {
            event.setX(0.0);
            event.setZ(0.0);
        }
    }

    private final double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED))) {
            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            IPotionEffect iPotionEffect = iEntityPlayerSP2.getActivePotionEffect(MinecraftInstance.classProvider.getPotionEnum(PotionType.MOVE_SPEED));
            if (iPotionEffect == null) {
                Intrinsics.throwNpe();
            }
            baseSpeed *= 1.0 + 0.2 * (double)(iPotionEffect.getAmplifier() + 1);
        }
        return baseSpeed;
    }

    private final double round(double value) {
        BigDecimal bigDecimal = new BigDecimal(value);
        bigDecimal = bigDecimal.setScale(3, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    public SNCPBHop() {
        super("SNCPBHop");
    }
}

