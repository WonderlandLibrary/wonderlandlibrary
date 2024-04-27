/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 *  net.minecraft.util.AxisAlignedBB
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.utils.misc;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.event.MoveEvent;
import net.dev.important.utils.MinecraftInstance;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\n\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\r\u001a\u00020\u0004J\u000e\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0004J\u0006\u0010\u0011\u001a\u00020\u0012J\u0006\u0010\u0013\u001a\u00020\u000fJ\u0006\u0010\u0014\u001a\u00020\u0015J\u0006\u0010\u0016\u001a\u00020\u0015J\u000e\u0010\u0017\u001a\u00020\u000f2\u0006\u0010\u0018\u001a\u00020\u0012J\u000e\u0010\u0019\u001a\u00020\u000f2\u0006\u0010\u001a\u001a\u00020\u0012J\u0006\u0010\u001b\u001a\u00020\u000fJ\u000e\u0010\u001b\u001a\u00020\u000f2\u0006\u0010\u0018\u001a\u00020\u0012J\u000e\u0010\u001c\u001a\u00020\u000f2\u0006\u0010\u0018\u001a\u00020\u0004J.\u0010\u001d\u001a\u00020\u000f2\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u00042\u0006\u0010!\u001a\u00020\u00122\u0006\u0010\"\u001a\u00020\u00042\u0006\u0010#\u001a\u00020\u0004J\u0006\u0010$\u001a\u00020\u000fJ\u000e\u0010$\u001a\u00020\u000f2\u0006\u0010\u0018\u001a\u00020\u0012J\u0006\u0010%\u001a\u00020\u000fR\u001e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\b\u001a\u00020\u00048F\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\u0007R\u000e\u0010\n\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006&"}, d2={"Lnet/dev/important/utils/misc/MovementUtil;", "Lnet/dev/important/utils/MinecraftInstance;", "()V", "<set-?>", "", "bps", "getBps", "()D", "direction", "getDirection", "lastX", "lastY", "lastZ", "calculateGround", "forward", "", "length", "getSpeed", "", "handleVanillaKickBypass", "hasMotion", "", "isMoving", "limitSpeed", "speed", "limitSpeedByPercent", "percent", "move", "setMotion", "setSpeed", "moveEvent", "Lnet/dev/important/event/MoveEvent;", "moveSpeed", "pseudoYaw", "pseudoStrafe", "pseudoForward", "strafe", "updateBlocksPerSecond", "LiquidBounce"})
public final class MovementUtil
extends MinecraftInstance {
    @NotNull
    public static final MovementUtil INSTANCE = new MovementUtil();
    private static double bps;
    private static double lastX;
    private static double lastY;
    private static double lastZ;

    private MovementUtil() {
    }

    public final float getSpeed() {
        return (float)Math.sqrt(MinecraftInstance.mc.field_71439_g.field_70159_w * MinecraftInstance.mc.field_71439_g.field_70159_w + MinecraftInstance.mc.field_71439_g.field_70179_y * MinecraftInstance.mc.field_71439_g.field_70179_y);
    }

    public final void strafe() {
        this.strafe(this.getSpeed());
    }

    public final void move() {
        this.move(this.getSpeed());
    }

    public final boolean isMoving() {
        return MinecraftInstance.mc.field_71439_g != null && (!(MinecraftInstance.mc.field_71439_g.field_71158_b.field_78900_b == 0.0f) || !(MinecraftInstance.mc.field_71439_g.field_71158_b.field_78902_a == 0.0f));
    }

    public final boolean hasMotion() {
        return !(MinecraftInstance.mc.field_71439_g.field_70159_w == 0.0) && !(MinecraftInstance.mc.field_71439_g.field_70179_y == 0.0) && !(MinecraftInstance.mc.field_71439_g.field_70181_x == 0.0);
    }

    public final void strafe(float speed) {
        if (!this.isMoving()) {
            return;
        }
        double yaw = this.getDirection();
        MinecraftInstance.mc.field_71439_g.field_70159_w = -Math.sin(yaw) * (double)speed;
        MinecraftInstance.mc.field_71439_g.field_70179_y = Math.cos(yaw) * (double)speed;
    }

    public final void move(float speed) {
        if (!this.isMoving()) {
            return;
        }
        double yaw = this.getDirection();
        EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.field_71439_g;
        entityPlayerSP.field_70159_w += -Math.sin(yaw) * (double)speed;
        entityPlayerSP = MinecraftInstance.mc.field_71439_g;
        entityPlayerSP.field_70179_y += Math.cos(yaw) * (double)speed;
    }

    public final void limitSpeed(float speed) {
        double yaw = this.getDirection();
        double maxXSpeed = -Math.sin(yaw) * (double)speed;
        double maxZSpeed = Math.cos(yaw) * (double)speed;
        if (MinecraftInstance.mc.field_71439_g.field_70159_w > maxZSpeed) {
            MinecraftInstance.mc.field_71439_g.field_70159_w = maxXSpeed;
        }
        if (MinecraftInstance.mc.field_71439_g.field_70179_y > maxZSpeed) {
            MinecraftInstance.mc.field_71439_g.field_70179_y = maxZSpeed;
        }
    }

    public final void limitSpeedByPercent(float percent) {
        EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.field_71439_g;
        entityPlayerSP.field_70159_w *= (double)percent;
        entityPlayerSP = MinecraftInstance.mc.field_71439_g;
        entityPlayerSP.field_70179_y *= (double)percent;
    }

    public final void forward(double length) {
        double yaw = Math.toRadians(MinecraftInstance.mc.field_71439_g.field_70177_z);
        MinecraftInstance.mc.field_71439_g.func_70107_b(MinecraftInstance.mc.field_71439_g.field_70165_t + -Math.sin(yaw) * length, MinecraftInstance.mc.field_71439_g.field_70163_u, MinecraftInstance.mc.field_71439_g.field_70161_v + Math.cos(yaw) * length);
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

    public final void setMotion(double speed) {
        double forward = MinecraftInstance.mc.field_71439_g.field_71158_b.field_78900_b;
        double strafe = MinecraftInstance.mc.field_71439_g.field_71158_b.field_78902_a;
        float yaw = MinecraftInstance.mc.field_71439_g.field_70177_z;
        if (forward == 0.0 && strafe == 0.0) {
            MinecraftInstance.mc.field_71439_g.field_70159_w = 0.0;
            MinecraftInstance.mc.field_71439_g.field_70179_y = 0.0;
        } else {
            if (!(forward == 0.0)) {
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
            MinecraftInstance.mc.field_71439_g.field_70159_w = forward * speed * cos + strafe * speed * sin;
            MinecraftInstance.mc.field_71439_g.field_70179_y = forward * speed * sin - strafe * speed * cos;
        }
    }

    public final void updateBlocksPerSecond() {
        if (MinecraftInstance.mc.field_71439_g == null || MinecraftInstance.mc.field_71439_g.field_70173_aa < 1) {
            bps = 0.0;
        }
        double distance = MinecraftInstance.mc.field_71439_g.func_70011_f(lastX, lastY, lastZ);
        lastX = MinecraftInstance.mc.field_71439_g.field_70165_t;
        lastY = MinecraftInstance.mc.field_71439_g.field_70163_u;
        lastZ = MinecraftInstance.mc.field_71439_g.field_70161_v;
        bps = distance * (double)((float)20 * MinecraftInstance.mc.field_71428_T.field_74278_d);
    }

    public final void setSpeed(@NotNull MoveEvent moveEvent, double moveSpeed, float pseudoYaw, double pseudoStrafe, double pseudoForward) {
        Intrinsics.checkNotNullParameter(moveEvent, "moveEvent");
        double forward = pseudoForward;
        double strafe = pseudoStrafe;
        float yaw = pseudoYaw;
        if (forward == 0.0 && strafe == 0.0) {
            moveEvent.setZ(0.0);
            moveEvent.setX(0.0);
        } else {
            if (!(forward == 0.0)) {
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
        AxisAlignedBB playerBoundingBox = MinecraftInstance.mc.field_71439_g.func_174813_aQ();
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
        MovementUtil $this$handleVanillaKickBypass_u24lambda_u2d0 = this;
        boolean bl = false;
        for (double posY = MinecraftInstance.mc.field_71439_g.field_70163_u; posY > ground; posY -= 8.0) {
            MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(MinecraftInstance.mc.field_71439_g.field_70165_t, posY, MinecraftInstance.mc.field_71439_g.field_70161_v, true));
            if (posY - 8.0 < ground) break;
        }
        MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(MinecraftInstance.mc.field_71439_g.field_70165_t, ground, MinecraftInstance.mc.field_71439_g.field_70161_v, true));
        for (double posY = ground; posY < MinecraftInstance.mc.field_71439_g.field_70163_u; posY += 8.0) {
            MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(MinecraftInstance.mc.field_71439_g.field_70165_t, posY, MinecraftInstance.mc.field_71439_g.field_70161_v, true));
            if (posY + 8.0 > MinecraftInstance.mc.field_71439_g.field_70163_u) break;
        }
        MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(MinecraftInstance.mc.field_71439_g.field_70165_t, MinecraftInstance.mc.field_71439_g.field_70163_u, MinecraftInstance.mc.field_71439_g.field_70161_v, true));
    }
}

