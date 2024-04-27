/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.enums.MaterialType;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.potion.IPotionEffect;
import net.ccbluex.liquidbounce.api.minecraft.potion.PotionType;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;

public final class YPort
extends SpeedMode {
    private double moveSpeed = 0.2873;
    private int level = 1;
    private double lastDist;
    private int timerDelay;
    private boolean safeJump;

    @Override
    public void onMotion() {
        double d;
        block22: {
            block23: {
                if (this.safeJump || MinecraftInstance.mc.getGameSettings().getKeyBindJump().isKeyDown()) break block22;
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP.isOnLadder()) break block22;
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP2.isInsideOfMaterial(MinecraftInstance.classProvider.getMaterialEnum(MaterialType.WATER))) break block22;
                IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP3 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP3.isInsideOfMaterial(MinecraftInstance.classProvider.getMaterialEnum(MaterialType.LAVA))) break block22;
                IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP4 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP4.isInWater()) break block22;
                if (!MinecraftInstance.classProvider.isBlockAir(this.getBlock(-1.1)) && !MinecraftInstance.classProvider.isBlockAir(this.getBlock(-1.1))) break block23;
                if (MinecraftInstance.classProvider.isBlockAir(this.getBlock(-0.1))) break block22;
                IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP5 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP5.getMotionX() == 0.0) break block22;
                IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP6 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP6.getMotionZ() == 0.0) break block22;
                IEntityPlayerSP iEntityPlayerSP7 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP7 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP7.getOnGround()) break block22;
                IEntityPlayerSP iEntityPlayerSP8 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP8 == null) {
                    Intrinsics.throwNpe();
                }
                if (!(iEntityPlayerSP8.getFallDistance() < 3.0f)) break block22;
                IEntityPlayerSP iEntityPlayerSP9 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP9 == null) {
                    Intrinsics.throwNpe();
                }
                if (!((double)iEntityPlayerSP9.getFallDistance() > 0.05)) break block22;
            }
            if (this.level == 3) {
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP.setMotionY(-0.3994);
            }
        }
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        double d2 = iEntityPlayerSP.getPosX();
        IEntityPlayerSP iEntityPlayerSP10 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP10 == null) {
            Intrinsics.throwNpe();
        }
        double xDist = d2 - iEntityPlayerSP10.getPrevPosX();
        IEntityPlayerSP iEntityPlayerSP11 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP11 == null) {
            Intrinsics.throwNpe();
        }
        double d3 = iEntityPlayerSP11.getPosZ();
        IEntityPlayerSP iEntityPlayerSP12 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP12 == null) {
            Intrinsics.throwNpe();
        }
        double zDist = d3 - iEntityPlayerSP12.getPrevPosZ();
        double d4 = xDist * xDist + zDist * zDist;
        YPort yPort = this;
        boolean bl = false;
        yPort.lastDist = d = Math.sqrt(d4);
        if (!MovementUtils.isMoving()) {
            this.safeJump = true;
        } else {
            IEntityPlayerSP iEntityPlayerSP13 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP13 == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP13.getOnGround()) {
                this.safeJump = false;
            }
        }
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onMove(MoveEvent event) {
        block40: {
            block44: {
                block43: {
                    block42: {
                        block41: {
                            block38: {
                                block39: {
                                    ++this.timerDelay;
                                    this.timerDelay %= 5;
                                    if (this.timerDelay != 0) {
                                        MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
                                    } else {
                                        if (MovementUtils.INSTANCE.hasMotion()) {
                                            MinecraftInstance.mc.getTimer().setTimerSpeed(32767.0f);
                                        }
                                        if (MovementUtils.INSTANCE.hasMotion()) {
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
                                    if (iEntityPlayerSP.getOnGround() && MovementUtils.INSTANCE.hasMotion()) {
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
                                    if (this.level != 1) break block38;
                                    IEntityPlayerSP iEntityPlayerSP7 = MinecraftInstance.mc.getThePlayer();
                                    if (iEntityPlayerSP7 == null) {
                                        Intrinsics.throwNpe();
                                    }
                                    if (iEntityPlayerSP7.getMoveForward() != 0.0f) break block39;
                                    IEntityPlayerSP iEntityPlayerSP8 = MinecraftInstance.mc.getThePlayer();
                                    if (iEntityPlayerSP8 == null) {
                                        Intrinsics.throwNpe();
                                    }
                                    if (iEntityPlayerSP8.getMoveStrafing() == 0.0f) break block38;
                                }
                                this.level = 2;
                                this.moveSpeed = 1.38 * this.getBaseMoveSpeed() - 0.01;
                                break block40;
                            }
                            if (this.level != 2) break block41;
                            this.level = 3;
                            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                            if (iEntityPlayerSP == null) {
                                Intrinsics.throwNpe();
                            }
                            iEntityPlayerSP.setMotionY(0.3994f);
                            event.setY(0.3994f);
                            this.moveSpeed *= 2.149;
                            break block40;
                        }
                        if (this.level != 3) break block42;
                        this.level = 4;
                        double difference = 0.66 * (this.lastDist - this.getBaseMoveSpeed());
                        this.moveSpeed = this.lastDist - difference;
                        break block40;
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
                    IEntityPlayerSP iEntityPlayerSP9 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP9 == null) {
                        Intrinsics.throwNpe();
                    }
                    IAxisAlignedBB iAxisAlignedBB = iEntityPlayerSP9.getEntityBoundingBox();
                    IEntityPlayerSP iEntityPlayerSP10 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP10 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iWorldClient.getCollidingBoundingBoxes(iEntity, iAxisAlignedBB.offset(0.0, iEntityPlayerSP10.getMotionY(), 0.0)).size() > 0) break block43;
                    IEntityPlayerSP iEntityPlayerSP11 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP11 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!iEntityPlayerSP11.isCollidedVertically()) break block44;
                }
                this.level = 1;
            }
            this.moveSpeed = this.lastDist - this.lastDist / 159.0;
        }
        this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed());
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        float forward = iEntityPlayerSP.getMovementInput().getMoveForward();
        IEntityPlayerSP iEntityPlayerSP12 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP12 == null) {
            Intrinsics.throwNpe();
        }
        float strafe = iEntityPlayerSP12.getMovementInput().getMoveStrafe();
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
        double mx = Math.cos(Math.toRadians((double)yaw + (double)90.0f));
        double mz = Math.sin(Math.toRadians((double)yaw + (double)90.0f));
        event.setX((double)forward * this.moveSpeed * mx + (double)strafe * this.moveSpeed * mz);
        event.setZ((double)forward * this.moveSpeed * mz - (double)strafe * this.moveSpeed * mx);
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
            int amplifier = iPotionEffect.getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

    /*
     * WARNING - void declaration
     */
    private final IBlock getBlock(IAxisAlignedBB axisAlignedBB) {
        double d = axisAlignedBB.getMinX();
        boolean bl = false;
        int n = (int)Math.floor(d);
        d = axisAlignedBB.getMaxX();
        bl = false;
        int n2 = (int)Math.floor(d) + 1;
        while (n < n2) {
            void x;
            double d2 = axisAlignedBB.getMinZ();
            boolean bl2 = false;
            int n3 = (int)Math.floor(d2);
            d2 = axisAlignedBB.getMaxZ();
            bl2 = false;
            int n4 = (int)Math.floor(d2) + 1;
            while (n3 < n4) {
                void z;
                IBlock block;
                IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                if (iWorldClient == null) {
                    Intrinsics.throwNpe();
                }
                if ((block = iWorldClient.getBlockState(new WBlockPos((int)x, (int)axisAlignedBB.getMinY(), (int)z)).getBlock()) != null) {
                    return block;
                }
                ++z;
            }
            ++x;
        }
        return null;
    }

    private final IBlock getBlock(double offset) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        return this.getBlock(iEntityPlayerSP.getEntityBoundingBox().offset(0.0, offset, 0.0));
    }

    private final double round(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(3, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public YPort() {
        super("YPort");
    }
}

