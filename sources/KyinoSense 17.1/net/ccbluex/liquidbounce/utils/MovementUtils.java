/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.potion.Potion
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.BlockPos
 */
package net.ccbluex.liquidbounce.utils;

import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public final class MovementUtils
extends MinecraftInstance {
    private static double bps = 0.0;
    private static double lastX = 0.0;
    private static double lastY = 0.0;
    private static double lastZ = 0.0;

    public static float getPredictionYaw(double x, double z) {
        if (MovementUtils.mc.field_71439_g == null) {
            lastX = -999999.0;
            lastZ = -999999.0;
            return 0.0f;
        }
        if (lastX == -999999.0) {
            lastX = MovementUtils.mc.field_71439_g.field_70169_q;
        }
        if (lastZ == -999999.0) {
            lastZ = MovementUtils.mc.field_71439_g.field_70166_s;
        }
        float returnValue = (float)(Math.atan2(z - lastZ, x - lastX) * 180.0 / Math.PI);
        lastX = x;
        lastZ = z;
        return returnValue;
    }

    public static float getSpeed() {
        return (float)MovementUtils.getSpeed(MovementUtils.mc.field_71439_g.field_70159_w, MovementUtils.mc.field_71439_g.field_70179_y);
    }

    public static double getSpeed(double motionX, double motionZ) {
        return Math.sqrt(motionX * motionX + motionZ * motionZ);
    }

    public static boolean isBlockUnder() {
        if (MovementUtils.mc.field_71439_g == null) {
            return false;
        }
        if (MovementUtils.mc.field_71439_g.field_70163_u < 0.0) {
            return false;
        }
        for (int off = 0; off < (int)MovementUtils.mc.field_71439_g.field_70163_u + 2; off += 2) {
            AxisAlignedBB bb = MovementUtils.mc.field_71439_g.func_174813_aQ().func_72317_d(0.0, (double)(-off), 0.0);
            if (MovementUtils.mc.field_71441_e.func_72945_a((Entity)MovementUtils.mc.field_71439_g, bb).isEmpty()) continue;
            return true;
        }
        return false;
    }

    public static void strafe() {
        MovementUtils.strafe(MovementUtils.getSpeed());
    }

    public static void move() {
        MovementUtils.move(MovementUtils.getSpeed());
    }

    public static boolean isMoving() {
        return MovementUtils.mc.field_71439_g != null && (MovementUtils.mc.field_71439_g.field_71158_b.field_78900_b != 0.0f || MovementUtils.mc.field_71439_g.field_71158_b.field_78902_a != 0.0f);
    }

    public static Block getBlockUnderPlayer(EntityPlayer inPlayer, double height) {
        return MovementUtils.mc.field_71441_e.func_180495_p(new BlockPos(inPlayer.field_70165_t, inPlayer.field_70163_u - height, inPlayer.field_70161_v)).func_177230_c();
    }

    public static boolean hasMotion() {
        return MovementUtils.mc.field_71439_g.field_70159_w != 0.0 && MovementUtils.mc.field_71439_g.field_70179_y != 0.0 && MovementUtils.mc.field_71439_g.field_70181_x != 0.0;
    }

    public static void strafe(float speed) {
        if (!MovementUtils.isMoving()) {
            return;
        }
        double yaw = MovementUtils.getDirection();
        MovementUtils.mc.field_71439_g.field_70159_w = -Math.sin(yaw) * (double)speed;
        MovementUtils.mc.field_71439_g.field_70179_y = Math.cos(yaw) * (double)speed;
    }

    public static boolean isOnGround(double height) {
        return !MovementUtils.mc.field_71441_e.func_72945_a((Entity)MovementUtils.mc.field_71439_g, MovementUtils.mc.field_71439_g.func_174813_aQ().func_72317_d(0.0, -height, 0.0)).isEmpty();
    }

    public static int getJumpEffect() {
        if (MovementUtils.mc.field_71439_g.func_70644_a(Potion.field_76430_j)) {
            return MovementUtils.mc.field_71439_g.func_70660_b(Potion.field_76430_j).func_76458_c() + 1;
        }
        return 0;
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

    public static void move(float speed) {
        if (!MovementUtils.isMoving()) {
            return;
        }
        double yaw = MovementUtils.getDirection();
        MovementUtils.mc.field_71439_g.field_70159_w += -Math.sin(yaw) * (double)speed;
        MovementUtils.mc.field_71439_g.field_70179_y += Math.cos(yaw) * (double)speed;
    }

    public static void limitSpeed(float speed) {
        double yaw = MovementUtils.getDirection();
        double maxXSpeed = -Math.sin(yaw) * (double)speed;
        double maxZSpeed = Math.cos(yaw) * (double)speed;
        if (MovementUtils.mc.field_71439_g.field_70159_w > maxZSpeed) {
            MovementUtils.mc.field_71439_g.field_70159_w = maxXSpeed;
        }
        if (MovementUtils.mc.field_71439_g.field_70179_y > maxZSpeed) {
            MovementUtils.mc.field_71439_g.field_70179_y = maxZSpeed;
        }
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

    public static void strafeCustom(float speed, float cYaw, float strafe, float forward) {
        if (!MovementUtils.isMoving()) {
            return;
        }
        double yaw = MovementUtils.getDirectionRotation(cYaw, strafe, forward);
        MovementUtils.mc.field_71439_g.field_70159_w = -Math.sin(yaw) * (double)speed;
        MovementUtils.mc.field_71439_g.field_70179_y = Math.cos(yaw) * (double)speed;
    }

    public static void forward(double length) {
        double yaw = Math.toRadians(MovementUtils.mc.field_71439_g.field_70177_z);
        MovementUtils.mc.field_71439_g.func_70107_b(MovementUtils.mc.field_71439_g.field_70165_t + -Math.sin(yaw) * length, MovementUtils.mc.field_71439_g.field_70163_u, MovementUtils.mc.field_71439_g.field_70161_v + Math.cos(yaw) * length);
    }

    public static double getDirection() {
        float rotationYaw = MovementUtils.mc.field_71439_g.field_70177_z;
        if (MovementUtils.mc.field_71439_g.field_70701_bs < 0.0f) {
            rotationYaw += 180.0f;
        }
        float forward = 1.0f;
        if (MovementUtils.mc.field_71439_g.field_70701_bs < 0.0f) {
            forward = -0.5f;
        } else if (MovementUtils.mc.field_71439_g.field_70701_bs > 0.0f) {
            forward = 0.5f;
        }
        if (MovementUtils.mc.field_71439_g.field_70702_br > 0.0f) {
            rotationYaw -= 90.0f * forward;
        }
        if (MovementUtils.mc.field_71439_g.field_70702_br < 0.0f) {
            rotationYaw += 90.0f * forward;
        }
        return Math.toRadians(rotationYaw);
    }

    public static void setMotion(double speed, Object get) {
        double forward = MovementUtils.mc.field_71439_g.field_71158_b.field_78900_b;
        double strafe = MovementUtils.mc.field_71439_g.field_71158_b.field_78902_a;
        float yaw = MovementUtils.mc.field_71439_g.field_70177_z;
        if (forward == 0.0 && strafe == 0.0) {
            MovementUtils.mc.field_71439_g.field_70159_w = 0.0;
            MovementUtils.mc.field_71439_g.field_70179_y = 0.0;
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
            MovementUtils.mc.field_71439_g.field_70159_w = forward * speed * cos + strafe * speed * sin;
            MovementUtils.mc.field_71439_g.field_70179_y = forward * speed * sin - strafe * speed * cos;
        }
    }

    public static void setMotion2(MoveEvent event, double speed, double motion, boolean smoothStrafe) {
        int direction;
        double forward = MovementUtils.mc.field_71439_g.field_71158_b.field_78900_b;
        double strafe = MovementUtils.mc.field_71439_g.field_71158_b.field_78902_a;
        double yaw = MovementUtils.mc.field_71439_g.field_70177_z;
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

    public static void setMotion2(double speed, boolean smoothStrafe) {
        int direction;
        double forward = MovementUtils.mc.field_71439_g.field_71158_b.field_78900_b;
        double strafe = MovementUtils.mc.field_71439_g.field_71158_b.field_78902_a;
        float yaw = MovementUtils.mc.field_71439_g.field_70177_z;
        int n = direction = smoothStrafe ? 45 : 90;
        if (forward == 0.0 && strafe == 0.0) {
            MovementUtils.mc.field_71439_g.field_70159_w = 0.0;
            MovementUtils.mc.field_71439_g.field_70179_y = 0.0;
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
            MovementUtils.mc.field_71439_g.field_70159_w = forward * speed * -Math.sin(Math.toRadians(yaw)) + strafe * speed * Math.cos(Math.toRadians(yaw));
            MovementUtils.mc.field_71439_g.field_70179_y = forward * speed * Math.cos(Math.toRadians(yaw)) - strafe * speed * -Math.sin(Math.toRadians(yaw));
        }
    }

    public static void updateBlocksPerSecond() {
        if (MovementUtils.mc.field_71439_g == null || MovementUtils.mc.field_71439_g.field_70173_aa < 1) {
            bps = 0.0;
        }
        double distance = MovementUtils.mc.field_71439_g.func_70011_f(lastX, lastY, lastZ);
        lastX = MovementUtils.mc.field_71439_g.field_70165_t;
        lastY = MovementUtils.mc.field_71439_g.field_70163_u;
        lastZ = MovementUtils.mc.field_71439_g.field_70161_v;
        bps = distance * (double)(20.0f * MovementUtils.mc.field_71428_T.field_74278_d);
    }

    public static double getBlocksPerSecond() {
        return bps;
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
            double cos = Math.cos(Math.toRadians(yaw + 90.0f));
            double sin = Math.sin(Math.toRadians(yaw + 90.0f));
            moveEvent.setX(forward * moveSpeed * cos + strafe * moveSpeed * sin);
            moveEvent.setZ(forward * moveSpeed * sin - strafe * moveSpeed * cos);
        }
    }

    public static double getBaseMoveSpeed(double customSpeed) {
        double baseSpeed = customSpeed;
        if (baseSpeed > 0.0) {
            baseSpeed *= 1.0 + 0.2 * (double)MovementUtils.getSpeedEffect();
        }
        return baseSpeed;
    }

    public static int getSpeedEffect() {
        if (MovementUtils.mc.field_71439_g.func_70644_a(Potion.field_76424_c)) {
            return MovementUtils.mc.field_71439_g.func_70660_b(Potion.field_76424_c).func_76458_c() + 1;
        }
        return 0;
    }

    public static double calculateGround() {
        AxisAlignedBB playerBoundingBox = MovementUtils.mc.field_71439_g.func_174813_aQ();
        double blockHeight = 1.0;
        for (double ground = MovementUtils.mc.field_71439_g.field_70163_u; ground > 0.0; ground -= blockHeight) {
            AxisAlignedBB customBox = new AxisAlignedBB(playerBoundingBox.field_72336_d, ground + blockHeight, playerBoundingBox.field_72334_f, playerBoundingBox.field_72340_a, ground, playerBoundingBox.field_72339_c);
            if (!MovementUtils.mc.field_71441_e.func_72829_c(customBox)) continue;
            if (blockHeight <= 0.05) {
                return ground + blockHeight;
            }
            ground += blockHeight;
            blockHeight = 0.05;
        }
        return 0.0;
    }

    public static void setSpeedTargetStrafe(MoveEvent moveEvent, double moveSpeed) {
        MovementUtils.setSpeedTargetStrafe(moveEvent, moveSpeed, MovementUtils.mc.field_71439_g.field_70177_z, MovementUtils.mc.field_71439_g.field_71158_b.field_78902_a, MovementUtils.mc.field_71439_g.field_71158_b.field_78900_b);
    }

    public static void setSpeedTargetStrafe(MoveEvent moveEvent, double moveSpeed, float pseudoYaw, double pseudoStrafe, double pseudoForward) {
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

