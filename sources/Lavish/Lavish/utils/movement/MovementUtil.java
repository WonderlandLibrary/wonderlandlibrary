// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.utils.movement;

import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.client.Minecraft;

public class MovementUtil
{
    protected static Minecraft mc;
    
    static {
        MovementUtil.mc = Minecraft.getMinecraft();
    }
    
    public static boolean isMovingOnGround() {
        return isMoving() && MovementUtil.mc.thePlayer.onGround;
    }
    
    public static float getRetarded() {
        return 0.2873f;
    }
    
    public static void sendPosition(final double x, final double y, final double z, final boolean ground, final boolean moving) {
        if (!moving) {
            MovementUtil.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(MovementUtil.mc.thePlayer.posX, MovementUtil.mc.thePlayer.posY + y, MovementUtil.mc.thePlayer.posZ, ground));
        }
        else {
            MovementUtil.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(MovementUtil.mc.thePlayer.posX + x, MovementUtil.mc.thePlayer.posY + y, MovementUtil.mc.thePlayer.posZ + z, ground));
        }
    }
    
    public static Block getBlockAtPos(final BlockPos inBlockPos) {
        final IBlockState s = MovementUtil.mc.theWorld.getBlockState(inBlockPos);
        return s.getBlock();
    }
    
    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2875;
        if (MovementUtil.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            baseSpeed *= 1.0 + 0.2 * (MovementUtil.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return baseSpeed;
    }
    
    public static boolean isMoving() {
        return MovementUtil.mc.thePlayer != null && (MovementUtil.mc.thePlayer.movementInput.moveForward != 0.0f || MovementUtil.mc.thePlayer.movementInput.moveStrafe != 0.0f);
    }
    
    public static double defaultMoveSpeed() {
        return MovementUtil.mc.thePlayer.isSprinting() ? 0.28700000047683716 : 0.22300000488758087;
    }
    
    public static double getLastDistance() {
        return Math.hypot(MovementUtil.mc.thePlayer.posX - MovementUtil.mc.thePlayer.prevPosX, MovementUtil.mc.thePlayer.posZ - MovementUtil.mc.thePlayer.prevPosZ);
    }
    
    public static boolean isOnGround(final double height) {
        return !MovementUtil.mc.theWorld.getCollidingBoundingBoxes(MovementUtil.mc.thePlayer, MovementUtil.mc.thePlayer.getEntityBoundingBox().offset(0.0, -height, 0.0)).isEmpty();
    }
    
    public static double jumpHeight() {
        if (MovementUtil.mc.thePlayer.isPotionActive(Potion.jump)) {
            return 0.419999986886978 + 0.1 * (MovementUtil.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1);
        }
        return 0.419999986886978;
    }
    
    public static double getJumpBoostModifier(double baseJumpHeight) {
        if (MovementUtil.mc.thePlayer.isPotionActive(Potion.jump)) {
            final int amplifier = MovementUtil.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier();
            baseJumpHeight += (amplifier + 1) * 0.1f;
        }
        return baseJumpHeight;
    }
    
    public static int getSpeedEffect() {
        if (MovementUtil.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            return MovementUtil.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
        }
        return 0;
    }
    
    public static void setSpeed(final double moveSpeed, float yaw, double strafe, double forward) {
        final double fforward = forward;
        final double sstrafe = strafe;
        final float yyaw = yaw;
        if (forward != 0.0) {
            if (strafe > 0.0) {
                yaw += ((forward > 0.0) ? -45 : 45);
            }
            else if (strafe < 0.0) {
                yaw += ((forward > 0.0) ? 45 : -45);
            }
            strafe = 0.0;
            if (forward > 0.0) {
                forward = 1.0;
            }
            else if (forward < 0.0) {
                forward = -1.0;
            }
        }
        if (strafe > 0.0) {
            strafe = 1.0;
        }
        else if (strafe < 0.0) {
            strafe = -1.0;
        }
        final double mx = Math.cos(Math.toRadians(yaw + 90.0f));
        final double mz = Math.sin(Math.toRadians(yaw + 90.0f));
        MovementUtil.mc.thePlayer.motionX = forward * moveSpeed * mx + strafe * moveSpeed * mz;
        MovementUtil.mc.thePlayer.motionZ = forward * moveSpeed * mz - strafe * moveSpeed * mx;
    }
    
    public static float getSpeed() {
        return (float)Math.sqrt(MovementUtil.mc.thePlayer.motionX * MovementUtil.mc.thePlayer.motionX + MovementUtil.mc.thePlayer.motionZ * MovementUtil.mc.thePlayer.motionZ);
    }
    
    public static void setSpeed(final double moveSpeed) {
        setSpeed(moveSpeed, MovementUtil.mc.thePlayer.rotationYaw, MovementUtil.mc.thePlayer.movementInput.moveStrafe, MovementUtil.mc.thePlayer.movementInput.moveForward);
    }
    
    public double getTickDist() {
        final double xDist = MovementUtil.mc.thePlayer.posX - MovementUtil.mc.thePlayer.lastTickPosX;
        final double zDist = MovementUtil.mc.thePlayer.posZ - MovementUtil.mc.thePlayer.lastTickPosZ;
        return Math.sqrt(Math.pow(xDist, 2.0) + Math.pow(zDist, 2.0));
    }
    
    public static void setMotion(final double speed) {
        double forward = MovementUtil.mc.thePlayer.movementInput.moveForward;
        double strafe = MovementUtil.mc.thePlayer.movementInput.moveStrafe;
        float yaw = MovementUtil.mc.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            MovementUtil.mc.thePlayer.motionX = 0.0;
            MovementUtil.mc.thePlayer.motionZ = 0.0;
        }
        else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                }
                else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                }
                else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            MovementUtil.mc.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f));
            MovementUtil.mc.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f));
        }
    }
    
    public static void setMotion(final double speed, final float directionInYaw) {
        double forward = MovementUtil.mc.thePlayer.movementInput.moveForward;
        double strafe = MovementUtil.mc.thePlayer.movementInput.moveStrafe;
        float yaw = directionInYaw;
        if (forward == 0.0 && strafe == 0.0) {
            MovementUtil.mc.thePlayer.motionX = 0.0;
            MovementUtil.mc.thePlayer.motionZ = 0.0;
        }
        else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                }
                else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                }
                else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            MovementUtil.mc.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f));
            MovementUtil.mc.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f));
        }
    }
}
