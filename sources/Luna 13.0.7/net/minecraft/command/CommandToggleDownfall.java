package net.minecraft.command;

import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;

public class CommandToggleDownfall
  extends CommandBase
{
  private static final String __OBFID = "CL_00001184";
  
  public CommandToggleDownfall() {}
  
  public String getCommandName()
  {
    return "toggledownfall";
  }
  
  public int getRequiredPermissionLevel()
  {
    return 2;
  }
  
  public String getCommandUsage(ICommandSender sender)
  {
    return "orders.downfall.usage";
  }
  
  public void processCommand(ICommandSender sender, String[] args)
    throws CommandException
  {
    toggleDownfall();
    notifyOperators(sender, this, "orders.downfall.success", new Object[0]);
  }
  
  protected void toggleDownfall()
  {
    WorldInfo var1 = net.minecraft.server.MinecraftServer.getServer().worldServers[0].getWorldInfo();
    var1.setRaining(!var1.isRaining());
  }
}
