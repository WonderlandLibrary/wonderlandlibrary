package wtf.dawn.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import org.lwjgl.util.vector.Vector2f;

import static java.lang.Math.sqrt;
import static net.minecraft.potion.Potion.moveSpeed;

public class MovementUtil {

    protected static Minecraft mc;

    public static boolean isMoving() {
        if (Minecraft.getMinecraft() != null) {
            return Minecraft.getMinecraft().thePlayer.moveForward == 0 && Minecraft.getMinecraft().thePlayer.moveStrafing == 0;
        } else {
            return false;
        }
    }

    public static boolean isMovingWithKeys() {
        return Minecraft.getMinecraft().gameSettings.keyBindForward.isPressed() || Minecraft.getMinecraft().gameSettings.keyBindBack.isPressed() || Minecraft.getMinecraft().gameSettings.keyBindLeft.isPressed() || Minecraft.getMinecraft().gameSettings.keyBindRight.isPressed();
    }

    public static double getDefaultSpeed() {
        double baseSpeed = (double)Minecraft.getMinecraft().thePlayer.capabilities.getWalkSpeed() * 2.873;
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSlowdown)) {
            baseSpeed /= 1.0 + 0.2 * (double)(Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSlowdown).getAmplifier() + 1);
        }

        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
            baseSpeed *= 1.0 + 0.2 * (double)(Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }

        return baseSpeed;
    }

    public static float getMoveYaw(float yaw) {
        Vector2f from = new Vector2f((float)Minecraft.getMinecraft().thePlayer.lastTickPosX, (float)Minecraft.getMinecraft().thePlayer.lastTickPosZ);
        Vector2f to = new Vector2f((float)Minecraft.getMinecraft().thePlayer.posX, (float)Minecraft.getMinecraft().thePlayer.posZ);
        Vector2f diff = new Vector2f(to.x - from.x, to.y - from.y);
        double x = (double)diff.x;
        double z = (double)diff.y;
        if (x != 0.0 || z != 0.0) {
            yaw = (float)Math.toDegrees((Math.atan2(-x, z) + (double) MathHelper.PI2) % (double)MathHelper.PI2);
        }

        return yaw;
    }

    public static double getDirection() {
        float rotationYaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
        if (Minecraft.getMinecraft().thePlayer.moveForward < 0.0F)
            rotationYaw += 180.0F;
        float forward = 1.0F;
        if (Minecraft.getMinecraft().thePlayer.moveForward < 0.0F) {
            forward = -0.5F;
        } else if (Minecraft.getMinecraft().thePlayer.moveForward > 0.0F) {
            forward = 0.5F;
        }
        if (Minecraft.getMinecraft().thePlayer.moveStrafing > 0.0F)
            rotationYaw -= 90.0F * forward;
        if (Minecraft.getMinecraft().thePlayer.moveStrafing < 0.0F)
            rotationYaw += 90.0F * forward;
        return Math.toRadians(rotationYaw);
    }
    public static void strafe(double speed) {
                    if(isMovingWithKeys()) {
                        double yaw = getDirection();
                        Minecraft.getMinecraft().thePlayer.motionX = -MathHelper.sin((float) yaw) * speed;
                        Minecraft.getMinecraft().thePlayer.motionZ = MathHelper.cos((float) yaw) * speed;
                    }
            }



    public static double getSpeed() {
        return sqrt(Minecraft.getMinecraft().thePlayer.motionX * Minecraft.getMinecraft().thePlayer.motionX + Minecraft.getMinecraft().thePlayer.motionZ * Minecraft.getMinecraft().thePlayer.motionZ);
    }


}
