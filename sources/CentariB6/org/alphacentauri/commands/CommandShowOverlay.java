package org.alphacentauri.commands;

import java.util.ArrayList;
import org.alphacentauri.AC;
import org.alphacentauri.gui.screens.GuiACOverlay;
import org.alphacentauri.management.commands.Command;
import org.alphacentauri.management.commands.ICommandHandler;
import org.lwjgl.input.Keyboard;

public class CommandShowOverlay implements ICommandHandler {
   public String getName() {
      return "ShowOverlay";
   }

   public boolean execute(Command cmd) {
      String[] args = cmd.getArgs();
      if(args.length == 1) {
         if(args[0].equalsIgnoreCase("show")) {
            AC.getMC().displayGuiScreen(new GuiACOverlay(AC.getMC().currentScreen));
         } else {
            int key = Keyboard.getKeyIndex(args[0].toUpperCase());
            if(key == 0) {
               AC.addChat(this.getName(), "Unknown key \"" + args[0].toUpperCase() + "\"!");
            } else {
               AC.getConfig().setOverlayKey(key);
               AC.addChat(this.getName(), "Overlay rebound to key \"" + args[0].toUpperCase() + "\"");
            }
         }
      } else {
         AC.addChat(this.getName(), "Usage: overlay <show/key>");
      }

      return true;
   }

   public String[] getAliases() {
      return new String[]{"overlay"};
   }

   public String getDesc() {
      return "Shows the Alpha Centauri Overlay";
   }

   public ArrayList autocomplete(Command cmd) {
      return new ArrayList();
   }
}
