package de.violence.command.cmds;

import de.violence.Violence;
import de.violence.command.Command;
import net.minecraft.client.Minecraft;

public class Info extends Command {
   public String getDescription() {
      return "Send infos in the chat.";
   }

   public String getName() {
      return "info";
   }

   public String getUsage() {
      return ".info";
   }

   public void onCommand(String[] args) {
      (new Thread(new Runnable() {
         public void run() {
            try {
               Minecraft.getMinecraft().thePlayer.sendChatMessage(Violence.NAME.replace("i", "�").replace("ν", "ʋ") + ": Coder: StaticCode & LiquidDev");
               Thread.sleep(2200L);
               Minecraft.getMinecraft().thePlayer.sendChatMessage("h@ze team");
            } catch (Exception var2) {
               ;
            }

         }
      })).start();
   }
}
