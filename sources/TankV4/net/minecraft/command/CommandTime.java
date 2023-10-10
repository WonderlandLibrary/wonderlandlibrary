package net.minecraft.command;

import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldServer;

public class CommandTime extends CommandBase {
   public String getCommandUsage(ICommandSender var1) {
      return "commands.time.usage";
   }

   public String getCommandName() {
      return "time";
   }

   protected void setTime(ICommandSender var1, int var2) {
      for(int var3 = 0; var3 < MinecraftServer.getServer().worldServers.length; ++var3) {
         MinecraftServer.getServer().worldServers[var3].setWorldTime((long)var2);
      }

   }

   public int getRequiredPermissionLevel() {
      return 2;
   }

   protected void addTime(ICommandSender var1, int var2) {
      for(int var3 = 0; var3 < MinecraftServer.getServer().worldServers.length; ++var3) {
         WorldServer var4 = MinecraftServer.getServer().worldServers[var3];
         var4.setWorldTime(var4.getWorldTime() + (long)var2);
      }

   }

   public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
      if (var2.length > 1) {
         int var3;
         if (var2[0].equals("set")) {
            if (var2[1].equals("day")) {
               var3 = 1000;
            } else if (var2[1].equals("night")) {
               var3 = 13000;
            } else {
               var3 = parseInt(var2[1], 0);
            }

            this.setTime(var1, var3);
            notifyOperators(var1, this, "commands.time.set", new Object[]{var3});
            return;
         }

         if (var2[0].equals("add")) {
            var3 = parseInt(var2[1], 0);
            this.addTime(var1, var3);
            notifyOperators(var1, this, "commands.time.added", new Object[]{var3});
            return;
         }

         if (var2[0].equals("query")) {
            if (var2[1].equals("daytime")) {
               var3 = (int)(var1.getEntityWorld().getWorldTime() % 2147483647L);
               var1.setCommandStat(CommandResultStats.Type.QUERY_RESULT, var3);
               notifyOperators(var1, this, "commands.time.query", new Object[]{var3});
               return;
            }

            if (var2[1].equals("gametime")) {
               var3 = (int)(var1.getEntityWorld().getTotalWorldTime() % 2147483647L);
               var1.setCommandStat(CommandResultStats.Type.QUERY_RESULT, var3);
               notifyOperators(var1, this, "commands.time.query", new Object[]{var3});
               return;
            }
         }
      }

      throw new WrongUsageException("commands.time.usage", new Object[0]);
   }

   public List addTabCompletionOptions(ICommandSender var1, String[] var2, BlockPos var3) {
      return var2.length == 1 ? getListOfStringsMatchingLastWord(var2, new String[]{"set", "add", "query"}) : (var2.length == 2 && var2[0].equals("set") ? getListOfStringsMatchingLastWord(var2, new String[]{"day", "night"}) : (var2.length == 2 && var2[0].equals("query") ? getListOfStringsMatchingLastWord(var2, new String[]{"daytime", "gametime"}) : null));
   }
}
