package space.lunaclient.luna.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class ChatUtils
{
  public static String chatHistoryString;
  public static boolean doesExist;
  private static int delay = 100;
  
  public ChatUtils() {}
  
  public static void printMessage(String requestMSG, String printMSG)
  {
    Minecraft mc = Minecraft.getMinecraft();
    if (chatHistoryString.contains(requestMSG))
    {
      doesExist = true;
      if ((doesExist & Minecraft.thePlayer.ticksExisted % 5 == 0)) {
        Minecraft.thePlayer.sendChatMessage(printMSG);
      }
      doesExist = false;
    }
  }
  
  public static void printMessage(String requestMSG, String printMSG, String secondMSG)
  {
    Minecraft mc = Minecraft.getMinecraft();
    if (chatHistoryString.contains(requestMSG))
    {
      doesExist = true;
      if ((doesExist & Minecraft.thePlayer.ticksExisted % 5 == 0)) {
        Minecraft.thePlayer.sendChatMessage(printMSG);
      }
      if ((doesExist & Minecraft.thePlayer.ticksExisted % 5 == 0)) {
        Minecraft.thePlayer.sendChatMessage(secondMSG);
      }
      doesExist = false;
    }
  }
}
