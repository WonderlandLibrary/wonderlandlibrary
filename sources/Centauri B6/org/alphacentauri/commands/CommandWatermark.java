package org.alphacentauri.commands;

import java.util.ArrayList;
import org.alphacentauri.AC;
import org.alphacentauri.management.commands.Command;
import org.alphacentauri.management.commands.ICommandHandler;

public class CommandWatermark implements ICommandHandler {
   public String getName() {
      return "Watermark";
   }

   public boolean execute(Command cmd) {
      String[] args = cmd.getArgs();
      if(args.length == 0) {
         AC.addChat(this.getName(), "Usage: watermark <<default/defaultstatic/tiny/tinystatic>/<color> <hex_color>>");
      } else if(args.length == 1) {
         if(args[0].equalsIgnoreCase("default")) {
            AC.getConfig().setWatermarkDesign(1);
            AC.addChat(this.getName(), "Set Watermark design to DEFAULT");
         } else if(args[0].equalsIgnoreCase("tiny")) {
            AC.getConfig().setWatermarkDesign(2);
            AC.addChat(this.getName(), "Set Watermark design to TINY");
         } else if(args[0].equalsIgnoreCase("defaultstatic")) {
            AC.getConfig().setWatermarkDesign(3);
            AC.addChat(this.getName(), "Set Watermark design to DEFAULTSTATIC");
         } else if(args[0].equalsIgnoreCase("tinystatic")) {
            AC.getConfig().setWatermarkDesign(4);
            AC.addChat(this.getName(), "Set Watermark design to TINYSTATIC");
         } else if(args[0].equalsIgnoreCase("color")) {
            AC.addChat(this.getName(), "Usage: watermark color <hex_color>");
         } else {
            AC.addChat(this.getName(), "Unknown Option");
         }
      } else {
         if(args.length != 2) {
            return false;
         }

         if(!args[0].equalsIgnoreCase("color")) {
            return false;
         }

         String hex = args[1];
         if(hex.length() != 8) {
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
               int color = (int)Long.parseLong(hex, 16);
               AC.getConfig().setWatermarkColor(color);
               AC.addChat(this.getName(), "Watermark color is now " + hex);
            } catch (Exception var5) {
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
      }

      return true;
   }

   public String[] getAliases() {
      return new String[]{"watermark"};
   }

   public String getDesc() {
      return "Changes how the watermark looks";
   }

   public ArrayList autocomplete(Command cmd) {
      ArrayList<String> ret = new ArrayList();
      String[] args = cmd.getArgs();
      if(args.length == 1) {
         String[] opts = new String[]{"default", "tiny", "defaultstatic", "tinystatic"};

         for(String opt : opts) {
            if(opt.startsWith(opt)) {
               ret.add(opt);
            }
         }
      }

      return ret;
   }
}
