package my.NewSnake.Tank.command.commands;

import my.NewSnake.Tank.command.Com;
import my.NewSnake.Tank.command.Command;
import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.module.ModuleManager;
import my.NewSnake.utils.ClientUtils;

@Com(
   names = {"toggle", "t", "tog"}
)
public class Toggle extends Command {
   public void runCommand(String[] var1) {
      String var2 = "";
      if (var1.length > 1) {
         var2 = var1[1];
      }

      Module var3 = ModuleManager.getModule(var2);
      if (var3.getId().equalsIgnoreCase("null")) {
         ClientUtils.sendMessage("Invalid Module.");
      } else {
         var3.toggle();
         ClientUtils.sendMessage(String.valueOf(var3.getDisplayName()) + " is now " + (var3.isEnabled() ? "enabled" : "disabled"));
         ModuleManager.save();
      }
   }

   public String getHelp() {
      return "Toggle - toggle <t, tog> (module) - Toggles a module on or off";
   }
}
