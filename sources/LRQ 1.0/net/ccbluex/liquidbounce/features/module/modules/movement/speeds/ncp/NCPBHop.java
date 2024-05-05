/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import kotlin.jvm.internal.Intrinsics;
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

public final class NCPBHop
extends SpeedMode {
    private int level = 1;
    private double moveSpeed = 0.2873;
    private double lastDist;
    private int timerDelay;

    /*
     * Unable to fully structure code
     */
    @Override
    public void onEnable() {
        MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
        v0 = MinecraftInstance.mc.getTheWorld();
        if (v0 == null) {
            Intrinsics.throwNpe();
        }
        v1 = MinecraftInstance.mc.getThePlayer();
        if (v1 == null) {
            Intrinsics.throwNpe();
        }
        v2 = v1;
        v3 = MinecraftInstance.mc.getThePlayer();
        if (v3 == null) {
            Intrinsics.throwNpe();
        }
        v4 = v3.getEntityBoundingBox();
        v5 = MinecraftInstance.mc.getThePlayer();
        if (v5 == null) {
            Intrinsics.throwNpe();
        }
        if (v0.getCollidingBoundingBoxes(v2, v4.offset(0.0, v5.getMotionY(), 0.0)).size() > 0) ** GOTO lbl-1000
        v6 = MinecraftInstance.mc.getThePlayer();
        if (v6 == null) {
            Intrinsics.throwNpe();
        }
        if (v6.isCollidedVertically()) lbl-1000:
        // 2 sources

        {
            v7 = 1;
        } else {
            v7 = 4;
        }
        this.level = v7;
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
        double d;
        block38: {
            block42: {
                block41: {
                    block40: {
                        block39: {
                            block36: {
                                block37: {
                                    NCPBHop nCPBHop = this;
                                    ++nCPBHop.timerDelay;
                                    int cfr_ignored_0 = nCPBHop.timerDelay;
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
                                    double d2 = iEntityPlayerSP3.getPosY();
                                    IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                                    if (iEntityPlayerSP4 == null) {
                                        Intrinsics.throwNpe();
                                    }
                                    if (this.round(d2 - (double)((int)iEntityPlayerSP4.getPosY())) == this.round(0.138)) {
                                        IEntityPlayerSP thePlayer;
                                        IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
                                        if (iEntityPlayerSP5 == null) {
                                            Intrinsics.throwNpe();
                                        }
                                        IEntityPlayerSP iEntityPlayerSP6 = thePlayer = iEntityPlayerSP5;
                                        iEntityPlayerSP6.setMotionY(iEntityPlayerSP6.getMotionY() - 0.08);
                                        event.setY(event.getY() - 0.09316090325960147);
                                        IEntityPlayerSP iEntityPlayerSP7 = thePlayer;
                                        iEntityPlayerSP7.setPosY(iEntityPlayerSP7.getPosY() - 0.09316090325960147);
                                    }
                                    if (this.level != 1) break block36;
                                    IEntityPlayerSP iEntityPlayerSP8 = MinecraftInstance.mc.getThePlayer();
                                    if (iEntityPlayerSP8 == null) {
                                        Intrinsics.throwNpe();
                                    }
                                    if (iEntityPlayerSP8.getMoveForward() != 0.0f) break block37;
                                    IEntityPlayerSP iEntityPlayerSP9 = MinecraftInstance.mc.getThePlayer();
                                    if (iEntityPlayerSP9 == null) {
                                        Intrinsics.throwNpe();
                                    }
                                    if (iEntityPlayerSP9.getMoveStrafing() == 0.0f) break block36;
                                }
                                this.level = 2;
                                this.moveSpeed = 1.35 * this.getBaseMoveSpeed() - 0.01;
                                break block38;
                            }
                            if (this.level != 2) break block39;
                            this.level = 3;
                            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                            if (iEntityPlayerSP == null) {
                                Intrinsics.throwNpe();
                            }
                            iEntityPlayerSP.setMotionY(0.3994f);
                            event.setY(0.3994f);
                            this.moveSpeed *= 2.149;
                            break block38;
                        }
                        if (this.level != 3) break block40;
                        this.level = 4;
                        double difference = 0.66 * (this.lastDist - this.getBaseMoveSpeed());
                        this.moveSpeed = this.lastDist - difference;
                        break block38;
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
                    IEntityPlayerSP iEntityPlayerSP10 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP10 == null) {
                        Intrinsics.throwNpe();
                    }
                    IAxisAlignedBB iAxisAlignedBB = iEntityPlayerSP10.getEntityBoundingBox();
                    IEntityPlayerSP iEntityPlayerSP11 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP11 == null) {
                        Intrinsics.throwNpe();
                    }
                    Collection<IAxisAlignedBB> difference = iWorldClient.getCollidingBoundingBoxes(iEntity, iAxisAlignedBB.offset(0.0, iEntityPlayerSP11.getMotionY(), 0.0));
                    boolean bl = false;
                    if (!difference.isEmpty()) break block41;
                    IEntityPlayerSP iEntityPlayerSP12 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP12 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!iEntityPlayerSP12.isCollidedVertically()) break block42;
                }
                this.level = 1;
            }
            this.moveSpeed = this.lastDist - this.lastDist / 159.0;
        }
        double difference = this.moveSpeed;
        double d3 = this.getBaseMoveSpeed();
        NCPBHop nCPBHop = this;
        boolean bl = false;
        nCPBHop.moveSpeed = d = Math.max(difference, d3);
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        IMovementInput movementInput = iEntityPlayerSP.getMovementInput();
        float forward = movementInput.getMoveForward();
        float strafe = movementInput.getMoveStrafe();
        IEntityPlayerSP iEntityPlayerSP13 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP13 == null) {
            Intrinsics.throwNpe();
        }
        float yaw = iEntityPlayerSP13.getRotationYaw();
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
        double d4 = Math.toRadians((double)yaw + (double)90.0f);
        boolean bl2 = false;
        double mx2 = Math.cos(d4);
        double d5 = Math.toRadians((double)yaw + (double)90.0f);
        boolean bl3 = false;
        double mz2 = Math.sin(d5);
        event.setX((double)forward * this.moveSpeed * mx2 + (double)strafe * this.moveSpeed * mz2);
        event.setZ((double)forward * this.moveSpeed * mz2 - (double)strafe * this.moveSpeed * mx2);
        IEntityPlayerSP iEntityPlayerSP14 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP14 == null) {
            Intrinsics.throwNpe();
        }
        iEntityPlayerSP14.setStepHeight(0.6f);
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
            baseSpeed *= 1.0 + 0.2 * (double)iPotionEffect.getAmplifier() + 1.0;
        }
        return baseSpeed;
    }

    private final double round(double value) {
        BigDecimal bigDecimal = new BigDecimal(value);
        bigDecimal = bigDecimal.setScale(3, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    public NCPBHop() {
        super("NCPBHop");
    }
}

