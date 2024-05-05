package tech.drainwalk.utility.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import tech.drainwalk.utility.Utility;

import java.util.Objects;

public class MoveUtility extends Utility {

    public static Minecraft mc = Minecraft.getMinecraft();

    public static boolean isMoving() {
        return mc.gameSettings.keyBindForward.isPressed() || mc.gameSettings.keyBindRight.isPressed()
                || mc.gameSettings.keyBindLeft.isPressed()|| mc.gameSettings.keyBindBack.isPressed();
    }

    public static int getSpeedEffect() {
        if (mc.player.isPotionActive(MobEffects.SPEED)) {
            return Objects.requireNonNull(mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier() + 1;
        }
        return 0;
    }

    public static double getBaseSpeed() {
        double baseSpeed = 0.2873;
        if (mc.player.isPotionActive(Potion.getPotionById(1))) {
            int amplifier = mc.player.getActivePotionEffect(Potion.getPotionById(1)).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double) (amplifier + 1);
        }
        return baseSpeed;
    }

    public static double getSpeed() {
        return Math.sqrt(Math.pow(mc.player.motionX, 2) + Math.pow(mc.player.motionZ, 2));
    }

    public static float getDirection() {
        float rotationYaw = mc.player.rotationYaw;

        float factor = 0f;

        if (mc.player.movementInput.moveForward > 0)
            factor = 1;
        if (mc.player.movementInput.moveForward < 0)
            factor = -1;

        if (factor == 0) {
            if (mc.player.movementInput.moveStrafe > 0)
                rotationYaw -= 90;

            if (mc.player.movementInput.moveStrafe < 0)
                rotationYaw += 90;
        } else {
            if (mc.player.movementInput.moveStrafe > 0)
                rotationYaw -= 45 * factor;

            if (mc.player.movementInput.moveStrafe < 0)
                rotationYaw += 45 * factor;
        }

        if (factor < 0)
            rotationYaw -= 180;

        return (float) Math.toRadians(rotationYaw);
    }

    public static boolean isBlockAboveHead() {
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(mc.player.posX - 0.3, mc.player.posY + mc.player.getEyeHeight(),
                mc.player.posZ + 0.3, mc.player.posX + 0.3, mc.player.posY + (!mc.player.onGround ? 1.5 : 2.5),
                mc.player.posZ - 0.3);
        return !mc.world.getCollisionBoxes(mc.player, axisAlignedBB).isEmpty();
    }

    public static void setSpeed(float speed) {
        float yaw = mc.player.rotationYaw;
        float forward = mc.player.movementInput.moveForward;
        float strafe = mc.player.movementInput.moveStrafe;
        if (forward != 0) {
            if (strafe > 0) {
                yaw += (forward > 0 ? -45 : 45);
            } else if (strafe < 0) {
                yaw += (forward > 0 ? 45 : -45);
            }
            strafe = 0;
            if (forward > 0) {
                forward = 1;
            } else if (forward < 0) {
                forward = -1;
            }
        }
        mc.player.motionX = (forward * speed * Math.cos(Math.toRadians(yaw + 90))
                + strafe * speed * Math.sin(Math.toRadians(yaw + 90)));
        mc.player.motionZ = (forward * speed * Math.sin(Math.toRadians(yaw + 90))
                - strafe * speed * Math.cos(Math.toRadians(yaw + 90)));
    }

    public static double[] getSpeed(double speed) {
        float yaw = mc.player.rotationYaw;
        float forward = mc.player.movementInput.moveForward;
        float strafe = mc.player.movementInput.moveStrafe;
        if (forward != 0) {
            if (strafe > 0) {
                yaw += (forward > 0 ? -45 : 45);
            } else if (strafe < 0) {
                yaw += (forward > 0 ? 45 : -45);
            }
            strafe = 0;
            if (forward > 0) {
                forward = 1;
            } else if (forward < 0) {
                forward = -1;
            }
        }
        return new double[] {
                (forward * speed * Math.cos(Math.toRadians(yaw + 90))
                        + strafe * speed * Math.sin(Math.toRadians(yaw + 90))),
                (forward * speed * Math.sin(Math.toRadians(yaw + 90))
                        - strafe * speed * Math.cos(Math.toRadians(yaw + 90))),
                yaw };
    }


    public static void setStrafe(double speed) {
        if (!MoveUtility.isMoving()) {
            return;
        }
        double yaw = getDirection();
        mc.player.motionX = -Math.sin(yaw) * speed;
        mc.player.motionZ = Math.cos(yaw) * speed;
    }

    public static boolean isInLiquid() {
        return mc.player.isInWater() || mc.player.isInLava();
    }
}
