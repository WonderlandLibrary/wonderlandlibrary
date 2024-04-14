package de.violence.command.cmds;

import de.violence.Violence;
import de.violence.command.Command;

public class BlockESP extends Command {
   public String getDescription() {
      return "remove/add blocks in the arraylist from blockesp.";
   }

   public String getName() {
      return "blockesp";
   }

   public String getUsage() {
      return ".blockesp add|remove <BlockId>";
   }

   public void onCommand(String[] args) {
      try {
         String e = args[0];
         int blockId = Integer.parseInt(args[1]);
         if(blockId == 0) {
            Violence.getViolence().sendChat("Blockid " + blockId + " not found.");
            return;
         }

         if(e.equalsIgnoreCase("add")) {
            if(!de.violence.module.modules.RENDER.BlockESP.blocks.contains(Integer.valueOf(blockId))) {
               de.violence.module.modules.RENDER.BlockESP.blocks.add(Integer.valueOf(blockId));
               Violence.getViolence().sendChat("BlockId " + blockId + " added to arrayList.");
            } else {
               Violence.getViolence().sendChat("BlockId " + blockId + " already addet!");
            }
         } else if(e.equalsIgnoreCase("remove")) {
            if(de.violence.module.modules.RENDER.BlockESP.blocks.contains(Integer.valueOf(blockId))) {
               de.violence.module.modules.RENDER.BlockESP.blocks.remove(de.violence.module.modules.RENDER.BlockESP.blocks.indexOf(Integer.valueOf(blockId)));
               Violence.getViolence().sendChat("BlockId " + blockId + " remove from arrayList.");
            } else {
               Violence.getViolence().sendChat("BlockId " + blockId + " not found.");
            }
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
