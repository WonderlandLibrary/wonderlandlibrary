package me.rhys.base.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class StrafeUtil {
  Minecraft mc = Minecraft.getMinecraft();
  
  public static void strafe(Double speed) {
    Minecraft mc = Minecraft.getMinecraft();
    if (!isMoving())
      return; 
    float yaw = (float)getDirection();
    EntityPlayerSP thePlayer = mc.thePlayer;
    thePlayer.motionX = -Math.sin(yaw) * speed.doubleValue();
    thePlayer.motionZ = Math.cos(yaw) * speed.doubleValue();
  }
  
  public static double getDirection() {
    Minecraft mc = Minecraft.getMinecraft();
    EntityPlayerSP thePlayer = mc.thePlayer;
    Float rotationYaw = Float.valueOf(thePlayer.rotationYaw);
    if (thePlayer.moveForward < 0.0F)
      rotationYaw = Float.valueOf(rotationYaw.floatValue() + 180.0F); 
    Float forward = Float.valueOf(1.0F);
    if (thePlayer.moveForward < 0.0F) {
      forward = Float.valueOf(-0.5F);
    } else if (thePlayer.moveForward > 0.0F) {
      forward = Float.valueOf(0.5F);
    } 
    if (thePlayer.moveStrafing > 0.0F)
      rotationYaw = Float.valueOf(rotationYaw.floatValue() - 90.0F * forward.floatValue()); 
    if (thePlayer.moveStrafing < 0.0F)
      rotationYaw = Float.valueOf(rotationYaw.floatValue() + 90.0F * forward.floatValue()); 
    return Math.toRadians(rotationYaw.floatValue());
  }
  
  public static boolean isMoving() {
    Minecraft mc = Minecraft.getMinecraft();
    if (mc.thePlayer == null || mc.thePlayer.movementInput == null)
      return false; 
    return (mc.thePlayer.movementInput.moveForward != 0.0F || mc.thePlayer.movementInput.moveStrafe != 0.0F);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\bas\\util\StrafeUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */