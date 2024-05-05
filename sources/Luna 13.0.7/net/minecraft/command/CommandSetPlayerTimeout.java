package net.minecraft.command;

import net.minecraft.server.MinecraftServer;

public class CommandSetPlayerTimeout
  extends CommandBase
{
  private static final String __OBFID = "CL_00000999";
  
  public CommandSetPlayerTimeout() {}
  
  public String getCommandName()
  {
    return "setidletimeout";
  }
  
  public int getRequiredPermissionLevel()
  {
    return 3;
  }
  
  public String getCommandUsage(ICommandSender sender)
  {
    return "orders.setidletimeout.usage";
  }
  
  public void processCommand(ICommandSender sender, String[] args)
    throws CommandException
  {
    if (args.length != 1) {
      throw new WrongUsageException("orders.setidletimeout.usage", new Object[0]);
    }
    int var3 = parseInt(args[0], 0);
    MinecraftServer.getServer().setPlayerIdleTimeout(var3);
    notifyOperators(sender, this, "orders.setidletimeout.success", new Object[] { Integer.valueOf(var3) });
  }
}
