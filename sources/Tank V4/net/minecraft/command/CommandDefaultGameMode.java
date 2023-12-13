package net.minecraft.command;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.WorldSettings;

public class CommandDefaultGameMode extends CommandGameMode {
   protected void setGameType(WorldSettings.GameType var1) {
      MinecraftServer var2 = MinecraftServer.getServer();
      var2.setGameType(var1);
      EntityPlayerMP var3;
      if (var2.getForceGamemode()) {
         for(Iterator var4 = MinecraftServer.getServer().getConfigurationManager().func_181057_v().iterator(); var4.hasNext(); var3.fallDistance = 0.0F) {
            var3 = (EntityPlayerMP)var4.next();
            var3.setGameType(var1);
         }
      }

   }

   public String getCommandName() {
      return "defaultgamemode";
   }

   public String getCommandUsage(ICommandSender var1) {
      return "commands.defaultgamemode.usage";
   }

   public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
      if (var2.length <= 0) {
         throw new WrongUsageException("commands.defaultgamemode.usage", new Object[0]);
      } else {
         WorldSettings.GameType var3 = this.getGameModeFromCommand(var1, var2[0]);
         this.setGameType(var3);
         notifyOperators(var1, this, "commands.defaultgamemode.success", new Object[]{new ChatComponentTranslation("gameMode." + var3.getName(), new Object[0])});
      }
   }
}
