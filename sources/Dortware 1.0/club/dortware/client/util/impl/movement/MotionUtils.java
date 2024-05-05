package club.dortware.client.util.impl.movement;

import club.dortware.client.event.impl.MovementEvent;
import club.dortware.client.util.Util;
import club.dortware.client.util.impl.networking.PacketUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;

public final class MotionUtils implements Util {

    public static void damage() {
        if (mc.thePlayer.onGround) {
            double x = mc.thePlayer.posX;
            double y = mc.thePlayer.posY;
            double z = mc.thePlayer.posZ;
            for (int i = 0; i < 9; i++) {
                PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.42F, z, false));
                PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1.23E-6, z, false));
                PacketUtil.sendPacket(new C03PacketPlayer(false));
            }
            PacketUtil.sendPacket(new C03PacketPlayer(true));
        }
    }

    public static double getMotion(float baseMotionY) {
        Potion potion = Potion.jump;

        if (mc.thePlayer.isPotionActive(potion)) {
            int amplifier = mc.thePlayer.getActivePotionEffect(potion).getAmplifier();
            baseMotionY += (amplifier + 1) * 0.1F;
        }

        return baseMotionY;
    }

    /**
     * @param event
     * @param moveSpeed
     * @author aristhena
     */
    public static void setMotion(MovementEvent event, float moveSpeed) {
        MovementInput movementInput = mc.thePlayer.movementInput;
        double moveForward = movementInput.moveForward;
        double moveStrafe = movementInput.moveStrafe;
        double rotationYaw = mc.thePlayer.rotationYaw;
        if (moveForward == 0.0D && moveStrafe == 0.0D) {
            event.setMotionX(0.0D);
            event.setMotionZ(0.0D);
        } else {
            if (moveStrafe > 0) {
                moveStrafe = 1;
            } else if (moveStrafe < 0) {
                moveStrafe = -1;
            }
            if (moveForward != 0.0D) {
                if (moveStrafe > 0.0D) {
                    rotationYaw += (moveForward > 0.0D ? -45 : 45);
                } else if (moveStrafe < 0.0D) {
                    rotationYaw += (moveForward > 0.0D ? 45 : -45);
                }
                moveStrafe = 0.0D;
                if (moveForward > 0.0D) {
                    moveForward = 1.0D;
                } else if (moveForward < 0.0D) {
                    moveForward = -1.0D;
                }
            }
            double cos = Math.cos(Math.toRadians(rotationYaw + 90.0F));
            double sin = Math.sin(Math.toRadians(rotationYaw + 90.0F));
            event.setMotionX(moveForward * moveSpeed * cos
                    + moveStrafe * moveSpeed * sin);
            event.setMotionZ(moveForward * moveSpeed * sin
                    - moveStrafe * moveSpeed * cos);
        }
    }

    public static void setMotion(float moveSpeed) {
        MovementInput movementInput = mc.thePlayer.movementInput;
        double moveForward = movementInput.moveForward;
        double moveStrafe = movementInput.moveStrafe;
        double rotationYaw = mc.thePlayer.rotationYaw;
        if (moveForward == 0.0D && moveStrafe == 0.0D) {
            mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;
        } else {
            if (moveStrafe > 0) {
                moveStrafe = 1;
            } else if (moveStrafe < 0) {
                moveStrafe = -1;
            }
            if (moveForward != 0.0D) {
                if (moveStrafe > 0.0D) {
                    rotationYaw += (moveForward > 0.0D ? -45 : 45);
                } else if (moveStrafe < 0.0D) {
                    rotationYaw += (moveForward > 0.0D ? 45 : -45);
                }
                moveStrafe = 0.0D;
                if (moveForward > 0.0D) {
                    moveForward = 1.0D;
                } else if (moveForward < 0.0D) {
                    moveForward = -1.0D;
                }
            }
            double cos = Math.cos(Math.toRadians(rotationYaw + 90.0F));
            double sin = Math.sin(Math.toRadians(rotationYaw + 90.0F));
            mc.thePlayer.motionX = moveForward * moveSpeed * cos
                    + moveStrafe * moveSpeed * sin;
            mc.thePlayer.motionZ = moveForward * moveSpeed * sin
                    - moveStrafe * moveSpeed * cos;
        }
    }

    /**
     * Gets the movement speed of the {@code EntityPlayerSP}
     * @return the base speed
     */
    public static float getBaseSpeed() {
        float baseSpeed = 0.2873F;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amp = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0F + 0.2F * (amp + 1);
        }
        return baseSpeed;
    }
}