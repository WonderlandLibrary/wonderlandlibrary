package me.aidanmees.trivia.client.tools;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

public class Player
{
  public static Minecraft mc = Minecraft.getMinecraft();
  
  public static double getDistanceToXYZ(double x, double y, double z)
  {
    double distX = mc.thePlayer.posX - x;
    double distY = mc.thePlayer.posY - y;
    double distZ = mc.thePlayer.posZ - z;
    
    double dist = Math.sqrt(Math.pow(distX, 2.0D) + Math.pow(distY, 2.0D) + Math.pow(distZ, 2.0D));
    
    return dist;
  }
  
  public static boolean isMoving()
  {
    if ((mc.gameSettings.keyBindForward.pressed) || (mc.gameSettings.keyBindBack.pressed) || (mc.gameSettings.keyBindLeft.pressed) || (mc.gameSettings.keyBindRight.pressed) || (mc.gameSettings.keyBindJump.pressed) || (mc.gameSettings.keyBindSneak.pressed)) {
      return true;
    }
    return false;
  }
}
