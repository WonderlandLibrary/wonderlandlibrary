package my.NewSnake.Tank.command.commands;

import my.NewSnake.Tank.command.Com;
import my.NewSnake.Tank.command.Command;
import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.module.ModuleManager;
import my.NewSnake.utils.ClientUtils;
import org.lwjgl.input.Keyboard;

@Com(
   names = {"bind", "b"}
)
public class Bind extends Command {
   public void runCommand(String[] var1) {
      String var2 = "";
      String var3 = "";
      if (var1.length > 1) {
         var2 = var1[1];
         if (var1.length > 2) {
            var3 = var1[2];
         }
      }

      Module var4 = ModuleManager.getModule(var2);
      if (var4.getId().equalsIgnoreCase("null")) {
         ClientUtils.sendMessage("Invalid Module.");
      } else if (var3 == "") {
         ClientUtils.sendMessage(String.valueOf(var4.getDisplayName()) + "'s bind has been cleared.");
         var4.setKeybind(0);
         ModuleManager.save();
      } else {
         var4.setKeybind(Keyboard.getKeyIndex(var3.toUpperCase()));
         ModuleManager.save();
         if (Keyboard.getKeyIndex(var3.toUpperCase()) == 0) {
            ClientUtils.sendMessage("Invalid Key entered, Bind cleared.");
         } else {
            ClientUtils.sendMessage(String.valueOf(var4.getDisplayName()) + " bound to " + var3);
         }

      }
   }

   public String getHelp() {
      return "Bind - bind <b> (module) (key) - Bind a module to a key.";
   }
}
