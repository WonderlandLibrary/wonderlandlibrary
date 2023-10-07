/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockIce
 *  net.minecraft.block.BlockPackedIce
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.potion.Potion
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.BlockPos
 */
package net.ccbluex.liquidbounce.utils;

import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.block.Block;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockPackedIce;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public final class MovementUtils2233
extends MinecraftInstance {
    private static double lastX = -999999.0;
    private static double lastZ = -999999.0;

    public static float getSpeed() {
        return (float)MovementUtils2233.getSpeed(MovementUtils2233.mc.field_71439_g.field_70159_w, MovementUtils2233.mc.field_71439_g.field_70179_y);
    }

    public static double getSpeed(double motionX, double motionZ) {
        return Math.sqrt(motionX * motionX + motionZ * motionZ);
    }

    public static boolean isOnIce() {
        EntityPlayerSP player = MovementUtils2233.mc.field_71439_g;
        Block blockUnder = MovementUtils2233.mc.field_71441_e.func_180495_p(new BlockPos(player.field_70165_t, player.field_70163_u - 1.0, player.field_70161_v)).func_177230_c();
        return blockUnder instanceof BlockIce || blockUnder instanceof BlockPackedIce;
    }

    public static boolean isBlockUnder() {
        if (MovementUtils2233.mc.field_71439_g == null) {
            return false;
        }
        if (MovementUtils2233.mc.field_71439_g.field_70163_u < 0.0) {
            return false;
        }
        for (int off = 0; off < (int)MovementUtils2233.mc.field_71439_g.field_70163_u + 2; off += 2) {
            AxisAlignedBB bb = MovementUtils2233.mc.field_71439_g.func_174813_aQ().func_72317_d(0.0, (double)(-off), 0.0);
            if (MovementUtils2233.mc.field_71441_e.func_72945_a((Entity)MovementUtils2233.mc.field_71439_g, bb).isEmpty()) continue;
            return true;
        }
        return false;
    }

    public static boolean isMoving() {
        return MovementUtils2233.mc.field_71439_g != null && (MovementUtils2233.mc.field_71439_g.field_71158_b.field_78900_b != 0.0f || MovementUtils2233.mc.field_71439_g.field_71158_b.field_78902_a != 0.0f);
    }

    public static boolean hasMotion() {
        return MovementUtils2233.mc.field_71439_g.field_70159_w != 0.0 && MovementUtils2233.mc.field_71439_g.field_70179_y != 0.0 && MovementUtils2233.mc.field_71439_g.field_70181_x != 0.0;
    }

    public static void strafeCustom(float speed, float cYaw, float strafe, float forward) {
        if (!MovementUtils2233.isMoving()) {
            return;
        }
        double yaw = MovementUtils2233.getDirectionRotation(cYaw, strafe, forward);
        MovementUtils2233.mc.field_71439_g.field_70159_w = -Math.sin(yaw) * (double)speed;
        MovementUtils2233.mc.field_71439_g.field_70179_y = Math.cos(yaw) * (double)speed;
    }

    public static void forward(double length) {
        double yaw = Math.toRadians(MovementUtils2233.mc.field_71439_g.field_70177_z);
        MovementUtils2233.mc.field_71439_g.func_70107_b(MovementUtils2233.mc.field_71439_g.field_70165_t + -Math.sin(yaw) * length, MovementUtils2233.mc.field_71439_g.field_70163_u, MovementUtils2233.mc.field_71439_g.field_70161_v + Math.cos(yaw) * length);
    }

    public static float getRawDirection() {
        return MovementUtils2233.getRawDirectionRotation(MovementUtils2233.mc.field_71439_g.field_70177_z, MovementUtils2233.mc.field_71439_g.field_70702_br, MovementUtils2233.mc.field_71439_g.field_70701_bs);
    }

    public static double[] getXZDist(float speed, float cYaw) {
        double[] arr = new double[2];
        double yaw = MovementUtils2233.getDirectionRotation(cYaw, MovementUtils2233.mc.field_71439_g.field_70702_br, MovementUtils2233.mc.field_71439_g.field_70701_bs);
        arr[0] = -Math.sin(yaw) * (double)speed;
        arr[1] = Math.cos(yaw) * (double)speed;
        return arr;
    }

    public static float getPredictionYaw(double x, double z) {
        if (MovementUtils2233.mc.field_71439_g == null) {
            lastX = -999999.0;
            lastZ = -999999.0;
            return 0.0f;
        }
        if (lastX == -999999.0) {
            lastX = MovementUtils2233.mc.field_71439_g.field_70169_q;
        }
        if (lastZ == -999999.0) {
            lastZ = MovementUtils2233.mc.field_71439_g.field_70166_s;
        }
        float returnValue = (float)(Math.atan2(z - lastZ, x - lastX) * 180.0 / Math.PI);
        lastX = x;
        lastZ = z;
        return returnValue;
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

    public static int getJumpEffect() {
        return MovementUtils2233.mc.field_71439_g.func_70644_a(Potion.field_76430_j) ? MovementUtils2233.mc.field_71439_g.func_70660_b(Potion.field_76430_j).func_76458_c() + 1 : 0;
    }

    public static int getSpeedEffect() {
        return MovementUtils2233.mc.field_71439_g.func_70644_a(Potion.field_76424_c) ? MovementUtils2233.mc.field_71439_g.func_70660_b(Potion.field_76424_c).func_76458_c() + 1 : 0;
    }

    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (MovementUtils2233.mc.field_71439_g.func_70644_a(Potion.field_76424_c)) {
            baseSpeed *= 1.0 + 0.2 * (double)(MovementUtils2233.mc.field_71439_g.func_70660_b(Potion.field_76424_c).func_76458_c() + 1);
        }
        return baseSpeed;
    }

    public static double getBaseMoveSpeed(double customSpeed) {
        double baseSpeed;
        double d = baseSpeed = MovementUtils2233.isOnIce() ? 0.258977700006 : customSpeed;
        if (MovementUtils2233.mc.field_71439_g.func_70644_a(Potion.field_76424_c)) {
            int amplifier = MovementUtils2233.mc.field_71439_g.func_70660_b(Potion.field_76424_c).func_76458_c();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

    public static double getJumpBoostModifier(double baseJumpHeight) {
        return MovementUtils2233.getJumpBoostModifier(baseJumpHeight, true);
    }

    public static double getJumpBoostModifier(double baseJumpHeight, boolean potionJump) {
        if (MovementUtils2233.mc.field_71439_g.func_70644_a(Potion.field_76430_j) && potionJump) {
            int amplifier = MovementUtils2233.mc.field_71439_g.func_70660_b(Potion.field_76430_j).func_76458_c();
            baseJumpHeight += (double)((float)(amplifier + 1) * 0.1f);
        }
        return baseJumpHeight;
    }

    public static void setMotion(MoveEvent event, double speed, double motion, boolean smoothStrafe) {
        int direction;
        double forward = MovementUtils2233.mc.field_71439_g.field_71158_b.field_78900_b;
        double strafe = MovementUtils2233.mc.field_71439_g.field_71158_b.field_78902_a;
        double yaw = MovementUtils2233.mc.field_71439_g.field_70177_z;
        int n = direction = smoothStrafe ? 45 : 90;
        if (forward == 0.0 && strafe == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (double)(forward > 0.0 ? -direction : direction);
                } else if (strafe < 0.0) {
                    yaw += (double)(forward > 0.0 ? direction : -direction);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            double cos = Math.cos(Math.toRadians(yaw + 90.0));
            double sin = Math.sin(Math.toRadians(yaw + 90.0));
            event.setX((forward * speed * cos + strafe * speed * sin) * motion);
            event.setZ((forward * speed * sin - strafe * speed * cos) * motion);
        }
    }

    public static void setMotion(double speed, boolean smoothStrafe) {
        int direction;
        double forward = MovementUtils2233.mc.field_71439_g.field_71158_b.field_78900_b;
        double strafe = MovementUtils2233.mc.field_71439_g.field_71158_b.field_78902_a;
        float yaw = MovementUtils2233.mc.field_71439_g.field_70177_z;
        int n = direction = smoothStrafe ? 45 : 90;
        if (forward == 0.0 && strafe == 0.0) {
            MovementUtils2233.mc.field_71439_g.field_70159_w = 0.0;
            MovementUtils2233.mc.field_71439_g.field_70179_y = 0.0;
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (float)(forward > 0.0 ? -direction : direction);
                } else if (strafe < 0.0) {
                    yaw += (float)(forward > 0.0 ? direction : -direction);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            MovementUtils2233.mc.field_71439_g.field_70159_w = forward * speed * -Math.sin(Math.toRadians(yaw)) + strafe * speed * Math.cos(Math.toRadians(yaw));
            MovementUtils2233.mc.field_71439_g.field_70179_y = forward * speed * Math.cos(Math.toRadians(yaw)) - strafe * speed * -Math.sin(Math.toRadians(yaw));
        }
    }

    public static void setSpeed(MoveEvent moveEvent, double moveSpeed) {
        MovementUtils2233.setSpeed(moveEvent, moveSpeed, MovementUtils2233.mc.field_71439_g.field_70177_z, MovementUtils2233.mc.field_71439_g.field_71158_b.field_78902_a, MovementUtils2233.mc.field_71439_g.field_71158_b.field_78900_b);
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

