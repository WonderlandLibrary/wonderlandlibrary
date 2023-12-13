package team.massacre.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class Logger {
   private static Minecraft mc = Minecraft.getMinecraft();

   public static void output(String string) {
      System.out.println(string);
   }

   public static void print(String string) {
      mc.thePlayer.addChatMessage(new ChatComponentText("§8<§bMassacre§8>§r " + string));
   }
}
