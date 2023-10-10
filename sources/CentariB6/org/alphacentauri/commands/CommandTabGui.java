package org.alphacentauri.commands;

import java.util.ArrayList;
import org.alphacentauri.AC;
import org.alphacentauri.management.commands.Command;
import org.alphacentauri.management.commands.ICommandHandler;
import org.alphacentauri.management.util.StringUtils;

public class CommandTabGui implements ICommandHandler {
   public String getName() {
      return "TabGui";
   }

   public boolean execute(Command cmd) {
      String[] args = cmd.getArgs();
      if(args.length == 0) {
         AC.addChat(this.getName(), "Available options:");
         AC.addChat(this.getName(), "toggle");
         AC.addChat(this.getName(), "setcolor");
      } else if(args.length == 1) {
         if(args[0].equalsIgnoreCase("toggle")) {
            AC.getConfig().setTabGuiEnabled(!AC.getConfig().isTabGuiEnabled());
            AC.addChat(this.getName(), "TabGui is now " + StringUtils.getModuleState(AC.getConfig().isTabGuiEnabled()));
         } else if(args[0].equalsIgnoreCase("setcolor")) {
            AC.addChat(this.getName(), "Usage: setcolor <name> <hex_color>");
         } else {
            AC.addChat(this.getName(), "Invalid Option! Valid options:");
            AC.addChat(this.getName(), "toggle");
            AC.addChat(this.getName(), "setcolor <name> <hex_color>");
         }
      } else if(args.length == 2) {
         if(args[0].equalsIgnoreCase("setcolor")) {
            String[] options = new String[]{"unselected_bg", "selected_bg", "unselected_fg", "selected_fg"};

            for(String option : options) {
               if(args[1].equalsIgnoreCase(option)) {
                  AC.addChat(this.getName(), "Usage: setcolor " + option + " <hex_color>");
                  return true;
               }
            }

            AC.addChat(this.getName(), "Invalid color name! Available:");

            for(String option : options) {
               AC.addChat(this.getName(), option);
            }
         } else {
            AC.addChat(this.getName(), "Invalid Syntax!");
         }
      } else if(args.length == 3) {
         if(args[0].equalsIgnoreCase("setcolor")) {
            String[] options = new String[]{"unselected_bg", "selected_bg", "unselected_fg", "selected_fg"};

            for(String option : options) {
               if(args[1].equalsIgnoreCase(option)) {
                  if(args[2].length() != 8) {
                     AC.addChat(this.getName(), "Invalid format!");
                     AC.addChat(this.getName(), "Format: AARRGGBB");
                     AC.addChat(this.getName(), "AA = Alpha Color");
                     AC.addChat(this.getName(), "RR = Red Color");
                     AC.addChat(this.getName(), "GG = Green Color");
                     AC.addChat(this.getName(), "BB = Blue Color");
                     AC.addChat(this.getName(), "This is Hex: ");
                     AC.addChat(this.getName(), "http://vlaurie.com/computers2/Articles/hexed.htm");
                  } else {
                     try {
                        int color = (int)Long.parseLong(args[2], 16);
                        if(option.equalsIgnoreCase(options[0])) {
                           AC.getConfig().setTabGuiColorUnselectedBG(color);
                        } else if(option.equalsIgnoreCase(options[1])) {
                           AC.getConfig().setTabGuiColorSelectedBG(color);
                        } else if(option.equalsIgnoreCase(options[2])) {
                           AC.getConfig().setTabGuiColorUnselectedFG(color);
                        } else if(option.equalsIgnoreCase(options[3])) {
                           AC.getConfig().setTabGuiColorSelectedFG(color);
                        }

                        AC.addChat(this.getName(), "TabGui color " + option + " is now " + args[2]);
                     } catch (Exception var9) {
                        AC.addChat(this.getName(), "Invalid format!");
                        AC.addChat(this.getName(), "Format: AARRGGBB");
                        AC.addChat(this.getName(), "AA = Alpha Color");
                        AC.addChat(this.getName(), "RR = Red Color");
                        AC.addChat(this.getName(), "GG = Green Color");
                        AC.addChat(this.getName(), "BB = Blue Color");
                        AC.addChat(this.getName(), "This is Hex: ");
                        AC.addChat(this.getName(), "http://vlaurie.com/computers2/Articles/hexed.htm");
                     }
                  }

                  return true;
               }
            }

            AC.addChat(this.getName(), "Invalid color name! Available:");

            for(String option : options) {
               AC.addChat(this.getName(), option);
            }
         } else {
            AC.addChat(this.getName(), "Invalid Syntax!");
         }
      }

      return true;
   }

   public String[] getAliases() {
      return new String[]{"tabgui"};
   }

   public String getDesc() {
      return "Settings of TabGui";
   }

   public ArrayList autocomplete(Command cmd) {
      ArrayList<String> ret = new ArrayList();
      String[] args = cmd.getArgs();
      if(args.length == 1) {
         if("toggle".startsWith(args[0].toLowerCase())) {
            ret.add("toggle");
         }

         if("setcolor".startsWith(args[0].toLowerCase())) {
            ret.add("setcolor");
         }
      } else if(args.length == 2 && args[0].equalsIgnoreCase("setcolor")) {
         String[] options = new String[]{"unselected_bg", "selected_bg", "unselected_fg", "selected_fg"};

         for(String option : options) {
            if(option.startsWith(args[1].toLowerCase())) {
               ret.add(option);
            }
         }
      }

      return ret;
   }
}
