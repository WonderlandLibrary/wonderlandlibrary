package rip.autumn.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import rip.autumn.events.player.MoveEvent;

public final class MovementUtils {
   private static final Minecraft mc = Minecraft.getMinecraft();

   public static void setSpeed(double moveSpeed, float yaw, double strafe, double forward) {
      if (forward != 0.0D) {
         if (strafe > 0.0D) {
            yaw += ((forward > 0.0D) ? -45 : 45);
         } else if (strafe < 0.0D) {
            yaw += ((forward > 0.0D) ? 45 : -45);
         }
         strafe = 0.0D;
         if (forward > 0.0D) {
            forward = 1.0D;
         } else if (forward < 0.0D) {
            forward = -1.0D;
         }
      }
      if (strafe > 0.0D) {
         strafe = 1.0D;
      } else if (strafe < 0.0D) {
         strafe = -1.0D;
      }
      double mx = Math.cos(Math.toRadians((yaw + 90.0F)));
      double mz = Math.sin(Math.toRadians((yaw + 90.0F)));
      mc.thePlayer.motionX = forward * moveSpeed * mx + strafe * moveSpeed * mz;
      mc.thePlayer.motionZ = forward * moveSpeed * mz - strafe * moveSpeed * mx;
   }

   public static void setSpeed(double moveSpeed) {
      setSpeed(moveSpeed, mc.thePlayer.rotationYaw, mc.thePlayer.movementInput.moveStrafe, mc.thePlayer.movementInput.moveForward);
   }

   public static double getBaseMoveSpeed() {
      double baseSpeed = 0.2875D;
      if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
         baseSpeed *= 1.0D + 0.2D * (double)(mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
      }

      return baseSpeed;
   }

   public static double getJumpBoostModifier(double baseJumpHeight) {
      if (mc.thePlayer.isPotionActive(Potion.jump)) {
         int amplifier = mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier();
         baseJumpHeight += (double)((float)(amplifier + 1) * 0.1F);
      }

      return baseJumpHeight;
   }
}
