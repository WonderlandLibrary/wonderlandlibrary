package net.minecraft.command;

import java.util.List;
import java.util.Random;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;

public class CommandWeather extends CommandBase {
   public List addTabCompletionOptions(ICommandSender var1, String[] var2, BlockPos var3) {
      return var2.length == 1 ? getListOfStringsMatchingLastWord(var2, new String[]{"clear", "rain", "thunder"}) : null;
   }

   public String getCommandUsage(ICommandSender var1) {
      return "commands.weather.usage";
   }

   public String getCommandName() {
      return "weather";
   }

   public int getRequiredPermissionLevel() {
      return 2;
   }

   public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
      if (var2.length >= 1 && var2.length <= 2) {
         int var3 = (300 + (new Random()).nextInt(600)) * 20;
         if (var2.length >= 2) {
            var3 = parseInt(var2[1], 1, 1000000) * 20;
         }

         WorldServer var4 = MinecraftServer.getServer().worldServers[0];
         WorldInfo var5 = var4.getWorldInfo();
         if ("clear".equalsIgnoreCase(var2[0])) {
            var5.setCleanWeatherTime(var3);
            var5.setRainTime(0);
            var5.setThunderTime(0);
            var5.setRaining(false);
            var5.setThundering(false);
            notifyOperators(var1, this, "commands.weather.clear", new Object[0]);
         } else if ("rain".equalsIgnoreCase(var2[0])) {
            var5.setCleanWeatherTime(0);
            var5.setRainTime(var3);
            var5.setThunderTime(var3);
            var5.setRaining(true);
            var5.setThundering(false);
            notifyOperators(var1, this, "commands.weather.rain", new Object[0]);
         } else {
            if (!"thunder".equalsIgnoreCase(var2[0])) {
               throw new WrongUsageException("commands.weather.usage", new Object[0]);
            }

            var5.setCleanWeatherTime(0);
            var5.setRainTime(var3);
            var5.setThunderTime(var3);
            var5.setRaining(true);
            var5.setThundering(true);
            notifyOperators(var1, this, "commands.weather.thunder", new Object[0]);
         }

      } else {
         throw new WrongUsageException("commands.weather.usage", new Object[0]);
      }
   }
}
