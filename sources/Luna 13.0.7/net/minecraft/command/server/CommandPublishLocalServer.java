package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldSettings.GameType;

public class CommandPublishLocalServer
  extends CommandBase
{
  private static final String __OBFID = "CL_00000799";
  
  public CommandPublishLocalServer() {}
  
  public String getCommandName()
  {
    return "publish";
  }
  
  public String getCommandUsage(ICommandSender sender)
  {
    return "orders.publish.usage";
  }
  
  public void processCommand(ICommandSender sender, String[] args)
    throws CommandException
  {
    String var3 = MinecraftServer.getServer().shareToLAN(WorldSettings.GameType.SURVIVAL, false);
    if (var3 != null) {
      notifyOperators(sender, this, "orders.publish.started", new Object[] { var3 });
    } else {
      notifyOperators(sender, this, "orders.publish.failed", new Object[0]);
    }
  }
}
