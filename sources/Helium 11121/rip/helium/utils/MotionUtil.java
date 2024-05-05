package rip.helium.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;
import rip.helium.event.minecraft.PlayerMoveEvent;

public class MotionUtil {

    static Minecraft mc;


    public static void setMoveSpeed(final PlayerMoveEvent event, final double speed) {
        double forward = MovementInput.moveForward;
        double strafe = MovementInput.moveStrafe;
        float yaw = mc.thePlayer.rotationYaw; //oh nigger
        if (forward == 0.0 && strafe == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
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
            event.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0f))
                    + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f)));
            event.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0f))
                    - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f)));
        }
    }

    public static double getBaseMoveSpeed() {
        double speed = 0.2873;
        if (mc.thePlayer != null && mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            final double amp = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
            speed *= 1.0 + 0.125 * (amp + 1.0);
        }
        return speed;
    }

    public static boolean isMoving() {
        return mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f;
    }

}
