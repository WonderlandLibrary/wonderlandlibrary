package net.minecraft.command.server;

import com.mojang.authlib.GameProfile;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class CommandWhitelist extends CommandBase {
  public String getCommandName() {
    return "whitelist";
  }
  
  public int getRequiredPermissionLevel() {
    return 3;
  }
  
  public String getCommandUsage(ICommandSender sender) {
    return "commands.whitelist.usage";
  }
  
  public void processCommand(ICommandSender sender, String[] args) throws CommandException {
    if (args.length < 1)
      throw new WrongUsageException("commands.whitelist.usage", new Object[0]); 
    MinecraftServer minecraftserver = MinecraftServer.getServer();
    if (args[0].equals("on")) {
      minecraftserver.getConfigurationManager().setWhiteListEnabled(true);
      notifyOperators(sender, (ICommand)this, "commands.whitelist.enabled", new Object[0]);
    } else if (args[0].equals("off")) {
      minecraftserver.getConfigurationManager().setWhiteListEnabled(false);
      notifyOperators(sender, (ICommand)this, "commands.whitelist.disabled", new Object[0]);
    } else if (args[0].equals("list")) {
      sender.addChatMessage((IChatComponent)new ChatComponentTranslation("commands.whitelist.list", new Object[] { Integer.valueOf((minecraftserver.getConfigurationManager().getWhitelistedPlayerNames()).length), Integer.valueOf((minecraftserver.getConfigurationManager().getAvailablePlayerDat()).length) }));
      String[] astring = minecraftserver.getConfigurationManager().getWhitelistedPlayerNames();
      sender.addChatMessage((IChatComponent)new ChatComponentText(joinNiceString((Object[])astring)));
    } else if (args[0].equals("add")) {
      if (args.length < 2)
        throw new WrongUsageException("commands.whitelist.add.usage", new Object[0]); 
      GameProfile gameprofile = minecraftserver.getPlayerProfileCache().getGameProfileForUsername(args[1]);
      if (gameprofile == null)
        throw new CommandException("commands.whitelist.add.failed", new Object[] { args[1] }); 
      minecraftserver.getConfigurationManager().addWhitelistedPlayer(gameprofile);
      notifyOperators(sender, (ICommand)this, "commands.whitelist.add.success", new Object[] { args[1] });
    } else if (args[0].equals("remove")) {
      if (args.length < 2)
        throw new WrongUsageException("commands.whitelist.remove.usage", new Object[0]); 
      GameProfile gameprofile1 = minecraftserver.getConfigurationManager().getWhitelistedPlayers().func_152706_a(args[1]);
      if (gameprofile1 == null)
        throw new CommandException("commands.whitelist.remove.failed", new Object[] { args[1] }); 
      minecraftserver.getConfigurationManager().removePlayerFromWhitelist(gameprofile1);
      notifyOperators(sender, (ICommand)this, "commands.whitelist.remove.success", new Object[] { args[1] });
    } else if (args[0].equals("reload")) {
      minecraftserver.getConfigurationManager().loadWhiteList();
      notifyOperators(sender, (ICommand)this, "commands.whitelist.reloaded", new Object[0]);
    } 
  }
  
  public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
    if (args.length == 1)
      return getListOfStringsMatchingLastWord(args, new String[] { "on", "off", "list", "add", "remove", "reload" }); 
    if (args.length == 2) {
      if (args[0].equals("remove"))
        return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getConfigurationManager().getWhitelistedPlayerNames()); 
      if (args[0].equals("add"))
        return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getPlayerProfileCache().getUsernames()); 
    } 
    return null;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\command\server\CommandWhitelist.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */