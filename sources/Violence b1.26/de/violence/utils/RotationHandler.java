package de.violence.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;

public class RotationHandler {
   public static float server_yaw;
   public static float server_pitch;

   public static float getDistanceYaw() {
      return MathHelper.wrapAngleTo180_float(Minecraft.getMinecraft().thePlayer.rotationYaw) - server_yaw;
   }

   public static float getDistancePitch() {
      return MathHelper.wrapAngleTo180_float(Minecraft.getMinecraft().thePlayer.rotationPitch) - server_pitch;
   }
}
