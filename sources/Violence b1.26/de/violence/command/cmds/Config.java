package de.violence.command.cmds;

import de.violence.Violence;
import de.violence.command.Command;
import de.violence.config.SaveConfig;

public class Config extends Command {
   public String getDescription() {
      return "Load / save a config.";
   }

   public String getName() {
      return "config";
   }

   public String getUsage() {
      return ".config load|save <Config-Name>";
   }

   public void onCommand(String[] args) {
      try {
         String e;
         if(args[0].equalsIgnoreCase("save")) {
            e = args[1];
            (new SaveConfig(e)).saveConfig();
            Violence.getViolence().sendChat("Succesfull created config with name: §3" + e);
         } else if(args[0].equalsIgnoreCase("load")) {
            e = args[1];
            (new SaveConfig("§" + e)).loadConfig();
            Violence.getViolence().sendChat("Succesfull loaded config: §3" + e);
         }
      } catch (Exception var3) {
         Violence.getViolence().sendChat("§cError.");
         var3.printStackTrace();
      }

   }
}
