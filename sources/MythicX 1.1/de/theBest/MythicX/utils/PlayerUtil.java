package de.theBest.MythicX.utils;

import de.theBest.MythicX.modules.hook.MinecraftHook;


public class PlayerUtil implements MinecraftHook {

    //Player dir formula, R = yaw, F = MoveForward, S = Strafe
    /*
    D(R, F, S) = R - 45 * ((F > 0) * (S > 0) - (F > 0) * (S < 0))
             + 45 * ((F < 0) * (S > 0) - (F < 0) * (S < 0))
             - 135 * ((F < 0) * (S > 0))
             + 135 * ((F < 0) * (S < 0))
             - 180 * ((F < 0) * (S == 0))
             - 90 * ((F == 0) * (S > 0))
             + 90 * ((F == 0) * (S < 0))
     */

    private static float getPlayerDirection() {
        if (mc.thePlayer == null) return 0;

        float direction = mc.thePlayer.rotationYaw;

        if (mc.thePlayer.moveForward > 0) {
            if (mc.thePlayer.moveStrafing > 0)
                direction -= 45;
            else if (mc.thePlayer.moveStrafing < 0)
                direction += 45;

        } else if (mc.thePlayer.moveForward < 0) {
            if (mc.thePlayer.moveStrafing > 0)
                direction -= 135;
            else if (mc.thePlayer.moveStrafing < 0)
                direction += 135;
            else
                direction -= 180;
        } else {
            if (mc.thePlayer.moveStrafing > 0)
                direction -= 90;
            else if (mc.thePlayer.moveStrafing < 0)
                direction += 90;
        }

        return direction;
    }

    public static void strafe(double speed) {
        if (mc.thePlayer == null) return;

        double direction = Math.toRadians(getPlayerDirection());

        if (isMoving()) {
            mc.thePlayer.motionX = getXDirection(direction, speed);
            mc.thePlayer.motionZ = getZDirection(direction, speed);
        } else {
            mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;
        }
    }

    private static double getXDirection(final double direction, final double speed) {
        return -Math.sin(direction) * speed;
    }

    private static double getZDirection(final double direction, final double speed) {
        return Math.cos(direction) * speed;
    }

    public static double getHorizontalMotion() {
        if (mc.thePlayer == null) return 0;
        return Math.hypot(mc.thePlayer.motionX, mc.thePlayer.motionZ);
    }

    public static boolean isMoving() {
        if (mc.thePlayer == null) return false;
        return mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0;
    }
}
