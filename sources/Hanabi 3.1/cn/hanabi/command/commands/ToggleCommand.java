package cn.hanabi.command.commands;

import cn.hanabi.command.Command;
import cn.hanabi.command.CommandException;
import cn.hanabi.modules.Mod;
import cn.hanabi.modules.ModManager;
import cn.hanabi.utils.ChatUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public class ToggleCommand extends Command {
   public ToggleCommand() {
      super("toggle", "t");
   }

   public void run(String alias, @NotNull String[] args) {
      if (args.length < 1) {
         throw new CommandException("Usage: ." + alias + " <module> [<on/off>]");
      } else {
         Mod mod = ModManager.getModule(args[0], false);
         if (mod == null) {
            throw new CommandException("The module '" + args[0] + "' does not exist");
         } else {
            boolean state = !mod.getState();
            if (args.length >= 2) {
               if (args[1].equalsIgnoreCase("on")) {
                  state = true;
               } else {
                  if (!args[1].equalsIgnoreCase("off")) {
                     throw new CommandException("Usage: ." + alias + " <module> <on/off>");
                  }

                  state = false;
               }
            }

            mod.setState(state);
            ChatUtils.success(mod.getName() + " was " + "§7" + (state ? "enabled" : "disabled"));
         }
      }
   }

   public List autocomplete(int arg, String[] args) {
      String prefix = "";
      boolean flag = false;

      try {
         if (arg == 0) {
            flag = true;
         } else if (arg == 1) {
            flag = true;
            prefix = args[0];
         }
      } catch (Exception var6) {
      }

      return (List)(flag ? (List)ModManager.getModules().stream().map(Mod::getName).filter((name) -> {
         return name.toLowerCase().startsWith(prefix);
      }).collect(Collectors.toList()) : new ArrayList());
   }
}
