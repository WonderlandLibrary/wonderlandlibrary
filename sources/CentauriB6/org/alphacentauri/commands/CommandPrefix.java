package org.alphacentauri.commands;

import java.util.ArrayList;
import org.alphacentauri.AC;
import org.alphacentauri.management.commands.Command;
import org.alphacentauri.management.commands.ICommandHandler;

public class CommandPrefix implements ICommandHandler {
   public String getName() {
      return "Prefix";
   }

   public boolean execute(Command cmd) {
      String[] args = cmd.getArgs();
      if(args.length == 1) {
         String newPrefix = args[0];
         AC.getConfig().setPrefix(newPrefix);
         AC.addChat(this.getName(), "The command prefix is now \"" + newPrefix + "\"!");
         AC.addChat(this.getName(), "To change it again do: " + newPrefix + cmd.getCommand() + " <new_prefix>");
      } else if(args.length > 1) {
         AC.addChat(this.getName(), "The prefix may not contain spaces");
      } else {
         AC.addChat(this.getName(), "The current prefix is \"" + AC.getConfig().getPrefix() + "\"!");
         AC.addChat(this.getName(), "To change it do: " + AC.getConfig().getPrefix() + cmd.getCommand() + " <new_prefix>");
      }

      return true;
   }

   public String[] getAliases() {
      return new String[]{"prefix"};
   }

   public String getDesc() {
      return "Sets the command prefix";
   }

   public ArrayList autocomplete(Command cmd) {
      return new ArrayList();
   }
}
