/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.potion.Potion
 *  net.minecraft.util.AxisAlignedBB
 */
package net.ccbluex.liquidbounce.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;

public final class MovementUt
extends MinecraftInstance {
    public static final List<Double> frictionValues = new ArrayList<Double>();

    public static double calculateFriction(double moveSpeed, double lastDist, double baseMoveSpeedRef) {
        frictionValues.clear();
        frictionValues.add(lastDist - lastDist / 159.9999985);
        frictionValues.add(lastDist - (moveSpeed - lastDist) / 33.3);
        double materialFriction = MovementUt.mc.field_71439_g.func_70090_H() ? (double)0.89f : (MovementUt.mc.field_71439_g.func_180799_ab() ? (double)0.535f : (double)0.98f);
        frictionValues.add(lastDist - baseMoveSpeedRef * (1.0 - materialFriction));
        return Collections.min(frictionValues);
    }

    public static float getSpeed() {
        return (float)MovementUt.getSpeed(MovementUt.mc.field_71439_g.field_70159_w, MovementUt.mc.field_71439_g.field_70179_y);
    }

    public static double getSpeed(double motionX, double motionZ) {
        return Math.sqrt(motionX * motionX + motionZ * motionZ);
    }

    public static boolean isBlockUnder() {
        if (MovementUt.mc.field_71439_g == null) {
            return false;
        }
        if (MovementUt.mc.field_71439_g.field_70163_u < 0.0) {
            return false;
        }
        for (int off = 0; off < (int)MovementUt.mc.field_71439_g.field_70163_u + 2; off += 2) {
            AxisAlignedBB bb = MovementUt.mc.field_71439_g.func_174813_aQ().func_72317_d(0.0, (double)(-off), 0.0);
            if (MovementUt.mc.field_71441_e.func_72945_a((Entity)MovementUt.mc.field_71439_g, bb).isEmpty()) continue;
            return true;
        }
        return false;
    }

    public static void accelerate() {
        MovementUt.accelerate(MovementUt.getSpeed());
    }

    public static void accelerate(float speed) {
        if (!MovementUt.isMoving()) {
            return;
        }
        double yaw = MovementUt.getDirection();
        MovementUt.mc.field_71439_g.field_70159_w += -Math.sin(yaw) * (double)speed;
        MovementUt.mc.field_71439_g.field_70179_y += Math.cos(yaw) * (double)speed;
    }

    public static void strafe() {
        MovementUt.strafe(MovementUt.getSpeed());
    }

    public static boolean isMoving() {
        return MovementUt.mc.field_71439_g != null && (MovementUt.mc.field_71439_g.field_71158_b.field_78900_b != 0.0f || MovementUt.mc.field_71439_g.field_71158_b.field_78902_a != 0.0f);
    }

    public static boolean hasMotion() {
        return MovementUt.mc.field_71439_g.field_70159_w != 0.0 && MovementUt.mc.field_71439_g.field_70179_y != 0.0 && MovementUt.mc.field_71439_g.field_70181_x != 0.0;
    }

    public static void strafe(float speed) {
        if (!MovementUt.isMoving()) {
            return;
        }
        double yaw = MovementUt.getDirection();
        MovementUt.mc.field_71439_g.field_70159_w = -Math.sin(yaw) * (double)speed;
        MovementUt.mc.field_71439_g.field_70179_y = Math.cos(yaw) * (double)speed;
    }

    public static void forward(double length) {
        double yaw = Math.toRadians(MovementUt.mc.field_71439_g.field_70177_z);
        MovementUt.mc.field_71439_g.func_70107_b(MovementUt.mc.field_71439_g.field_70165_t + -Math.sin(yaw) * length, MovementUt.mc.field_71439_g.field_70163_u, MovementUt.mc.field_71439_g.field_70161_v + Math.cos(yaw) * length);
    }

    public static double getDirection() {
        return MovementUt.getDirectionRotation(MovementUt.mc.field_71439_g.field_70177_z, MovementUt.mc.field_71439_g.field_70702_br, MovementUt.mc.field_71439_g.field_70701_bs);
    }

    public static float getRawDirection() {
        return MovementUt.getRawDirectionRotation(MovementUt.mc.field_71439_g.field_70177_z, MovementUt.mc.field_71439_g.field_70702_br, MovementUt.mc.field_71439_g.field_70701_bs);
    }

    public static float getRawDirection(float yaw) {
        return MovementUt.getRawDirectionRotation(yaw, MovementUt.mc.field_71439_g.field_70702_br, MovementUt.mc.field_71439_g.field_70701_bs);
    }

    public static double getDirectionRotation(float yaw, float pStrafe, float pForward) {
        float rotationYaw = yaw;
        if (pForward < 0.0f) {
            rotationYaw += 180.0f;
        }
        float forward = 1.0f;
        if (pForward < 0.0f) {
            forward = -0.5f;
        } else if (pForward > 0.0f) {
            forward = 0.5f;
        }
        if (pStrafe > 0.0f) {
            rotationYaw -= 90.0f * forward;
        }
        if (pStrafe < 0.0f) {
            rotationYaw += 90.0f * forward;
        }
        return Math.toRadians(rotationYaw);
    }

    public static float getRawDirectionRotation(float yaw, float pStrafe, float pForward) {
        float rotationYaw = yaw;
        if (pForward < 0.0f) {
            rotationYaw += 180.0f;
        }
        float forward = 1.0f;
        if (pForward < 0.0f) {
            forward = -0.5f;
        } else if (pForward > 0.0f) {
            forward = 0.5f;
        }
        if (pStrafe > 0.0f) {
            rotationYaw -= 90.0f * forward;
        }
        if (pStrafe < 0.0f) {
            rotationYaw += 90.0f * forward;
        }
        return rotationYaw;
    }

    public static float getScaffoldRotation(float yaw, float strafe) {
        float rotationYaw = yaw;
        rotationYaw += 180.0f;
        float forward = -0.5f;
        if (strafe < 0.0f) {
            rotationYaw -= 90.0f * forward;
        }
        if (strafe > 0.0f) {
            rotationYaw += 90.0f * forward;
        }
        return rotationYaw;
    }

    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (MovementUt.mc.field_71439_g.func_70644_a(Potion.field_76424_c)) {
            baseSpeed *= 1.0 + 0.2 * (double)(MovementUt.mc.field_71439_g.func_70660_b(Potion.field_76424_c).func_76458_c() + 1);
        }
        return baseSpeed;
    }

    public static double getJumpBoostModifier(double baseJumpHeight) {
        if (MovementUt.mc.field_71439_g.func_70644_a(Potion.field_76430_j)) {
            int amplifier = MovementUt.mc.field_71439_g.func_70660_b(Potion.field_76430_j).func_76458_c();
            baseJumpHeight += (double)((float)(amplifier + 1) * 0.1f);
        }
        return baseJumpHeight;
    }

    public static void setSpeed(MoveEvent moveEvent, double moveSpeed) {
        MovementUt.setSpeed(moveEvent, moveSpeed, MovementUt.mc.field_71439_g.field_70177_z, MovementUt.mc.field_71439_g.field_71158_b.field_78902_a, MovementUt.mc.field_71439_g.field_71158_b.field_78900_b);
    }

    public static void setSpeed(MoveEvent moveEvent, double moveSpeed, float pseudoYaw, double pseudoStrafe, double pseudoForward) {
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
            if (strafe > 0.0) {
                strafe = 1.0;
            } else if (strafe < 0.0) {
                strafe = -1.0;
            }
            double cos = Math.cos(Math.toRadians(yaw + 90.0f));
            double sin = Math.sin(Math.toRadians(yaw + 90.0f));
            moveEvent.setX(forward * moveSpeed * cos + strafe * moveSpeed * sin);
            moveEvent.setZ(forward * moveSpeed * sin - strafe * moveSpeed * cos);
        }
    }
}

