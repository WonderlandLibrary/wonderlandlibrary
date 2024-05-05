package net.minecraft.command.server;

import com.mojang.authlib.GameProfile;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.server.management.UserListBans;
import net.minecraft.server.management.UserListBansEntry;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;

public class CommandBanPlayer
  extends CommandBase
{
  private static final String __OBFID = "CL_00000165";
  
  public CommandBanPlayer() {}
  
  public String getCommandName()
  {
    return "ban";
  }
  
  public int getRequiredPermissionLevel()
  {
    return 3;
  }
  
  public String getCommandUsage(ICommandSender sender)
  {
    return "orders.ban.usage";
  }
  
  public boolean canCommandSenderUseCommand(ICommandSender sender)
  {
    return (MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().isLanServer()) && (super.canCommandSenderUseCommand(sender));
  }
  
  public void processCommand(ICommandSender sender, String[] args)
    throws CommandException
  {
    if ((args.length >= 1) && (args[0].length() > 0))
    {
      MinecraftServer var3 = MinecraftServer.getServer();
      GameProfile var4 = var3.getPlayerProfileCache().getGameProfileForUsername(args[0]);
      if (var4 == null) {
        throw new CommandException("orders.ban.failed", new Object[] { args[0] });
      }
      String var5 = null;
      if (args.length >= 2) {
        var5 = getChatComponentFromNthArg(sender, args, 1).getUnformattedText();
      }
      UserListBansEntry var6 = new UserListBansEntry(var4, null, sender.getName(), null, var5);
      var3.getConfigurationManager().getBannedPlayers().addEntry(var6);
      EntityPlayerMP var7 = var3.getConfigurationManager().getPlayerByUsername(args[0]);
      if (var7 != null) {
        var7.playerNetServerHandler.kickPlayerFromServer("You are banned from this server.");
      }
      notifyOperators(sender, this, "orders.ban.success", new Object[] { args[0] });
    }
    else
    {
      throw new WrongUsageException("orders.ban.usage", new Object[0]);
    }
  }
  
  public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
  {
    return args.length >= 1 ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
  }
}
