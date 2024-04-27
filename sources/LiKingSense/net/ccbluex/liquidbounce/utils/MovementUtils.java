/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.JvmOverloads
 *  kotlin.jvm.JvmStatic
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.utils;

import kotlin.Metadata;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J0\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\r2\u0006\u0010\u0015\u001a\u00020\r2\u0006\u0010\u0016\u001a\u00020\u00172\b\b\u0002\u0010\u0018\u001a\u00020\u0019J\u0010\u0010\u001a\u001a\u00020\u00112\u0006\u0010\u001b\u001a\u00020\u0004H\u0007J\u0006\u0010\u001c\u001a\u00020\tJ\u000e\u0010\u001d\u001a\u00020\t2\u0006\u0010\u001e\u001a\u00020\u0004J\u0012\u0010\u001f\u001a\u00020\u00112\b\b\u0002\u0010\f\u001a\u00020\rH\u0007R\u001a\u0010\u0003\u001a\u00020\u00048FX\u0087\u0004\u00a2\u0006\f\u0012\u0004\b\u0005\u0010\u0002\u001a\u0004\b\u0006\u0010\u0007R\u001a\u0010\b\u001a\u00020\t8FX\u0087\u0004\u00a2\u0006\f\u0012\u0004\b\n\u0010\u0002\u001a\u0004\b\b\u0010\u000bR\u0011\u0010\f\u001a\u00020\r8F\u00a2\u0006\u0006\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006 "}, d2={"Lnet/ccbluex/liquidbounce/utils/MovementUtils;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "()V", "direction", "", "direction$annotations", "getDirection", "()D", "isMoving", "", "isMoving$annotations", "()Z", "speed", "", "getSpeed", "()F", "doTargetStrafe", "", "curTarget", "Lnet/minecraft/entity/EntityLivingBase;", "direction_", "radius", "moveEvent", "Lnet/ccbluex/liquidbounce/event/MoveEvent;", "mathRadius", "", "forward", "length", "hasMotion", "isOnGround", "height", "strafe", "LiKingSense"})
public final class MovementUtils
extends MinecraftInstance {
    public static final MovementUtils INSTANCE;

    public final float getSpeed() {
        double d = MinecraftInstance.mc.getThePlayer().getMotionX() * MinecraftInstance.mc.getThePlayer().getMotionX() + MinecraftInstance.mc.getThePlayer().getMotionZ() * MinecraftInstance.mc.getThePlayer().getMotionZ();
        boolean bl = false;
        return (float)Math.sqrt(d);
    }

    @JvmStatic
    public static /* synthetic */ void isMoving$annotations() {
    }

    public static final boolean isMoving() {
        return MinecraftInstance.mc.getThePlayer() != null && (MinecraftInstance.mc.getThePlayer().getMovementInput().getMoveForward() != 0.0f || MinecraftInstance.mc.getThePlayer().getMovementInput().getMoveStrafe() != 0.0f);
    }

    public final boolean hasMotion() {
        return MinecraftInstance.mc.getThePlayer().getMotionX() != 0.0 && MinecraftInstance.mc.getThePlayer().getMotionZ() != 0.0 && MinecraftInstance.mc.getThePlayer().getMotionY() != 0.0;
    }

    public final void doTargetStrafe(@NotNull EntityLivingBase curTarget, float direction_, float radius, @NotNull MoveEvent moveEvent, int mathRadius) {
        Intrinsics.checkParameterIsNotNull((Object)curTarget, (String)"curTarget");
        Intrinsics.checkParameterIsNotNull((Object)moveEvent, (String)"moveEvent");
        if (!MovementUtils.isMoving()) {
            return;
        }
        double forward_ = 0.0;
        double strafe_ = 0.0;
        double d = moveEvent.getX() * moveEvent.getX() + moveEvent.getZ() * moveEvent.getZ();
        boolean bl = false;
        double speed_ = Math.sqrt(d);
        if (speed_ <= 1.0E-4) {
            return;
        }
        double _direction = 0.0;
        if ((double)direction_ > 0.001) {
            _direction = 1.0;
        } else if ((double)direction_ < -0.001) {
            _direction = -1.0;
        }
        float curDistance = (float)0.01;
        if (mathRadius == 1) {
            curDistance = MinecraftInstance.mc2.field_71439_g.func_70032_d((Entity)curTarget);
        } else if (mathRadius == 0) {
            double d2 = (MinecraftInstance.mc.getThePlayer().getPosX() - curTarget.field_70165_t) * (MinecraftInstance.mc.getThePlayer().getPosX() - curTarget.field_70165_t) + (MinecraftInstance.mc.getThePlayer().getPosZ() - curTarget.field_70161_v) * (MinecraftInstance.mc.getThePlayer().getPosZ() - curTarget.field_70161_v);
            boolean bl2 = false;
            curDistance = (float)Math.sqrt(d2);
        }
        forward_ = (double)curDistance < (double)radius - speed_ ? -1.0 : ((double)curDistance > (double)radius + speed_ ? 1.0 : (double)(curDistance - radius) / speed_);
        if ((double)curDistance < (double)radius + speed_ * (double)2 && (double)curDistance > (double)radius - speed_ * (double)2) {
            strafe_ = 1.0;
        }
        double strafeYaw = RotationUtils.getRotationsEntity(curTarget).getYaw();
        double d3 = forward_ * forward_ + (strafe_ *= _direction) * strafe_;
        boolean bl3 = false;
        double covert_ = Math.sqrt(d3);
        forward_ /= covert_;
        strafe_ /= covert_;
        bl3 = false;
        double turnAngle = Math.toDegrees(Math.asin(strafe_));
        if (turnAngle > 0.0) {
            if (forward_ < 0.0) {
                turnAngle = (double)180.0f - turnAngle;
            }
        } else if (forward_ < 0.0) {
            turnAngle = (double)-180.0f - turnAngle;
        }
        strafeYaw = Math.toRadians(strafeYaw + turnAngle);
        MoveEvent moveEvent2 = moveEvent;
        bl3 = false;
        double d4 = Math.sin(strafeYaw);
        moveEvent2.setX(-d4 * speed_);
        moveEvent2 = moveEvent;
        bl3 = false;
        d4 = Math.cos(strafeYaw);
        moveEvent2.setZ(d4 * speed_);
        MinecraftInstance.mc.getThePlayer().setMotionX(moveEvent.getX());
        MinecraftInstance.mc.getThePlayer().setMotionZ(moveEvent.getZ());
    }

    public static /* synthetic */ void doTargetStrafe$default(MovementUtils movementUtils, EntityLivingBase entityLivingBase, float f, float f2, MoveEvent moveEvent, int n, int n2, Object object) {
        if ((n2 & 0x10) != 0) {
            n = 0;
        }
        movementUtils.doTargetStrafe(entityLivingBase, f, f2, moveEvent, n);
    }

    public final boolean isOnGround(double height) {
        return !MinecraftInstance.mc.getTheWorld().getCollidingBoundingBoxes(MinecraftInstance.mc.getThePlayer(), MinecraftInstance.mc.getThePlayer().getEntityBoundingBox().offset(0.0, -height, 0.0)).isEmpty();
    }

    @JvmStatic
    @JvmOverloads
    public static final void strafe(float speed) {
        IEntityPlayerSP thePlayer;
        if (!MovementUtils.isMoving()) {
            return;
        }
        double yaw = MovementUtils.getDirection();
        IEntityPlayerSP iEntityPlayerSP = thePlayer = MinecraftInstance.mc.getThePlayer();
        boolean bl = false;
        double d = Math.sin(yaw);
        iEntityPlayerSP.setMotionX(-d * (double)speed);
        iEntityPlayerSP = thePlayer;
        bl = false;
        d = Math.cos(yaw);
        iEntityPlayerSP.setMotionZ(d * (double)speed);
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
        IEntityPlayerSP thePlayer = MinecraftInstance.mc.getThePlayer();
        double yaw = Math.toRadians(thePlayer.getRotationYaw());
        double d = thePlayer.getPosX();
        IEntityPlayerSP iEntityPlayerSP = thePlayer;
        boolean bl = false;
        double d2 = Math.sin(yaw);
        double d3 = d + -d2 * length;
        double d4 = thePlayer.getPosZ();
        d2 = thePlayer.getPosY();
        d = d3;
        bl = false;
        double d5 = Math.cos(yaw);
        iEntityPlayerSP.setPosition(d, d2, d4 + d5 * length);
    }

    @JvmStatic
    public static /* synthetic */ void direction$annotations() {
    }

    public static final double getDirection() {
        IEntityPlayerSP thePlayer = MinecraftInstance.mc.getThePlayer();
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

