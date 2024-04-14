package de.violence.command.cmds;

import de.violence.Violence;
import de.violence.command.Command;
import de.violence.module.Module;

public class Toggle extends Command {
   public String getDescription() {
      return "Toggle modules.";
   }

   public String getName() {
      return "t";
   }

   public String getUsage() {
      return ".t <Module>";
   }

   public void onCommand(String[] args) {
      String module = args[0];
      Module mod = Module.getByName(module);
      if(mod != null) {
         mod.setToggled(!mod.isToggled());
         if(mod.isToggled()) {
            Violence.getViolence().sendChat("Module " + mod.getName() + " was toggled on");
         } else {
            Violence.getViolence().sendChat("Module " + mod.getName() + " was toggled off");
         }

      } else {
         Violence.getViolence().sendChat("Module not found.");
      }
   }
}
