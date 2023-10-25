package me.rhys.base.util;

import net.minecraft.client.Minecraft;

public class MoveUtils {
  private static Minecraft mc = Minecraft.getMinecraft();
  
  public static void teleportForward(double amount, TeleportMode teleportMode) {
    double yaw = mc.thePlayer.rotationYaw;
    yaw = Math.toRadians(yaw);
    double dX = -Math.sin(yaw) * amount;
    double dZ = Math.cos(yaw) * amount;
    if (teleportMode == TeleportMode.SET_POSITION) {
      mc.thePlayer.setPosition(mc.thePlayer.posX + dX, mc.thePlayer.posY, mc.thePlayer.posZ + dZ);
    } else if (teleportMode == TeleportMode.SET_POSITION_UPDATE) {
      mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX + dX, mc.thePlayer.posY, mc.thePlayer.posZ + dZ);
    } 
  }
  
  public static void setFriction(double speed) {
    double forward = mc.thePlayer.movementInput.moveForward;
    double strafe = mc.thePlayer.movementInput.moveStrafe;
    float yaw = mc.thePlayer.rotationYaw;
    if (forward == 0.0D && strafe == 0.0D) {
      mc.thePlayer.motionX = 0.0D;
      mc.thePlayer.motionZ = 0.0D;
    } else {
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
      double sin = Math.sin(Math.toRadians((yaw + 90.0F)));
      double cos = Math.cos(Math.toRadians((yaw + 90.0F)));
      mc.thePlayer.motionX = forward * speed * cos + strafe * speed * sin;
      mc.thePlayer.motionZ = forward * speed * sin - strafe * speed * cos;
    } 
  }
  
  public enum TeleportMode {
    SET_POSITION, SET_POSITION_UPDATE;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\bas\\util\MoveUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */