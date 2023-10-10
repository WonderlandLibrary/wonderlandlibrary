package org.alphacentauri.commands;

import java.util.ArrayList;
import org.alphacentauri.AC;
import org.alphacentauri.management.commands.Command;
import org.alphacentauri.management.commands.ICommandHandler;
import org.alphacentauri.management.util.StringUtils;

public class CommandDesign implements ICommandHandler {
   public String getName() {
      return "Design";
   }

   public boolean execute(Command cmd) {
      String[] args = cmd.getArgs();
      if(args.length == 2 && args[0].equalsIgnoreCase("invtransparency")) {
         AC.getConfig().setInventoryTransparent(Boolean.parseBoolean(args[1]));
      } else {
         AC.addChat(this.getName(), "Usage: design <<invtransparency> <true/false>>");
      }

      return true;
   }

   public String[] getAliases() {
      return new String[]{"design"};
   }

   public String getDesc() {
      return "Let\'s you change some design options";
   }

   public ArrayList autocomplete(Command cmd) {
      String[] args = cmd.getArgs();
      ArrayList<String> ret = new ArrayList();
      if(args.length == 1) {
         ret.addAll(StringUtils.autocomplete(args[0], new String[]{"invtransparency"}));
      }

      return ret;
   }
}
