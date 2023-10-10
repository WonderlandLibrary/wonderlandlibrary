package net.minecraft.command.server;

import java.util.List;
import java.util.regex.Matcher;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandPardonIp extends CommandBase {
   public List addTabCompletionOptions(ICommandSender var1, String[] var2, BlockPos var3) {
      return var2.length == 1 ? getListOfStringsMatchingLastWord(var2, MinecraftServer.getServer().getConfigurationManager().getBannedIPs().getKeys()) : null;
   }

   public String getCommandUsage(ICommandSender var1) {
      return "commands.unbanip.usage";
   }

   public boolean canCommandSenderUseCommand(ICommandSender var1) {
      return MinecraftServer.getServer().getConfigurationManager().getBannedIPs().isLanServer() && super.canCommandSenderUseCommand(var1);
   }

   public String getCommandName() {
      return "pardon-ip";
   }

   public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
      if (var2.length == 1 && var2[0].length() > 1) {
         Matcher var3 = CommandBanIp.field_147211_a.matcher(var2[0]);
         if (var3.matches()) {
            MinecraftServer.getServer().getConfigurationManager().getBannedIPs().removeEntry(var2[0]);
            notifyOperators(var1, this, "commands.unbanip.success", new Object[]{var2[0]});
         } else {
            throw new SyntaxErrorException("commands.unbanip.invalid", new Object[0]);
         }
      } else {
         throw new WrongUsageException("commands.unbanip.usage", new Object[0]);
      }
   }

   public int getRequiredPermissionLevel() {
      return 3;
   }
}
