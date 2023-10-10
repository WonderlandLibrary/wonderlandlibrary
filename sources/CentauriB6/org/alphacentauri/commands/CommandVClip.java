package org.alphacentauri.commands;

import java.util.ArrayList;
import org.alphacentauri.management.commands.Command;
import org.alphacentauri.management.commands.ICommandHandler;
import org.alphacentauri.management.util.MoveUtils;

public class CommandVClip implements ICommandHandler {
   public String getName() {
      return "VClip";
   }

   public boolean execute(Command cmd) {
      String[] args = cmd.getArgs();
      if(args.length != 1) {
         return false;
      } else {
         try {
            MoveUtils.tpRel(0.0D, Double.parseDouble(args[0]), 0.0D);
            return true;
         } catch (Exception var4) {
            return false;
         }
      }
   }

   public String[] getAliases() {
      return new String[]{"vclip"};
   }

   public String getDesc() {
      return "Vertical Phase";
   }

   public ArrayList autocomplete(Command cmd) {
      return new ArrayList();
   }
}
