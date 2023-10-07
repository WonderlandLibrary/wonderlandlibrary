/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 *  net.minecraft.util.AxisAlignedBB
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.utils;

import java.math.BigDecimal;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\n\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0011\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\r\u001a\u00020\u0004J0\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00132\u0006\u0010\u0015\u001a\u00020\u00162\b\b\u0002\u0010\u0017\u001a\u00020\u0018J\u0010\u0010\u0019\u001a\u00020\u000f2\u0006\u0010\u001a\u001a\u00020\u0004H\u0007J\u000e\u0010\u001b\u001a\u00020\u00042\u0006\u0010\u001c\u001a\u00020\u0011J\b\u0010\u001d\u001a\u00020\u0013H\u0007J\u0006\u0010\u001e\u001a\u00020\u000fJ\b\u0010\u001f\u001a\u00020 H\u0007J\b\u0010!\u001a\u00020 H\u0007J\u0010\u0010\"\u001a\u00020\u000f2\u0006\u0010#\u001a\u00020\u0013H\u0007J\u0010\u0010$\u001a\u00020\u000f2\u0006\u0010%\u001a\u00020\u0013H\u0007J\b\u0010&\u001a\u00020\u000fH\u0007J\u0010\u0010&\u001a\u00020\u000f2\u0006\u0010#\u001a\u00020\u0013H\u0007J\u000e\u0010'\u001a\u00020\u000f2\u0006\u0010(\u001a\u00020 J\u0010\u0010)\u001a\u00020\u000f2\u0006\u0010#\u001a\u00020\u0004H\u0007J0\u0010*\u001a\u00020\u000f2\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010+\u001a\u00020\u00042\u0006\u0010,\u001a\u00020\u00132\u0006\u0010-\u001a\u00020\u00042\u0006\u0010.\u001a\u00020\u0004H\u0007J\b\u0010/\u001a\u00020\u000fH\u0007J\u0010\u0010/\u001a\u00020\u000f2\u0006\u0010#\u001a\u00020\u0013H\u0007J\b\u00100\u001a\u00020\u000fH\u0007R\u001e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\b\u001a\u00020\u00048F\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\u0007R\u000e\u0010\n\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u00061"}, d2={"Lnet/ccbluex/liquidbounce/utils/MovementUtil;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "()V", "<set-?>", "", "bps", "getBps", "()D", "direction", "getDirection", "lastX", "lastY", "lastZ", "calculateGround", "doTargetStrafe", "", "curTarget", "Lnet/minecraft/entity/EntityLivingBase;", "direction_", "", "radius", "moveEvent", "Lnet/ccbluex/liquidbounce/event/MoveEvent;", "mathRadius", "", "forward", "length", "getBlockSpeed", "entityIn", "getSpeed", "handleVanillaKickBypass", "hasMotion", "", "isMoving", "limitSpeed", "speed", "limitSpeedByPercent", "percent", "move", "resetMotion", "y", "setMotion", "setSpeed", "moveSpeed", "pseudoYaw", "pseudoStrafe", "pseudoForward", "strafe", "updateBlocksPerSecond", "KyinoClient"})
public final class MovementUtil
extends MinecraftInstance {
    private static double bps;
    private static double lastX;
    private static double lastY;
    private static double lastZ;
    public static final MovementUtil INSTANCE;

    @JvmStatic
    public static final float getSpeed() {
        double d = MinecraftInstance.mc.field_71439_g.field_70159_w * MinecraftInstance.mc.field_71439_g.field_70159_w + MinecraftInstance.mc.field_71439_g.field_70179_y * MinecraftInstance.mc.field_71439_g.field_70179_y;
        boolean bl = false;
        return (float)Math.sqrt(d);
    }

    @JvmStatic
    public static final void strafe() {
        MovementUtil.strafe(MovementUtil.getSpeed());
    }

    @JvmStatic
    public static final void move() {
        MovementUtil.move(MovementUtil.getSpeed());
    }

    @JvmStatic
    public static final boolean isMoving() {
        return MinecraftInstance.mc.field_71439_g != null && (MinecraftInstance.mc.field_71439_g.field_71158_b.field_78900_b != 0.0f || MinecraftInstance.mc.field_71439_g.field_71158_b.field_78902_a != 0.0f);
    }

    @JvmStatic
    public static final boolean hasMotion() {
        return MinecraftInstance.mc.field_71439_g.field_70159_w != 0.0 && MinecraftInstance.mc.field_71439_g.field_70179_y != 0.0 && MinecraftInstance.mc.field_71439_g.field_70181_x != 0.0;
    }

    public final void doTargetStrafe(@NotNull EntityLivingBase curTarget, float direction_, float radius, @NotNull MoveEvent moveEvent, int mathRadius) {
        Intrinsics.checkParameterIsNotNull(curTarget, "curTarget");
        Intrinsics.checkParameterIsNotNull(moveEvent, "moveEvent");
        if (!MovementUtil.isMoving()) {
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
            curDistance = MinecraftInstance.mc.field_71439_g.func_70032_d((Entity)curTarget);
        } else if (mathRadius == 0) {
            double d2 = (MinecraftInstance.mc.field_71439_g.field_70165_t - curTarget.field_70165_t) * (MinecraftInstance.mc.field_71439_g.field_70165_t - curTarget.field_70165_t) + (MinecraftInstance.mc.field_71439_g.field_70161_v - curTarget.field_70161_v) * (MinecraftInstance.mc.field_71439_g.field_70161_v - curTarget.field_70161_v);
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
        MinecraftInstance.mc.field_71439_g.field_70159_w = moveEvent.getX();
        MinecraftInstance.mc.field_71439_g.field_70179_y = moveEvent.getZ();
    }

    public static /* synthetic */ void doTargetStrafe$default(MovementUtil movementUtil, EntityLivingBase entityLivingBase, float f, float f2, MoveEvent moveEvent, int n, int n2, Object object) {
        if ((n2 & 0x10) != 0) {
            n = 0;
        }
        movementUtil.doTargetStrafe(entityLivingBase, f, f2, moveEvent, n);
    }

    @JvmStatic
    public static final void strafe(float speed) {
        if (!MovementUtil.isMoving()) {
            return;
        }
        double yaw = INSTANCE.getDirection();
        EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.field_71439_g;
        boolean bl = false;
        double d = Math.sin(yaw);
        entityPlayerSP.field_70159_w = -d * (double)speed;
        entityPlayerSP = MinecraftInstance.mc.field_71439_g;
        bl = false;
        d = Math.cos(yaw);
        entityPlayerSP.field_70179_y = d * (double)speed;
    }

    public final double getBlockSpeed(@NotNull EntityLivingBase entityIn) {
        Intrinsics.checkParameterIsNotNull(entityIn, "entityIn");
        return BigDecimal.valueOf(Math.sqrt(Math.pow(entityIn.field_70165_t - entityIn.field_70169_q, 2.0) + Math.pow(entityIn.field_70161_v - entityIn.field_70166_s, 2.0)) * (double)20).setScale(1, 4).doubleValue();
    }

    public final void resetMotion(boolean y) {
        MinecraftInstance.mc.field_71439_g.field_70159_w = 0.0;
        MinecraftInstance.mc.field_71439_g.field_70179_y = 0.0;
        if (y) {
            MinecraftInstance.mc.field_71439_g.field_70181_x = 0.0;
        }
    }

    @JvmStatic
    public static final void move(float speed) {
        if (!MovementUtil.isMoving()) {
            return;
        }
        double yaw = INSTANCE.getDirection();
        EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.field_71439_g;
        double d = entityPlayerSP.field_70159_w;
        EntityPlayerSP entityPlayerSP2 = entityPlayerSP;
        boolean bl = false;
        double d2 = Math.sin(yaw);
        entityPlayerSP2.field_70159_w = d + -d2 * (double)speed;
        EntityPlayerSP entityPlayerSP3 = MinecraftInstance.mc.field_71439_g;
        d = entityPlayerSP3.field_70179_y;
        entityPlayerSP2 = entityPlayerSP3;
        bl = false;
        d2 = Math.cos(yaw);
        entityPlayerSP2.field_70179_y = d + d2 * (double)speed;
    }

    @JvmStatic
    public static final void limitSpeed(float speed) {
        double yaw = INSTANCE.getDirection();
        boolean bl = false;
        double maxXSpeed = -Math.sin(yaw) * (double)speed;
        boolean bl2 = false;
        double maxZSpeed = Math.cos(yaw) * (double)speed;
        if (MinecraftInstance.mc.field_71439_g.field_70159_w > maxZSpeed) {
            MinecraftInstance.mc.field_71439_g.field_70159_w = maxXSpeed;
        }
        if (MinecraftInstance.mc.field_71439_g.field_70179_y > maxZSpeed) {
            MinecraftInstance.mc.field_71439_g.field_70179_y = maxZSpeed;
        }
    }

    @JvmStatic
    public static final void limitSpeedByPercent(float percent) {
        MinecraftInstance.mc.field_71439_g.field_70159_w *= (double)percent;
        MinecraftInstance.mc.field_71439_g.field_70179_y *= (double)percent;
    }

    @JvmStatic
    public static final void forward(double length) {
        double yaw = Math.toRadians(MinecraftInstance.mc.field_71439_g.field_70177_z);
        double d = MinecraftInstance.mc.field_71439_g.field_70165_t;
        EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.field_71439_g;
        boolean bl = false;
        double d2 = Math.sin(yaw);
        double d3 = d + -d2 * length;
        double d4 = MinecraftInstance.mc.field_71439_g.field_70161_v;
        d2 = MinecraftInstance.mc.field_71439_g.field_70163_u;
        d = d3;
        bl = false;
        double d5 = Math.cos(yaw);
        entityPlayerSP.func_70107_b(d, d2, d4 + d5 * length);
    }

    public final double getDirection() {
        float rotationYaw = MinecraftInstance.mc.field_71439_g.field_70177_z;
        if (MinecraftInstance.mc.field_71439_g.field_70701_bs < 0.0f) {
            rotationYaw += 180.0f;
        }
        float forward = 1.0f;
        if (MinecraftInstance.mc.field_71439_g.field_70701_bs < 0.0f) {
            forward = -0.5f;
        } else if (MinecraftInstance.mc.field_71439_g.field_70701_bs > 0.0f) {
            forward = 0.5f;
        }
        if (MinecraftInstance.mc.field_71439_g.field_70702_br > 0.0f) {
            rotationYaw -= 90.0f * forward;
        }
        if (MinecraftInstance.mc.field_71439_g.field_70702_br < 0.0f) {
            rotationYaw += 90.0f * forward;
        }
        return Math.toRadians(rotationYaw);
    }

    public final double getBps() {
        return bps;
    }

    @JvmStatic
    public static final void setMotion(double speed) {
        double forward = MinecraftInstance.mc.field_71439_g.field_71158_b.field_78900_b;
        double strafe = MinecraftInstance.mc.field_71439_g.field_71158_b.field_78902_a;
        float yaw = MinecraftInstance.mc.field_71439_g.field_70177_z;
        if (forward == 0.0 && strafe == 0.0) {
            MinecraftInstance.mc.field_71439_g.field_70159_w = 0.0;
            MinecraftInstance.mc.field_71439_g.field_70179_y = 0.0;
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (float)(forward > 0.0 ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += (float)(forward > 0.0 ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            double d = Math.toRadians(yaw + 90.0f);
            boolean bl = false;
            double cos = Math.cos(d);
            double d2 = Math.toRadians(yaw + 90.0f);
            boolean bl2 = false;
            double sin = Math.sin(d2);
            MinecraftInstance.mc.field_71439_g.field_70159_w = forward * speed * cos + strafe * speed * sin;
            MinecraftInstance.mc.field_71439_g.field_70179_y = forward * speed * sin - strafe * speed * cos;
        }
    }

    @JvmStatic
    public static final void updateBlocksPerSecond() {
        if (MinecraftInstance.mc.field_71439_g == null || MinecraftInstance.mc.field_71439_g.field_70173_aa < 1) {
            bps = 0.0;
        }
        double distance = MinecraftInstance.mc.field_71439_g.func_70011_f(lastX, lastY, lastZ);
        lastX = MinecraftInstance.mc.field_71439_g.field_70165_t;
        lastY = MinecraftInstance.mc.field_71439_g.field_70163_u;
        lastZ = MinecraftInstance.mc.field_71439_g.field_70161_v;
        bps = distance * (double)((float)20 * MinecraftInstance.mc.field_71428_T.field_74278_d);
    }

    @JvmStatic
    public static final void setSpeed(@NotNull MoveEvent moveEvent, double moveSpeed, float pseudoYaw, double pseudoStrafe, double pseudoForward) {
        Intrinsics.checkParameterIsNotNull(moveEvent, "moveEvent");
        double forward = pseudoForward;
        double strafe = pseudoStrafe;
        float yaw = pseudoYaw;
        if (forward == 0.0 && strafe == 0.0) {
            moveEvent.setZ(0.0);
            moveEvent.setX(0.0);
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (float)(forward > 0.0 ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += (float)(forward > 0.0 ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            double cos = Math.cos(Math.toRadians(yaw + 90.0f));
            double sin = Math.sin(Math.toRadians(yaw + 90.0f));
            moveEvent.setX(forward * moveSpeed * cos + strafe * moveSpeed * sin);
            moveEvent.setZ(forward * moveSpeed * sin - strafe * moveSpeed * cos);
        }
    }

    public final double calculateGround() {
        EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
        AxisAlignedBB playerBoundingBox = entityPlayerSP.func_174813_aQ();
        double blockHeight = 1.0;
        for (double ground = MinecraftInstance.mc.field_71439_g.field_70163_u; ground > 0.0; ground -= blockHeight) {
            AxisAlignedBB customBox = new AxisAlignedBB(playerBoundingBox.field_72336_d, ground + blockHeight, playerBoundingBox.field_72334_f, playerBoundingBox.field_72340_a, ground, playerBoundingBox.field_72339_c);
            if (!MinecraftInstance.mc.field_71441_e.func_72829_c(customBox)) continue;
            if (blockHeight <= 0.05) {
                return ground + blockHeight;
            }
            ground += blockHeight;
            blockHeight = 0.05;
        }
        return 0.0;
    }

    public final void handleVanillaKickBypass() {
        double ground = this.calculateGround();
        MovementUtil movementUtil = this;
        boolean bl = false;
        boolean bl2 = false;
        MovementUtil $this$run = movementUtil;
        boolean bl3 = false;
        for (double posY = MinecraftInstance.mc.field_71439_g.field_70163_u; posY > ground; posY -= 8.0) {
            Minecraft minecraft = MinecraftInstance.mc;
            Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
            minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(MinecraftInstance.mc.field_71439_g.field_70165_t, posY, MinecraftInstance.mc.field_71439_g.field_70161_v, true));
            if (posY - 8.0 < ground) break;
        }
        Minecraft minecraft = MinecraftInstance.mc;
        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
        minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(MinecraftInstance.mc.field_71439_g.field_70165_t, ground, MinecraftInstance.mc.field_71439_g.field_70161_v, true));
        for (double posY = ground; posY < MinecraftInstance.mc.field_71439_g.field_70163_u; posY += 8.0) {
            Minecraft minecraft2 = MinecraftInstance.mc;
            Intrinsics.checkExpressionValueIsNotNull(minecraft2, "mc");
            minecraft2.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(MinecraftInstance.mc.field_71439_g.field_70165_t, posY, MinecraftInstance.mc.field_71439_g.field_70161_v, true));
            if (posY + 8.0 > MinecraftInstance.mc.field_71439_g.field_70163_u) break;
        }
        Minecraft minecraft3 = MinecraftInstance.mc;
        Intrinsics.checkExpressionValueIsNotNull(minecraft3, "mc");
        minecraft3.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(MinecraftInstance.mc.field_71439_g.field_70165_t, MinecraftInstance.mc.field_71439_g.field_70163_u, MinecraftInstance.mc.field_71439_g.field_70161_v, true));
    }

    private MovementUtil() {
    }

    static {
        MovementUtil movementUtil;
        INSTANCE = movementUtil = new MovementUtil();
    }
}

