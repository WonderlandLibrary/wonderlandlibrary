package team.massacre.impl.command;

import java.util.Iterator;
import team.massacre.Massacre;
import team.massacre.api.command.Command;
import team.massacre.api.command.CommandExecutionException;
import team.massacre.api.manager.Config;
import team.massacre.utils.Logger;

public final class ConfigCommand implements Command {
   public String[] getAliases() {
      return new String[]{"config", "c", "preset"};
   }

   public void execute(String[] arguments) throws CommandExecutionException {
      String upperCaseFunction = arguments[1].toUpperCase();
      if (arguments.length == 3) {
         byte var4 = -1;
         switch(upperCaseFunction.hashCode()) {
         case 2342118:
            if (upperCaseFunction.equals("LOAD")) {
               var4 = 0;
            }
            break;
         case 2537853:
            if (upperCaseFunction.equals("SAVE")) {
               var4 = 1;
            }
            break;
         case 2012838315:
            if (upperCaseFunction.equals("DELETE")) {
               var4 = 2;
            }
         }

         switch(var4) {
         case 0:
            if (Massacre.INSTANCE.getConfigManager().loadConfig(arguments[2])) {
               this.success("loaded", arguments[2]);
            } else {
               this.fail("load", arguments[2]);
            }

            return;
         case 1:
            if (Massacre.INSTANCE.getConfigManager().saveConfig(arguments[2])) {
               this.success("saved", arguments[2]);
            } else {
               this.fail("save", arguments[2]);
            }

            return;
         case 2:
            if (Massacre.INSTANCE.getConfigManager().deleteConfig(arguments[2])) {
               this.success("deleted", arguments[2]);
            } else {
               this.fail("delete", arguments[2]);
            }

            return;
         }
      } else if (arguments.length == 2 && upperCaseFunction.equalsIgnoreCase("LIST")) {
         Logger.print("Available Configs:");
         Iterator var3 = Massacre.INSTANCE.getConfigManager().getElements().iterator();

         while(var3.hasNext()) {
            Config config = (Config)var3.next();
            Logger.print(config.getName());
         }

         return;
      }

      throw new CommandExecutionException(this.getUsage());
   }

   private void success(String type, String configName) {
      Logger.print(String.format("Successfully %s config: '%s'", type, configName));
   }

   private void fail(String type, String configName) {
      Logger.print(String.format("Failed to %s config: '%s'", type, configName));
   }

   public String getUsage() {
      return "config/c/preset <load/save/delete/list>";
   }

   public boolean testArgsCount(int argc) {
      return argc >= 2;
   }
}
