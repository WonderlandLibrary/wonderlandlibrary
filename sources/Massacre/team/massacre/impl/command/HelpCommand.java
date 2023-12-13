package team.massacre.impl.command;

import team.massacre.api.command.Command;
import team.massacre.api.command.CommandExecutionException;
import team.massacre.utils.Logger;

public class HelpCommand implements Command {
   public String[] getAliases() {
      return new String[]{"help", "h"};
   }

   public void execute(String[] arguments) throws CommandExecutionException {
      Logger.print("help: gets a list of commands");
   }

   public String getUsage() {
      return null;
   }

   public boolean testArgsCount(int argc) {
      return false;
   }
}
