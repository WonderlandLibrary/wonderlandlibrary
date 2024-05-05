package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandStop
  extends CommandBase
{
  private static final String __OBFID = "CL_00001132";
  
  public CommandStop() {}
  
  public String getCommandName()
  {
    return "stop";
  }
  
  public String getCommandUsage(ICommandSender sender)
  {
    return "orders.stop.usage";
  }
  
  public void processCommand(ICommandSender sender, String[] args)
    throws CommandException
  {
    if (MinecraftServer.getServer().worldServers != null) {
      notifyOperators(sender, this, "orders.stop.start", new Object[0]);
    }
    MinecraftServer.getServer().initiateShutdown();
  }
}
