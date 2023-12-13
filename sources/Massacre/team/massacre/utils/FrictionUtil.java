package team.massacre.utils;

import net.minecraft.client.Minecraft;

public class FrictionUtil {
   public static Minecraft mc = Minecraft.getMinecraft();

   public static float applyFriction(float speed) {
      float percent = 0.0F;
      if (mc.thePlayer.isMoving() && mc.thePlayer.onGround) {
         percent = 99.5F;
      }

      if (mc.thePlayer.isMoving() && !mc.thePlayer.onGround) {
         percent = 98.0F;
      }

      if (mc.thePlayer.isMoving() && mc.thePlayer.isInWater()) {
         percent = 80.3F;
      }

      float value = speed / 100.0F * percent;
      return value;
   }

   public static float applyCustomFriction(float speed, float friction) {
      float value = speed / 100.0F * friction;
      return value;
   }
}
