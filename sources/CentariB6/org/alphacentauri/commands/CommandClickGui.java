package org.alphacentauri.commands;

import java.util.ArrayList;
import org.alphacentauri.AC;
import org.alphacentauri.core.CoreConfig;
import org.alphacentauri.gui.screens.GuiClick;
import org.alphacentauri.management.commands.Command;
import org.alphacentauri.management.commands.ICommandHandler;
import org.alphacentauri.management.tasks.TaskShowGui;

public class CommandClickGui implements ICommandHandler {
   public String getName() {
      return "ClickGui";
   }

   public boolean execute(Command cmd) {
      String[] args = cmd.getArgs();
      if(args.length == 1 && args[0].equalsIgnoreCase("reload")) {
         AC.getInstance().guiManager = null;
      } else if(args.length == 4) {
         if(args[0].equalsIgnoreCase("color")) {
            try {
               CoreConfig config = AC.getConfig();
               config.setClickGuiR(Integer.parseInt(args[1]));
               config.setClickGuiG(Integer.parseInt(args[2]));
               config.setClickGuiB(Integer.parseInt(args[3]));
               return true;
            } catch (Exception var4) {
               AC.addChat(this.getName(), "Usage: " + cmd.getCommand() + " color <r> <g> <b>");
               return true;
            }
         }
      } else if(args.length == 1 && args[0].equalsIgnoreCase("color")) {
         AC.addChat(this.getName(), "Usage: " + cmd.getCommand() + " color <r> <g> <b>");
         return true;
      }

      AC.getTaskManager().add(new TaskShowGui(new GuiClick(AC.getGuiManager())));
      return true;
   }

   public String[] getAliases() {
      return new String[]{"clickgui", "gui"};
   }

   public String getDesc() {
      return "Shows the Click GUI";
   }

   public ArrayList autocomplete(Command cmd) {
      ArrayList<String> ret = new ArrayList();
      String[] args = cmd.getArgs();
      if(args.length == 1) {
         String[] opts = new String[]{"reload", "color"};

         for(String opt : opts) {
            if(opt.toLowerCase().startsWith(args[0].toLowerCase())) {
               ret.add(opt);
            }
         }
      }

      return ret;
   }
}
