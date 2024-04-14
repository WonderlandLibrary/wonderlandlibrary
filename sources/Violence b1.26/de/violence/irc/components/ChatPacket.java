package de.violence.irc.components;

import de.violence.Violence;
import de.violence.irc.IrcPacket;
import net.minecraft.client.Minecraft;

public class ChatPacket extends IrcPacket {
   public String getName() {
      return "c";
   }

   public void onReceivePacket(String s) {
      try {
         if(Minecraft.getMinecraft().theWorld != null) {
            String l = s.replace("&", "§");
            String userName = l.split("\\$")[0].replace(" ", "");
            String message = l.split("\\$")[1];
            Violence.getViolence().sendChat(userName + "§7: §f" + message);
         }

      } catch (Exception var5) {
         ;
      }
   }
}
