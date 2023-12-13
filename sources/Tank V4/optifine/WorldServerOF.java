package optifine;

import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;

public class WorldServerOF extends WorldServer {
   private MinecraftServer mcServer;

   public WorldServerOF(MinecraftServer var1, ISaveHandler var2, WorldInfo var3, int var4, Profiler var5) {
      super(var1, var2, var3, var4, var5);
      this.mcServer = var1;
   }

   public void tick() {
      super.tick();
      if (!Config.isTimeDefault()) {
         this.fixWorldTime();
      }

      if (Config.waterOpacityChanged) {
         Config.waterOpacityChanged = false;
         ClearWater.updateWaterOpacity(Config.getGameSettings(), this);
      }

   }

   protected void updateWeather() {
      if (!Config.isWeatherEnabled()) {
         this.fixWorldWeather();
      }

      super.updateWeather();
   }

   private void fixWorldWeather() {
      if (this.worldInfo.isRaining() || this.worldInfo.isThundering()) {
         this.worldInfo.setRainTime(0);
         this.worldInfo.setRaining(false);
         this.setRainStrength(0.0F);
         this.worldInfo.setThunderTime(0);
         this.worldInfo.setThundering(false);
         this.setThunderStrength(0.0F);
         this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(2, 0.0F));
         this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(7, 0.0F));
         this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(8, 0.0F));
      }

   }

   private void fixWorldTime() {
      if (this.worldInfo.getGameType().getID() == 1) {
         long var1 = this.getWorldTime();
         long var3 = var1 % 24000L;
         if (Config.isTimeDayOnly()) {
            if (var3 <= 1000L) {
               this.setWorldTime(var1 - var3 + 1001L);
            }

            if (var3 >= 11000L) {
               this.setWorldTime(var1 - var3 + 24001L);
            }
         }

         if (Config.isTimeNightOnly()) {
            if (var3 <= 14000L) {
               this.setWorldTime(var1 - var3 + 14001L);
            }

            if (var3 >= 22000L) {
               this.setWorldTime(var1 - var3 + 24000L + 14001L);
            }
         }
      }

   }
}
