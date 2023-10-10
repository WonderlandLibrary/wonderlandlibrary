package my.NewSnake.Tank.command.commands;

import my.NewSnake.Tank.command.Com;
import my.NewSnake.Tank.command.Command;
import my.NewSnake.utils.ClientUtils;

@Com(
   names = {""}
)
public class UnknownCommand extends Command {
   public void runCommand(String[] var1) {
      ClientUtils.sendMessage("Unknown Command. Type \"help\" for a list of commands.");
   }
}
