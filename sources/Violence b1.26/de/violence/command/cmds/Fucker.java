package de.violence.command.cmds;

import de.violence.Violence;
import de.violence.command.Command;
import de.violence.save.manager.FileManager;

public class Fucker extends Command {
   public String getDescription() {
      return "Changes the blockid from module fucker.";
   }

   public String getName() {
      return "fucker";
   }

   public String getUsage() {
      return ".fucker <BlockId>";
   }

   public void onCommand(String[] args) {
      try {
         int e = Integer.parseInt(args[0]);
         de.violence.module.modules.WORLD.Fucker.blockId = e;
         Violence.getViolence().sendChat("BlockId was set to: " + e);
         FileManager.toggledModules.setInteger("fucker_blockId", Integer.valueOf(e));
         FileManager.toggledModules.save();
      } catch (Exception var3) {
         Violence.getViolence().sendChat("Block not found!");
      }
   }
}
