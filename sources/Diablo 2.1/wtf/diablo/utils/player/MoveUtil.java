package wtf.diablo.utils.player;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;

public class MoveUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void setMotion(double speed) {
        float yaw = mc.thePlayer.rotationYaw;
        double forward = mc.thePlayer.moveForward;
        double strafe = mc.thePlayer.moveStrafing;
        if ((forward == 0.0D) && (strafe == 0.0D)) {
            mc.thePlayer.motionX = 0;
            mc.thePlayer.motionZ = 0;
        } else {
            if (forward != 0.0D) {
                if (strafe > 0.0D) {
                    yaw += (forward > 0.0D ? -45 : 45);
                } else if (strafe < 0.0D) {
                    yaw += (forward > 0.0D ? 45 : -45);
                }
                strafe = 0.0D;
                if (forward > 0.0D) {
                    forward = 1;
                } else if (forward < 0.0D) {
                    forward = -1;
                }
            }

            mc.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F));
            mc.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F));
        }
    }

    public static float getSpeedPotMultiplier(double multi) {
        float speedPotMultiplier = 1F;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amp = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            speedPotMultiplier = 1.0F + (float)multi * (amp + 1);
        }
        return speedPotMultiplier;
    }

    public static float getSpeed() {
        return (float) Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
    }

    public static float getDirection() {
        float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
        final float forward = Minecraft.getMinecraft().thePlayer.moveForward;
        final float strafe = Minecraft.getMinecraft().thePlayer.moveStrafing;
        yaw += ((forward < 0.0f) ? 180 : 0);
        final int i = (forward < 0.0f) ? -45 : ((forward == 0.0f) ? 90 : 45);
        if (strafe < 0.0f) {
            yaw += i;
        }
        if (strafe > 0.0f) {
            yaw -= i;
        }
        return yaw * 0.017453292f;
    }

    public static double getBaseMoveSpeed() {
        double baseSpeed = Minecraft.getMinecraft().thePlayer.capabilities.getWalkSpeed() * 2.925;
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSlowdown)) {
            baseSpeed /= 1.0 + 0.15 * (Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSlowdown).getAmplifier() + 1);
        }
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
            baseSpeed *= 1.0 + 0.2 * (Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return baseSpeed;
    }
    public static boolean isOnGroundAndNotMoving() {
        return mc.thePlayer.onGround && mc.thePlayer.moveForward > 0;
    }

    public static boolean isMoving() {
        return mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0;
    }

    public static float getSpeedMotion() {
        return (float) Math.sqrt(Math.pow(mc.thePlayer.motionX, 2) + Math.pow(mc.thePlayer.motionZ, 2));
    }
    public static void strafe(double speed) {
        if (!isMoving()) {
            mc.thePlayer.motionX = (0.0F);
            mc.thePlayer.motionZ = (0.0F);
        } else {
            double direction = getDirection();
            mc.thePlayer.motionX = (Math.sin(direction) * -speed);
            mc.thePlayer.motionZ = (Math.cos(direction) * speed);
        }
    }
    public static float getBaseSpeed() {
        float baseSpeed = 0.2873F;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amp = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0F + 0.2F * (amp + 1);
        }
        return baseSpeed;
    }
}
