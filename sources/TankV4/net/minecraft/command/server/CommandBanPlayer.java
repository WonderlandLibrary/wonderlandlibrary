package net.minecraft.command.server;

import com.mojang.authlib.GameProfile;
import java.util.Date;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserListBansEntry;
import net.minecraft.util.BlockPos;

public class CommandBanPlayer extends CommandBase {
   public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
      if (var2.length >= 1 && var2[0].length() > 0) {
         MinecraftServer var3 = MinecraftServer.getServer();
         GameProfile var4 = var3.getPlayerProfileCache().getGameProfileForUsername(var2[0]);
         if (var4 == null) {
            throw new CommandException("commands.ban.failed", new Object[]{var2[0]});
         } else {
            String var5 = null;
            if (var2.length >= 2) {
               var5 = getChatComponentFromNthArg(var1, var2, 1).getUnformattedText();
            }

            UserListBansEntry var6 = new UserListBansEntry(var4, (Date)null, var1.getName(), (Date)null, var5);
            var3.getConfigurationManager().getBannedPlayers().addEntry(var6);
            EntityPlayerMP var7 = var3.getConfigurationManager().getPlayerByUsername(var2[0]);
            if (var7 != null) {
               var7.playerNetServerHandler.kickPlayerFromServer("You are banned from this server.");
            }

            notifyOperators(var1, this, "commands.ban.success", new Object[]{var2[0]});
         }
      } else {
         throw new WrongUsageException("commands.ban.usage", new Object[0]);
      }
   }

   public int getRequiredPermissionLevel() {
      return 3;
   }

   public String getCommandName() {
      return "ban";
   }

   public String getCommandUsage(ICommandSender var1) {
      return "commands.ban.usage";
   }

   public List addTabCompletionOptions(ICommandSender var1, String[] var2, BlockPos var3) {
      return var2.length >= 1 ? getListOfStringsMatchingLastWord(var2, MinecraftServer.getServer().getAllUsernames()) : null;
   }

   public boolean canCommandSenderUseCommand(ICommandSender var1) {
      return MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().isLanServer() && super.canCommandSenderUseCommand(var1);
   }
}
