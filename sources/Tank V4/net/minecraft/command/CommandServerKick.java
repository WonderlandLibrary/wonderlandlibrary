package net.minecraft.command;

import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandServerKick extends CommandBase {
   public String getCommandUsage(ICommandSender var1) {
      return "commands.kick.usage";
   }

   public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
      if (var2.length > 0 && var2[0].length() > 1) {
         EntityPlayerMP var3 = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(var2[0]);
         String var4 = "Kicked by an operator.";
         boolean var5 = false;
         if (var3 == null) {
            throw new PlayerNotFoundException();
         } else {
            if (var2.length >= 2) {
               var4 = getChatComponentFromNthArg(var1, var2, 1).getUnformattedText();
               var5 = true;
            }

            var3.playerNetServerHandler.kickPlayerFromServer(var4);
            if (var5) {
               notifyOperators(var1, this, "commands.kick.success.reason", new Object[]{var3.getName(), var4});
            } else {
               notifyOperators(var1, this, "commands.kick.success", new Object[]{var3.getName()});
            }

         }
      } else {
         throw new WrongUsageException("commands.kick.usage", new Object[0]);
      }
   }

   public int getRequiredPermissionLevel() {
      return 3;
   }

   public String getCommandName() {
      return "kick";
   }

   public List addTabCompletionOptions(ICommandSender var1, String[] var2, BlockPos var3) {
      return var2.length >= 1 ? getListOfStringsMatchingLastWord(var2, MinecraftServer.getServer().getAllUsernames()) : null;
   }
}
