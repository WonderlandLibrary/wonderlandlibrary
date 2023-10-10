package net.minecraft.command;

import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandSetSpawnpoint extends CommandBase {
   public String getCommandName() {
      return "spawnpoint";
   }

   public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
      if (var2.length > 1 && var2.length < 4) {
         throw new WrongUsageException("commands.spawnpoint.usage", new Object[0]);
      } else {
         EntityPlayerMP var3 = var2.length > 0 ? getPlayer(var1, var2[0]) : getCommandSenderAsPlayer(var1);
         BlockPos var4 = var2.length > 3 ? parseBlockPos(var1, var2, 1, true) : var3.getPosition();
         if (var3.worldObj != null) {
            var3.setSpawnPoint(var4, true);
            notifyOperators(var1, this, "commands.spawnpoint.success", new Object[]{var3.getName(), var4.getX(), var4.getY(), var4.getZ()});
         }

      }
   }

   public String getCommandUsage(ICommandSender var1) {
      return "commands.spawnpoint.usage";
   }

   public boolean isUsernameIndex(String[] var1, int var2) {
      return var2 == 0;
   }

   public int getRequiredPermissionLevel() {
      return 2;
   }

   public List addTabCompletionOptions(ICommandSender var1, String[] var2, BlockPos var3) {
      return var2.length == 1 ? getListOfStringsMatchingLastWord(var2, MinecraftServer.getServer().getAllUsernames()) : (var2.length > 1 && var2.length <= 4 ? func_175771_a(var2, 1, var3) : null);
   }
}
