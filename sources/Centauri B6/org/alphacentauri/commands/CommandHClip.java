package org.alphacentauri.commands;

import java.util.ArrayList;
import org.alphacentauri.management.commands.Command;
import org.alphacentauri.management.commands.ICommandHandler;
import org.alphacentauri.management.util.MoveUtils;

public class CommandHClip implements ICommandHandler {
   public String getName() {
      return "HClip";
   }

   public boolean execute(Command cmd) {
      String[] args = cmd.getArgs();
      if(args.length != 1) {
         return false;
      } else {
         try {
            MoveUtils.toFwd(Double.parseDouble(args[0]));
            return true;
         } catch (Exception var4) {
            return false;
         }
      }
   }

   public String[] getAliases() {
      return new String[]{"hclip"};
   }

   public String getDesc() {
      return "Horizontal Phase";
   }

   public ArrayList autocomplete(Command cmd) {
      return new ArrayList();
   }
}
