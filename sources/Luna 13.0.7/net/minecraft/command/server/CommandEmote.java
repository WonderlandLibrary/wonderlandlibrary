package net.minecraft.command.server;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class CommandEmote
  extends CommandBase
{
  private static final String __OBFID = "CL_00000351";
  
  public CommandEmote() {}
  
  public String getCommandName()
  {
    return "me";
  }
  
  public int getRequiredPermissionLevel()
  {
    return 0;
  }
  
  public String getCommandUsage(ICommandSender sender)
  {
    return "orders.me.usage";
  }
  
  public void processCommand(ICommandSender sender, String[] args)
    throws CommandException
  {
    if (args.length <= 0) {
      throw new WrongUsageException("orders.me.usage", new Object[0]);
    }
    IChatComponent var3 = getChatComponentFromNthArg(sender, args, 0, !(sender instanceof EntityPlayer));
    MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentTranslation("chat.type.emote", new Object[] { sender.getDisplayName(), var3 }));
  }
  
  public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
  {
    return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
  }
}
