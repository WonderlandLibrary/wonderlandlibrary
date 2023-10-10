package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandStop extends CommandBase {
   public String getCommandUsage(ICommandSender var1) {
      return "commands.stop.usage";
   }

   public String getCommandName() {
      return "stop";
   }

   public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
      if (MinecraftServer.getServer().worldServers != null) {
         notifyOperators(var1, this, "commands.stop.start", new Object[0]);
      }

      MinecraftServer.getServer().initiateShutdown();
   }
}
