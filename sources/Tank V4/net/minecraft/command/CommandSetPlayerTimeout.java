package net.minecraft.command;

import net.minecraft.server.MinecraftServer;

public class CommandSetPlayerTimeout extends CommandBase {
   public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
      if (var2.length != 1) {
         throw new WrongUsageException("commands.setidletimeout.usage", new Object[0]);
      } else {
         int var3 = parseInt(var2[0], 0);
         MinecraftServer.getServer().setPlayerIdleTimeout(var3);
         notifyOperators(var1, this, "commands.setidletimeout.success", new Object[]{var3});
      }
   }

   public int getRequiredPermissionLevel() {
      return 3;
   }

   public String getCommandUsage(ICommandSender var1) {
      return "commands.setidletimeout.usage";
   }

   public String getCommandName() {
      return "setidletimeout";
   }
}
