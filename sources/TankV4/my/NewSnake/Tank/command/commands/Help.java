package my.NewSnake.Tank.command.commands;

import java.util.Iterator;
import my.NewSnake.Tank.command.Com;
import my.NewSnake.Tank.command.Command;
import my.NewSnake.Tank.command.CommandManager;
import my.NewSnake.utils.ClientUtils;

@Com(
   names = {"help"}
)
public class Help extends Command {
   public void runCommand(String[] var1) {
      Iterator var3 = CommandManager.commandList.iterator();

      while(var3.hasNext()) {
         Command var2 = (Command)var3.next();
         if (!(var2 instanceof OptionCommand) && var2.getHelp() != null) {
            ClientUtils.sendMessage(var2.getHelp());
         }
      }

      ClientUtils.sendMessage(OptionCommand.getHelpString());
   }

   public String getHelp() {
      return "Help - help - Returns a list of commands.";
   }
}
