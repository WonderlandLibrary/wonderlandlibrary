package org.alphacentauri.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.alphacentauri.AC;
import org.alphacentauri.management.commands.Command;
import org.alphacentauri.management.commands.ICommandHandler;
import org.alphacentauri.management.modules.Module;

public class CommandToggle implements ICommandHandler {
   public String getName() {
      return "Toggle";
   }

   public boolean execute(Command cmd) {
      String[] args = cmd.getArgs();
      if(args.length == 0) {
         AC.addChat(this.getName(), "Usage: toggle <module> [on/off]");
      } else if(args.length == 1) {
         for(Module module : AC.getModuleManager().all()) {
            for(String alias : module.getAliases()) {
               if(alias.equalsIgnoreCase(args[0])) {
                  module.toggle();
                  return true;
               }
            }
         }
      } else if(args.length == 2) {
         if(!args[1].equalsIgnoreCase("on") && !args[1].equalsIgnoreCase("off")) {
            AC.addChat(this.getName(), "Expected on or off, got " + args[1]);
            return true;
         }

         for(Module module : AC.getModuleManager().all()) {
            for(String alias : module.getAliases()) {
               if(alias.equalsIgnoreCase(args[0])) {
                  module.setEnabled(args[1].equalsIgnoreCase("on"));
                  return true;
               }
            }
         }
      }

      return true;
   }

   public String[] getAliases() {
      return new String[]{"t", "toggle"};
   }

   public String getDesc() {
      return "Toggles a module";
   }

   public ArrayList autocomplete(Command cmd) {
      ArrayList<String> ret = new ArrayList();
      String[] args = cmd.getArgs();
      if(args.length == 1) {
         ArrayList<String> options = (ArrayList)AC.getModuleManager().all().stream().map((module) -> {
            return module.getAliases()[0];
         }).collect(Collectors.toCollection(ArrayList::<init>));
         ret.addAll((Collection)options.stream().filter((option) -> {
            return args[0].toLowerCase().startsWith(option.toLowerCase());
         }).collect(Collectors.toList()));
      } else if(args.length == 2) {
         String[] options = new String[]{"on", "off"};

         for(String option : options) {
            if(args[1].toLowerCase().startsWith(option.toLowerCase())) {
               ret.add(option);
            }
         }
      }

      return ret;
   }
}
