package rip.helium.utils;

import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovementInput;
import rip.helium.event.minecraft.PlayerMoveEvent;

public class MoveUtils {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void setSpeed(final PlayerMoveEvent moveEvent, final double moveSpeed) {
        setSpeed(moveEvent, moveSpeed, mc.thePlayer.rotationYaw, MovementInput.moveStrafe, MovementInput.moveForward);
    }

    public static void setSpeed(final PlayerMoveEvent moveEvent, final double moveSpeed, final float pseudoYaw, final double pseudoStrafe, final double pseudoForward) {
        double forward = pseudoForward;
        double strafe = pseudoStrafe;
        float yaw = pseudoYaw;

        if (forward == 0.0 && strafe == 0.0) {
            moveEvent.setZ(0);
            moveEvent.setX(0);
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
            final double sin = Math.sin(Math.toRadians(yaw + 90.0f));

            moveEvent.setX((forward * moveSpeed * cos + strafe * moveSpeed * sin));
            moveEvent.setZ((forward * moveSpeed * sin - strafe * moveSpeed * cos));
        }
    }

    public static boolean isBlockAbovePlayer() {
        return !(mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().maxY + 0.42F, mc.thePlayer.posZ)).getBlock() instanceof BlockAir);
    }

    /**
     * teleport local player relative to local players coordinates
     *
     * @param x x
     * @param y y
     * @param z z
     */
    public static void setPos(double x, double y, double z) {
        mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z);
    }

    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2875;

        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            baseSpeed *= 1 + .2 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }

        return baseSpeed;
    }

    /**
     * @param baseJumpHeight base jump height e.g. 0.42D
     * @return {@code baseJumpHeight} multiplied by jump boost modifier
     */
    public static double getJumpBoostModifier(double baseJumpHeight) {
        if (mc.thePlayer.isPotionActive(Potion.jump)) {
            int amplifier = mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier();
            baseJumpHeight += (float) (amplifier + 1) * 0.1F;
        }

        return baseJumpHeight;
    }

}

