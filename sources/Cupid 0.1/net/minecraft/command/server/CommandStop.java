package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandStop extends CommandBase {
  public String getCommandName() {
    return "stop";
  }
  
  public String getCommandUsage(ICommandSender sender) {
    return "commands.stop.usage";
  }
  
  public void processCommand(ICommandSender sender, String[] args) throws CommandException {
    if ((MinecraftServer.getServer()).worldServers != null)
      notifyOperators(sender, (ICommand)this, "commands.stop.start", new Object[0]); 
    MinecraftServer.getServer().initiateShutdown();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\command\server\CommandStop.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */