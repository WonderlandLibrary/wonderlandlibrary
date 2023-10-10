package org.alphacentauri.commands;

import java.util.ArrayList;
import org.alphacentauri.AC;
import org.alphacentauri.management.commands.Command;
import org.alphacentauri.management.commands.ICommandHandler;

public class CommandSay implements ICommandHandler {
   public String getName() {
      return "Say";
   }

   public boolean execute(Command cmd) {
      String[] args = cmd.getArgs();
      if(args.length == 0) {
         AC.addChat(this.getName(), "Usage: say <chat_msg>");
      } else {
         StringBuilder sb = new StringBuilder();

         for(String arg : args) {
            sb.append(arg).append(' ');
         }

         sb.deleteCharAt(sb.length() - 1);
         AC.getMC().getPlayer().sendChatMessage(sb.toString());
      }

      return true;
   }

   public String[] getAliases() {
      return new String[]{"say"};
   }

   public String getDesc() {
      return "Sends the given message";
   }

   public ArrayList autocomplete(Command cmd) {
      return new ArrayList();
   }
}
