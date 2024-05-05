package net.minecraft.command.server;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.BanList;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.server.management.UserListBans;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;

public class CommandListBans
  extends CommandBase
{
  private static final String __OBFID = "CL_00000596";
  
  public CommandListBans() {}
  
  public String getCommandName()
  {
    return "banlist";
  }
  
  public int getRequiredPermissionLevel()
  {
    return 3;
  }
  
  public boolean canCommandSenderUseCommand(ICommandSender sender)
  {
    return ((MinecraftServer.getServer().getConfigurationManager().getBannedIPs().isLanServer()) || (MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().isLanServer())) && (super.canCommandSenderUseCommand(sender));
  }
  
  public String getCommandUsage(ICommandSender sender)
  {
    return "orders.banlist.usage";
  }
  
  public void processCommand(ICommandSender sender, String[] args)
    throws CommandException
  {
    if ((args.length >= 1) && (args[0].equalsIgnoreCase("ips")))
    {
      sender.addChatMessage(new ChatComponentTranslation("orders.banlist.ips", new Object[] { Integer.valueOf(MinecraftServer.getServer().getConfigurationManager().getBannedIPs().getKeys().length) }));
      sender.addChatMessage(new ChatComponentText(joinNiceString(MinecraftServer.getServer().getConfigurationManager().getBannedIPs().getKeys())));
    }
    else
    {
      sender.addChatMessage(new ChatComponentTranslation("orders.banlist.players", new Object[] { Integer.valueOf(MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().getKeys().length) }));
      sender.addChatMessage(new ChatComponentText(joinNiceString(MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().getKeys())));
    }
  }
  
  public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
  {
    return args.length == 1 ? getListOfStringsMatchingLastWord(args, new String[] { "players", "ips" }) : null;
  }
}
