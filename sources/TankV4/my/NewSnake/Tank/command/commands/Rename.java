package my.NewSnake.Tank.command.commands;

import my.NewSnake.Tank.command.Com;
import my.NewSnake.Tank.command.Command;
import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.module.ModuleManager;
import my.NewSnake.utils.ClientUtils;

@Com(
   names = {"rename", "rn"}
)
public class Rename extends Command {
   public void runCommand(String[] var1) {
      String var2 = "";
      String var3 = "";
      if (var1.length > 1) {
         var2 = var1[1];
         if (var1.length > 2) {
            var3 = var1[2];
            if (var3.startsWith("\"") && var1[var1.length - 1].endsWith("\"")) {
               var3 = var3.substring(1, var3.length());

               for(int var4 = 3; var4 < var1.length; ++var4) {
                  var3 = String.valueOf(var3) + " " + var1[var4].replace("\"", "");
               }
            }
         }
      }

      Module var5 = ModuleManager.getModule(var2);
      if (var5.getId().equalsIgnoreCase("null")) {
         ClientUtils.sendMessage("Invalid Module.");
      } else if (var3 == "") {
         ClientUtils.sendMessage(String.valueOf(var5.getId()) + "'s name has been reset.");
         var5.setDisplayName(var5.getId());
         ModuleManager.save();
      } else {
         var5.setDisplayName(var3);
         ModuleManager.save();
         ClientUtils.sendMessage(String.valueOf(var5.getId()) + " has been renamed to " + var3);
      }
   }

   public String getHelp() {
      return "Rename - rename <rn> (module) (name) - Rename a module.";
   }
}
