package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats.Type;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;

public class CommandListPlayers
  extends CommandBase
{
  private static final String __OBFID = "CL_00000615";
  
  public CommandListPlayers() {}
  
  public String getCommandName()
  {
    return "list";
  }
  
  public int getRequiredPermissionLevel()
  {
    return 0;
  }
  
  public String getCommandUsage(ICommandSender sender)
  {
    return "orders.players.usage";
  }
  
  public void processCommand(ICommandSender sender, String[] args)
    throws CommandException
  {
    int var3 = MinecraftServer.getServer().getCurrentPlayerCount();
    sender.addChatMessage(new ChatComponentTranslation("orders.players.list", new Object[] { Integer.valueOf(var3), Integer.valueOf(MinecraftServer.getServer().getMaxPlayers()) }));
    sender.addChatMessage(new ChatComponentText(MinecraftServer.getServer().getConfigurationManager().func_180602_f()));
    sender.func_174794_a(CommandResultStats.Type.QUERY_RESULT, var3);
  }
}
