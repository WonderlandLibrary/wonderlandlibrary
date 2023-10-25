package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class CommandListPlayers extends CommandBase {
  public String getCommandName() {
    return "list";
  }
  
  public int getRequiredPermissionLevel() {
    return 0;
  }
  
  public String getCommandUsage(ICommandSender sender) {
    return "commands.players.usage";
  }
  
  public void processCommand(ICommandSender sender, String[] args) throws CommandException {
    int i = MinecraftServer.getServer().getCurrentPlayerCount();
    sender.addChatMessage((IChatComponent)new ChatComponentTranslation("commands.players.list", new Object[] { Integer.valueOf(i), Integer.valueOf(MinecraftServer.getServer().getMaxPlayers()) }));
    sender.addChatMessage((IChatComponent)new ChatComponentText(MinecraftServer.getServer().getConfigurationManager().func_181058_b((args.length > 0 && "uuids".equalsIgnoreCase(args[0])))));
    sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, i);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\command\server\CommandListPlayers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */