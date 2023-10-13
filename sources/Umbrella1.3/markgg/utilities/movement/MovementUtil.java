/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package markgg.utilities.movement;

import markgg.events.Event;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;

public class MovementUtil {
    protected static Minecraft mc = Minecraft.getMinecraft();

    public static boolean isMovingOnGround() {
        return MovementUtil.isMoving() && MovementUtil.mc.thePlayer.onGround;
    }

    public static int getForwardCode() {
        return Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode();
    }

    public static boolean isOnLadder() {
        return Minecraft.getMinecraft().thePlayer.isOnLadder();
    }

    public static double getJumpHeight(double speed) {
        return MovementUtil.mc.thePlayer.isPotionActive(Potion.jump) ? speed + 0.1 * (double)(MovementUtil.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) : speed;
    }

    public static void setMotionX(double x) {
        Minecraft.getMinecraft().thePlayer.motionX = x;
    }

    public static void setMotionY(double y) {
        Minecraft.getMinecraft().thePlayer.motionY = y;
    }

    public static void setMotionZ(double z) {
        Minecraft.getMinecraft().thePlayer.motionZ = z;
    }

    public static void strafe(float speed) {
        if (!MovementUtil.isMoving()) {
            return;
        }
        double yaw = MovementUtil.getDirection();
        MovementUtil.mc.thePlayer.motionX = -Math.sin(yaw) * (double)speed;
        MovementUtil.mc.thePlayer.motionZ = Math.cos(yaw) * (double)speed;
    }

    public static double getDirection() {
        float rotationYaw = MovementUtil.mc.thePlayer.rotationYaw;
        if (MovementUtil.mc.thePlayer.moveForward < 0.0f) {
            rotationYaw += 180.0f;
        }
        float forward = 1.0f;
        if (MovementUtil.mc.thePlayer.moveForward < 0.0f) {
            forward = -0.5f;
        } else if (MovementUtil.mc.thePlayer.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (MovementUtil.mc.thePlayer.moveStrafing > 0.0f) {
            rotationYaw -= 90.0f * forward;
        }
        if (MovementUtil.mc.thePlayer.moveStrafing < 0.0f) {
            rotationYaw += 90.0f * forward;
        }
        return Math.toRadians(rotationYaw);
    }

    public static void sendPosition(double x, double y, double z, boolean ground, boolean moving) {
        if (!moving) {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(MovementUtil.mc.thePlayer.posX, MovementUtil.mc.thePlayer.posY + y, MovementUtil.mc.thePlayer.posZ, ground));
        } else {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(MovementUtil.mc.thePlayer.posX + x, MovementUtil.mc.thePlayer.posY + y, MovementUtil.mc.thePlayer.posZ + z, ground));
        }
    }

    public static Block getBlockAtPos(BlockPos inBlockPos) {
        IBlockState s = MovementUtil.mc.theWorld.getBlockState(inBlockPos);
        return s.getBlock();
    }

    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2875;
        if (MovementUtil.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            baseSpeed *= 1.0 + 0.2 * (double)(MovementUtil.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return baseSpeed;
    }

    public static boolean isMoving() {
        return MovementUtil.mc.thePlayer.movementInput.moveForward != 0.0f || MovementUtil.mc.thePlayer.movementInput.moveStrafe != 0.0f;
    }

    public static double defaultMoveSpeed() {
        return MovementUtil.mc.thePlayer.isSprinting() ? (double)0.287f : (double)0.223f;
    }

    public static double getLastDistance() {
        return Math.hypot(MovementUtil.mc.thePlayer.posX - MovementUtil.mc.thePlayer.prevPosX, MovementUtil.mc.thePlayer.posZ - MovementUtil.mc.thePlayer.prevPosZ);
    }

    public static boolean isOnGround(double height) {
        return !MovementUtil.mc.theWorld.getCollidingBoundingBoxes(MovementUtil.mc.thePlayer, MovementUtil.mc.thePlayer.getEntityBoundingBox().offset(0.0, -height, 0.0)).isEmpty();
    }

    public static double jumpHeight() {
        if (MovementUtil.mc.thePlayer.isPotionActive(Potion.jump)) {
            return 0.419999986886978 + 0.1 * (double)(MovementUtil.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1);
        }
        return 0.419999986886978;
    }

    public static double getJumpBoostModifier(double baseJumpHeight) {
        if (MovementUtil.mc.thePlayer.isPotionActive(Potion.jump)) {
            int amplifier = MovementUtil.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier();
            baseJumpHeight += (double)((float)(amplifier + 1) * 0.1f);
        }
        return baseJumpHeight;
    }

    public static void setSpeed(double moveSpeed, float yaw, double strafe, double forward) {
        double fforward = MovementUtil.mc.thePlayer.movementInput.moveForward;
        double sstrafe = MovementUtil.mc.thePlayer.movementInput.moveStrafe;
        float yyaw = MovementUtil.mc.thePlayer.rotationYaw;
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
        double mx = Math.cos(Math.toRadians(yaw + 90.0f));
        double mz = Math.sin(Math.toRadians(yaw + 90.0f));
        MovementUtil.mc.thePlayer.motionX = forward * moveSpeed * mx + strafe * moveSpeed * mz;
        MovementUtil.mc.thePlayer.motionZ = forward * moveSpeed * mz - strafe * moveSpeed * mx;
    }

    public static float getSpeed() {
        return (float)Math.sqrt(MovementUtil.mc.thePlayer.motionX * MovementUtil.mc.thePlayer.motionX + MovementUtil.mc.thePlayer.motionZ * MovementUtil.mc.thePlayer.motionZ);
    }

    public static void setMotionWithValues(Event em, double speed, float yaw, double forward, double strafe) {
        if (forward == 0.0 && strafe == 0.0) {
            MovementUtil.mc.thePlayer.motionX = 0.0;
            MovementUtil.mc.thePlayer.motionZ = 0.0;
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
            MovementUtil.mc.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f));
            MovementUtil.mc.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f));
        }
    }

    public static boolean isMovingWithKeys() {
        return Keyboard.isKeyDown((int)MovementUtil.mc.gameSettings.keyBindForward.getKeyCode()) || Keyboard.isKeyDown((int)MovementUtil.mc.gameSettings.keyBindBack.getKeyCode()) || Keyboard.isKeyDown((int)MovementUtil.mc.gameSettings.keyBindLeft.getKeyCode()) || Keyboard.isKeyDown((int)MovementUtil.mc.gameSettings.keyBindRight.getKeyCode());
    }

    public static void setSpeed(double moveSpeed) {
        MovementUtil.setSpeed(moveSpeed, MovementUtil.mc.thePlayer.rotationYaw, MovementUtil.mc.thePlayer.movementInput.moveStrafe, MovementUtil.mc.thePlayer.movementInput.moveForward);
    }

    public double getTickDist() {
        double xDist = MovementUtil.mc.thePlayer.posX - MovementUtil.mc.thePlayer.lastTickPosX;
        double zDist = MovementUtil.mc.thePlayer.posZ - MovementUtil.mc.thePlayer.lastTickPosZ;
        return Math.sqrt(Math.pow(xDist, 2.0) + Math.pow(zDist, 2.0));
    }
}

