package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldSettings;

public class CommandPublishLocalServer extends CommandBase {
   public String getCommandName() {
      return "publish";
   }

   public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
      String var3 = MinecraftServer.getServer().shareToLAN(WorldSettings.GameType.SURVIVAL, false);
      if (var3 != null) {
         notifyOperators(var1, this, "commands.publish.started", new Object[]{var3});
      } else {
         notifyOperators(var1, this, "commands.publish.failed", new Object[0]);
      }

   }

   public String getCommandUsage(ICommandSender var1) {
      return "commands.publish.usage";
   }
}
