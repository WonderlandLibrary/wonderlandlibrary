package de.tired.util.player;

import de.tired.base.event.events.EventStrafe;
import de.tired.base.interfaces.IHook;
import net.minecraft.util.MathHelper;

public class StrafeUtil implements IHook {

    public static void customSilentMoveFlying(EventStrafe event, float yaw) {
        final float[] movementValues = getSilentMovementValues(yaw);
        float forward = movementValues[0], strafe = movementValues[1];
        if (forward < 0) {
            MC.thePlayer.setSprinting(false);
            MC.gameSettings.keyBindInventory.pressed = false;
        }
        event.forward = forward;
        event.strafe = strafe;
        {
            float f = strafe * strafe + forward * forward;

            if (f >= 1.0E-4F) {
                f = MathHelper.sqrt_float(f);

                if (f < 1.0F) {
                    f = 1.0F;
                }

                f = event.friction / f;
                strafe = strafe * f;
                forward = forward * f;
                float f1 = MathHelper.sin(yaw * (float) Math.PI / 180.0F);
                float f2 = MathHelper.cos(yaw * (float) Math.PI / 180.0F);
                MC.thePlayer.motionX +=  (strafe * f2 - forward * f1);
                MC.thePlayer.motionZ +=  (forward * f2 + strafe * f1);
            }
        }
        event.setCancelled(true);

    }

    public static float[] getSilentMovementValues(float yaw) {
        float diff = (MC.thePlayer.rotationYaw - yaw);
        float f = MathHelper.sin(diff * ((float) Math.PI / 180F));
        float f1 = MathHelper.cos(diff * ((float) Math.PI / 180F));
        float multiplier = 1f;
        if (MC.thePlayer.isSneaking())
            multiplier = 10;
        float forward = (float) (Math.round((MC.thePlayer.moveForward * (double) f1 + MC.thePlayer.moveStrafing * (double) f) * multiplier)) / multiplier;
        float strafe = (float) (Math.round((MC.thePlayer.moveStrafing * (double) f1 - MC.thePlayer.moveForward * (double) f) * multiplier)) / multiplier;
        forward *= 0.98;
        strafe *= 0.98;
        return new float[] { forward, strafe };
    }


}
