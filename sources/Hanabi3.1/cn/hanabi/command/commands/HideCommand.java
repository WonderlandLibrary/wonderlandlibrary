package cn.hanabi.command.commands;

import cn.hanabi.command.Command;
import cn.hanabi.command.CommandException;
import cn.hanabi.modules.Mod;
import cn.hanabi.modules.ModManager;
import com.darkmagician6.eventapi.EventManager;
import java.util.ArrayList;
import java.util.List;

public class HideCommand extends Command {
   public HideCommand() {
      super("hide");
      EventManager.register(this);
   }

   public void run(String alias, String[] args) {
      if (args.length == 0) {
         throw new CommandException("Usage: ." + alias + " <module> <true , false>");
      } else {
         Mod mod = ModManager.getModule(args[0], false);
         if (mod == null) {
            throw new CommandException("The module '" + args[0] + "' does not exist");
         } else if (args.length > 1) {
            if (args[1].equalsIgnoreCase("true")) {
               mod.setHidden(true);
            } else {
               if (!args[1].equalsIgnoreCase("false")) {
                  throw new CommandException("true or false");
               }

               mod.setHidden(false);
            }

         } else {
            mod.setHidden(true);
         }
      }
   }

   public List autocomplete(int arg, String[] args) {
      return new ArrayList();
   }
}
