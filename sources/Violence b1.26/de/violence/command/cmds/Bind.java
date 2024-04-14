package de.violence.command.cmds;

import de.violence.Violence;
import de.violence.command.Command;
import de.violence.module.Module;
import org.lwjgl.input.Keyboard;

public class Bind extends Command {
   public String getDescription() {
      return "Change the keybind of a module.";
   }

   public String getName() {
      return "bind";
   }

   public String getUsage() {
      return ".bind <Module> <Key>";
   }

   public void onCommand(String[] args) {
      String module = args[0];
      int keybind = Keyboard.getKeyIndex(args[1].toUpperCase());
      Module mod = Module.getByName(module);
      if(mod != null) {
         mod.setKeybind(keybind);
         Violence.getViolence().sendChat("Module " + mod.getName() + " was bound to " + args[1].toUpperCase());
      } else {
         Violence.getViolence().sendChat("Module " + module + " not found!");
      }

   }
}
