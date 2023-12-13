package me.earth.earthhack.impl.util.minecraft;

import me.earth.earthhack.api.util.Globals;

public class MovementUtil implements Globals
{
    public static boolean isMoving()
    {
        return mc.player.moveForward != 0.0 || mc.player.moveStrafing != 0.0;
    }

    public static double[] strafe(double speed)
    {
        float moveForward = mc.player.movementInput.moveForward;
        float moveStrafe = mc.player.movementInput.moveStrafe;
        float rotationYaw = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * mc.getRenderPartialTicks();

        if (moveForward != 0.0f)
        {
            if (moveStrafe > 0.0f)
            {
                rotationYaw += ((moveForward > 0.0f) ? -45 : 45);
            }
            else if (moveStrafe < 0.0f)
            {
                rotationYaw += ((moveForward > 0.0f) ? 45 : -45);
            }
            moveStrafe = 0.0f;
            if (moveForward > 0.0f)
            {
                moveForward = 1.0f;
            }
            else if (moveForward < 0.0f)
            {
                moveForward = -1.0f;
            }
        }

        double posX = moveForward * speed * -Math.sin(Math.toRadians(rotationYaw)) + moveStrafe * speed * Math.cos(Math.toRadians(rotationYaw));
        double posZ = moveForward * speed * Math.cos(Math.toRadians(rotationYaw)) - moveStrafe * speed * -Math.sin(Math.toRadians(rotationYaw));

        return new double[] {posX, posZ};
    }

}
