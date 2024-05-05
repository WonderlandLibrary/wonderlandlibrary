package client.util;

import lombok.experimental.UtilityClass;
import net.minecraft.block.material.Material;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@UtilityClass
public class MoveUtil implements MinecraftInstance {

    public boolean isMoving() {
        return mc.thePlayer.moveStrafing != 0 || mc.thePlayer.moveForward != 0;
    }

    public double getSpeed() {
        return Math.hypot(mc.thePlayer.motionX, mc.thePlayer.motionZ);
    }

    public double getDirection() {
        float rotationYaw = mc.thePlayer.rotationYaw;
        if (mc.thePlayer.moveForward < 0) rotationYaw += 180;
        float forward = 1;
        if (mc.thePlayer.moveForward < 0) forward = -0.5F; else if (mc.thePlayer.moveForward > 0) forward = 0.5F;
        if (mc.thePlayer.moveStrafing > 0) rotationYaw -= 90 * forward;
        if (mc.thePlayer.moveStrafing < 0) rotationYaw += 90 * forward;
        return Math.toRadians(rotationYaw);
    }

    public float getYaw() {
        float rotationYaw = mc.thePlayer.rotationYaw;
        if (mc.thePlayer.moveForward < 0) rotationYaw += 180;
        float forward = 1;
        if (mc.thePlayer.moveForward < 0) forward = -0.5F; else if (mc.thePlayer.moveForward > 0) forward = 0.5F;
        if (mc.thePlayer.moveStrafing > 0) rotationYaw -= 90 * forward;
        if (mc.thePlayer.moveStrafing < 0) rotationYaw += 90 * forward;
        return rotationYaw;
    }

    public EnumFacing getFacing() {
        float rotationYaw = mc.thePlayer.rotationYaw;
        if (mc.thePlayer.moveForward < 0) rotationYaw += 180;
        float forward = 1;
        if (mc.thePlayer.moveForward < 0) forward = -0.5F; else if (mc.thePlayer.moveForward > 0) forward = 0.5F;
        if (mc.thePlayer.moveStrafing > 0) rotationYaw -= 90 * forward;
        if (mc.thePlayer.moveStrafing < 0) rotationYaw += 90 * forward;
        return EnumFacing.getHorizontal((int) Math.floor(rotationYaw * 4 / 360 + 0.5) & 3);
    }

    public void strafe() {
        strafe(getSpeed());
    }

    public void strafe(final double speed) {
        if (isMoving()) setSpeed(speed);
    }

    public void setSpeed(final double speed) {
        mc.thePlayer.motionX = -Math.sin(getDirection()) * speed;
        mc.thePlayer.motionZ = Math.cos(getDirection()) * speed;
    }

    public void stop() {
        mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;
    }

    public double getBaseSpeed() {
        double d = mc.thePlayer.isSprinting() ? 0.2873 : 0.221;
        if (mc.thePlayer.isPotionActive(1)) d *= 1 + 0.2F * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        return d;
    }
}
