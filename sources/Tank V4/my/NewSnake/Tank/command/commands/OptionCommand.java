package my.NewSnake.Tank.command.commands;

import my.NewSnake.Tank.command.Command;
import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.module.ModuleManager;
import my.NewSnake.Tank.option.Option;
import my.NewSnake.Tank.option.OptionManager;
import my.NewSnake.Tank.option.types.BooleanOption;
import my.NewSnake.Tank.option.types.NumberOption;
import my.NewSnake.utils.ClientUtils;

public class OptionCommand extends Command {
   public static String getHelpString() {
      return "Set option - (modname) (option name) <value>";
   }

   public void runCommand(String[] var1) {
      if (var1.length < 2) {
         ClientUtils.sendMessage(getHelpString());
      } else {
         Module var2 = ModuleManager.getModule(var1[0]);
         if (!var2.getId().equalsIgnoreCase("Null")) {
            Option var3 = OptionManager.getOption(var1[1], var2.getId());
            if (var3 instanceof BooleanOption) {
               BooleanOption var4 = (BooleanOption)var3;
               var4.setValue(!(Boolean)var4.getValue());
               ClientUtils.sendMessage(String.valueOf(var3.getDisplayName()) + " set to " + var3.getValue());
               OptionManager.save();
            } else if (var3 instanceof NumberOption) {
               try {
                  var3.setValue(Double.parseDouble(var1[2]));
                  ClientUtils.sendMessage(String.valueOf(var3.getDisplayName()) + " set to " + var1[2]);
               } catch (NumberFormatException var6) {
                  ClientUtils.sendMessage("Number option format error.");
               }

               OptionManager.save();
            } else {
               ClientUtils.sendMessage("Option not recognized.");
            }
         } else {
            ClientUtils.sendMessage(getHelpString());
         }

      }
   }
}
