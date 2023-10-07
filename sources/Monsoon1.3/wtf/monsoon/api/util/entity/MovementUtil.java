package wtf.monsoon.api.util.entity;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;

public class MovementUtil {
	
	public static Minecraft mc = Minecraft.getMinecraft();
	
	public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2875D;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed))
            baseSpeed *= 1.0D + 0.2D * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        return baseSpeed;
    }

    public static float getSpeed() {
        return (float) Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
    }

    public static boolean isMoving() {
        return mc.thePlayer.movementInput.moveForward != 0.0F || mc.thePlayer.movementInput.moveStrafe != 0.0F;
    }

    public static boolean isBlockUnderneath(BlockPos pos) {
        for (int k = 0; k < pos.getY() + 1; k++) {
            if (mc.theWorld.getBlockState(new BlockPos(pos.getX(), k, pos.getZ())).getBlock().getMaterial() != Material.air)
                return true;
        }
        return false;
    }

    public static double getPosForSetPosX(double value) {
        double yaw = Math.toRadians(Minecraft.getMinecraft().thePlayer.rotationYaw);
        double x = -Math.sin(yaw) * value;
        return x;
    }

    public static double getPosForSetPosZ(double value) {
        double yaw = Math.toRadians(Minecraft.getMinecraft().thePlayer.rotationYaw);
        double z = Math.cos(yaw) * value;
        return z;
    }

    public static double getJumpHeight() {
        double baseJumpHeight = 0.41999998688697815D;
        if (mc.thePlayer.isInLava() || mc.thePlayer.isInWater())
        return 0.13500000163912773D;
        if (mc.thePlayer.isPotionActive(Potion.jump)) {
            return baseJumpHeight + (mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1.0F) * 0.1F;
        }
        return baseJumpHeight;
	}

    public float getDirection() {
        float yaw = this.mc.thePlayer.rotationYaw;
        if (this.mc.thePlayer.moveForward < 0.0F) {
            yaw += 180.0F;
        }

        float forward = 1.0F;
        if (this.mc.thePlayer.moveForward < 0.0F) {
            forward = -0.5F;
        } else if (this.mc.thePlayer.moveForward > 0.0F) {
            forward = 0.5F;
        }

        if (this.mc.thePlayer.moveStrafing > 0.0F) {
            yaw -= 90.0F * forward;
        }

        if (this.mc.thePlayer.moveStrafing < 0.0F) {
            yaw += 90.0F * forward;
        }

        yaw *= 0.017453292F;
        return yaw;
    }

    /*public static void setSpeed(final EventMotion moveEvent, final double moveSpeed) {
        setSpeed(moveEvent, moveSpeed, mc.thePlayer.rotationYaw, mc.thePlayer.movementInput.moveStrafe, mc.thePlayer.movementInput.moveForward);
    }

    public static void setSpeed(final EventMotion moveEvent, final double moveSpeed, final float pseudoYaw, final double pseudoStrafe, final double pseudoForward) {
        double forward = pseudoForward;
        double strafe = pseudoStrafe;
        float yaw = pseudoYaw;

        if (forward != 0.0) {
            if (strafe > 0.0) {
                yaw += ((forward > 0.0) ? -45 : 45);
            } else if (strafe < 0.0) {
                yaw += ((forward > 0.0) ? 45 : -45);
            }
            strafe = 0.0F;
            if (forward > 0.0) {
                forward = 1F;
            } else if (forward < 0.0) {
                forward = -1F;
            }
        }

        if (strafe > 0.0) {
            strafe = 1F;
        } else if (strafe < 0.0) {
            strafe = -1F;
        }
        double mx = Math.cos(Math.toRadians((yaw + 90.0F)));
        double mz = Math.sin(Math.toRadians((yaw + 90.0F)));
        moveEvent.x = (forward * moveSpeed * mx + strafe * moveSpeed * mz);
        moveEvent.z = (forward * moveSpeed * mz - strafe * moveSpeed * mx);

    }*/

}
