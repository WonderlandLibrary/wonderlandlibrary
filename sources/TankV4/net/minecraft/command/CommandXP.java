package net.minecraft.command;

import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandXP extends CommandBase {
   public boolean isUsernameIndex(String[] var1, int var2) {
      return var2 == 1;
   }

   public int getRequiredPermissionLevel() {
      return 2;
   }

   protected String[] getAllUsernames() {
      return MinecraftServer.getServer().getAllUsernames();
   }

   public String getCommandName() {
      return "xp";
   }

   public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
      if (var2.length <= 0) {
         throw new WrongUsageException("commands.xp.usage", new Object[0]);
      } else {
         String var3 = var2[0];
         boolean var4 = var3.endsWith("l") || var3.endsWith("L");
         if (var4 && var3.length() > 1) {
            var3 = var3.substring(0, var3.length() - 1);
         }

         int var5 = parseInt(var3);
         boolean var6 = var5 < 0;
         if (var6) {
            var5 *= -1;
         }

         EntityPlayerMP var7 = var2.length > 1 ? getPlayer(var1, var2[1]) : getCommandSenderAsPlayer(var1);
         if (var4) {
            var1.setCommandStat(CommandResultStats.Type.QUERY_RESULT, var7.experienceLevel);
            if (var6) {
               var7.addExperienceLevel(-var5);
               notifyOperators(var1, this, "commands.xp.success.negative.levels", new Object[]{var5, var7.getName()});
            } else {
               var7.addExperienceLevel(var5);
               notifyOperators(var1, this, "commands.xp.success.levels", new Object[]{var5, var7.getName()});
            }
         } else {
            var1.setCommandStat(CommandResultStats.Type.QUERY_RESULT, var7.experienceTotal);
            if (var6) {
               throw new CommandException("commands.xp.failure.widthdrawXp", new Object[0]);
            }

            var7.addExperience(var5);
            notifyOperators(var1, this, "commands.xp.success", new Object[]{var5, var7.getName()});
         }

      }
   }

   public String getCommandUsage(ICommandSender var1) {
      return "commands.xp.usage";
   }

   public List addTabCompletionOptions(ICommandSender var1, String[] var2, BlockPos var3) {
      return var2.length == 2 ? getListOfStringsMatchingLastWord(var2, this.getAllUsernames()) : null;
   }
}
