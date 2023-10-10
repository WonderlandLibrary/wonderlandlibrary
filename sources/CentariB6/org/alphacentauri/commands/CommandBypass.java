package org.alphacentauri.commands;

import java.util.ArrayList;
import org.alphacentauri.AC;
import org.alphacentauri.management.bypass.AntiCheat;
import org.alphacentauri.management.commands.Command;
import org.alphacentauri.management.commands.ICommandHandler;
import org.alphacentauri.management.modules.Module;

public class CommandBypass implements ICommandHandler {
   public String getName() {
      return "Bypass";
   }

   public boolean execute(Command cmd) {
      String[] args = cmd.getArgs();
      if(args.length == 0) {
         AC.addChat(this.getName(), "Usage: bypass <ncp/aac/spartan/reflex> [module]");
         return true;
      } else if(args.length == 1) {
         AntiCheat target = null;

         for(AntiCheat antiCheat : AntiCheat.values()) {
            if(antiCheat.name().equalsIgnoreCase(args[0])) {
               target = antiCheat;
               break;
            }
         }

         if(target == null) {
            AC.addChat(this.getName(), "Unknown Anti Cheat " + args[0]);
            return true;
         } else {
            for(Module module : AC.getModuleManager().all()) {
               module.setBypass(target);
            }

            AC.addChat(this.getName(), "You are now bypassing " + target.name());
            return true;
         }
      } else if(args.length != 2) {
         return false;
      } else {
         Module mod = null;

         for(Module module : AC.getModuleManager().all()) {
            for(String s : module.getAliases()) {
               if(s.equalsIgnoreCase(args[1])) {
                  mod = module;
                  break;
               }
            }
         }

         AntiCheat target = null;

         for(AntiCheat antiCheat : AntiCheat.values()) {
            if(antiCheat.name().equalsIgnoreCase(args[0])) {
               target = antiCheat;
               break;
            }
         }

         if(mod == null) {
            AC.addChat(this.getName(), "Unknown Module " + args[1]);
            return true;
         } else if(target == null) {
            AC.addChat(this.getName(), "Unknown Anti Cheat " + args[0]);
            return true;
         } else {
            mod.setBypass(target);
            AC.addChat(this.getName(), mod.getName() + " is now bypassing " + target.name());
            return true;
         }
      }
   }

   public String[] getAliases() {
      return new String[]{"bypass"};
   }

   public String getDesc() {
      return "Loads configs for bypassing certain anticheats";
   }

   public ArrayList autocomplete(Command cmd) {
      String[] args = cmd.getArgs();
      ArrayList<String> ret = new ArrayList();
      if(args.length == 1) {
         for(AntiCheat antiCheat : AntiCheat.values()) {
            if(antiCheat.name().toLowerCase().startsWith(args[0].toLowerCase())) {
               ret.add(antiCheat.name());
            }
         }
      } else if(args.length == 2) {
         for(Module module : AC.getModuleManager().all()) {
            for(String alias : module.getAliases()) {
               if(alias.startsWith(args[1])) {
                  ret.add(alias);
                  break;
               }
            }
         }
      }

      return ret;
   }
}
