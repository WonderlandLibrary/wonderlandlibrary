/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.JvmOverloads
 *  kotlin.jvm.JvmStatic
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.utils;

import java.math.BigDecimal;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;

public final class MovementUtils
extends MinecraftInstance {
    public static final MovementUtils INSTANCE;

    public final float getSpeed() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        double d = iEntityPlayerSP.getMotionX();
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        double d2 = d * iEntityPlayerSP2.getMotionX();
        IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP3 == null) {
            Intrinsics.throwNpe();
        }
        double d3 = iEntityPlayerSP3.getMotionZ();
        IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP4 == null) {
            Intrinsics.throwNpe();
        }
        double d4 = d2 + d3 * iEntityPlayerSP4.getMotionZ();
        boolean bl = false;
        return (float)Math.sqrt(d4);
    }

    @JvmStatic
    public static /* synthetic */ void isMoving$annotations() {
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static final boolean isMoving() {
        if (MinecraftInstance.mc.getThePlayer() == null) return false;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.getMovementInput().getMoveForward() != 0.0f) return true;
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP2.getMovementInput().getMoveStrafe() == 0.0f) return false;
        return true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public final boolean hasMotion() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.getMotionX() == 0.0) return false;
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP2.getMotionZ() == 0.0) return false;
        IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP3 == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP3.getMotionY() == 0.0) return false;
        return true;
    }

    public final double getBlockSpeed(IEntityLivingBase entityIn) {
        return BigDecimal.valueOf(Math.sqrt(Math.pow(entityIn.getPosX() - entityIn.getPrevPosX(), 2.0) + Math.pow(entityIn.getPosZ() - entityIn.getPrevPosZ(), 2.0)) * (double)20).setScale(1, 4).doubleValue();
    }

    public final boolean isOnGround(double height) {
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        IEntity iEntity = iEntityPlayerSP;
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        return !iWorldClient.getCollidingBoundingBoxes(iEntity, iEntityPlayerSP2.getEntityBoundingBox().offset(0.0, -height, 0.0)).isEmpty();
    }

    @JvmStatic
    @JvmOverloads
    public static final void strafe(float speed) {
        IEntityPlayerSP thePlayer;
        if (!MovementUtils.isMoving()) {
            return;
        }
        double yaw = MovementUtils.getDirection();
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        IEntityPlayerSP iEntityPlayerSP2 = thePlayer = iEntityPlayerSP;
        boolean bl = false;
        double d = Math.sin(yaw);
        iEntityPlayerSP2.setMotionX(-d * (double)speed);
        iEntityPlayerSP2 = thePlayer;
        bl = false;
        d = Math.cos(yaw);
        iEntityPlayerSP2.setMotionZ(d * (double)speed);
    }

    public static /* synthetic */ void strafe$default(float f, int n, Object object) {
        if ((n & 1) != 0) {
            f = INSTANCE.getSpeed();
        }
        MovementUtils.strafe(f);
    }

    @JvmStatic
    @JvmOverloads
    public static final void strafe() {
        MovementUtils.strafe$default(0.0f, 1, null);
    }

    @JvmStatic
    public static final void forward(double length) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        double yaw = Math.toRadians(thePlayer.getRotationYaw());
        double d = thePlayer.getPosX();
        IEntityPlayerSP iEntityPlayerSP2 = thePlayer;
        boolean bl = false;
        double d2 = Math.sin(yaw);
        double d3 = d + -d2 * length;
        double d4 = thePlayer.getPosZ();
        d2 = thePlayer.getPosY();
        d = d3;
        bl = false;
        double d5 = Math.cos(yaw);
        iEntityPlayerSP2.setPosition(d, d2, d4 + d5 * length);
    }

    @JvmStatic
    public static /* synthetic */ void direction$annotations() {
    }

    public static final double getDirection() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        float rotationYaw = thePlayer.getRotationYaw();
        if (thePlayer.getMoveForward() < 0.0f) {
            rotationYaw += 180.0f;
        }
        float forward = 1.0f;
        if (thePlayer.getMoveForward() < 0.0f) {
            forward = -0.5f;
        } else if (thePlayer.getMoveForward() > 0.0f) {
            forward = 0.5f;
        }
        if (thePlayer.getMoveStrafing() > 0.0f) {
            rotationYaw -= 90.0f * forward;
        }
        if (thePlayer.getMoveStrafing() < 0.0f) {
            rotationYaw += 90.0f * forward;
        }
        return Math.toRadians(rotationYaw);
    }

    private MovementUtils() {
    }

    static {
        MovementUtils movementUtils;
        INSTANCE = movementUtils = new MovementUtils();
    }
}

