package org.alphacentauri.commands;

import java.util.ArrayList;
import org.alphacentauri.core.CloudAPI;
import org.alphacentauri.management.commands.Command;
import org.alphacentauri.management.commands.ICommandHandler;

public class CommandIRC implements ICommandHandler {
   public String getName() {
      return "IRC";
   }

   public boolean execute(Command cmd) {
      String[] args = cmd.getArgs();
      if(args.length == 0) {
         return true;
      } else {
         StringBuilder sb = new StringBuilder();

         for(String arg : args) {
            sb.append(arg).append(' ');
         }

         sb.deleteCharAt(sb.length() - 1);
         CloudAPI.sendIRC(sb.toString().replaceAll("ö", "oe").replaceAll("ü", "ue").replaceAll("ä", "ae").replaceAll("ß", "ss").replaceAll("§", "&"));
         return true;
      }
   }

   public String[] getAliases() {
      return new String[]{"irc", "i"};
   }

   public String getDesc() {
      return "Chat with other people";
   }

   public ArrayList autocomplete(Command cmd) {
      return new ArrayList();
   }
}
