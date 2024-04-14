package de.violence.command.cmds;

import de.violence.command.Command;
import net.minecraft.client.Minecraft;

public class Say extends Command {
   public String getDescription() {
      return "Send messages.";
   }

   public String getName() {
      return "say";
   }

   public String getUsage() {
      return ".say <Message>";
   }

   public void onCommand(String[] args) {
      StringBuilder stringBuilder = new StringBuilder();

      for(int i = 0; i < args.length; ++i) {
         stringBuilder.append(args[i] + " ");
      }

      Minecraft.getMinecraft().thePlayer.sendChatMessage(stringBuilder.toString() + "§");
   }
}
