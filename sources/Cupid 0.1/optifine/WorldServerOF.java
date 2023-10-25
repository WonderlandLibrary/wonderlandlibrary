package optifine;

import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;

public class WorldServerOF extends WorldServer {
  private MinecraftServer mcServer;
  
  public WorldServerOF(MinecraftServer p_i98_1_, ISaveHandler p_i98_2_, WorldInfo p_i98_3_, int p_i98_4_, Profiler p_i98_5_) {
    super(p_i98_1_, p_i98_2_, p_i98_3_, p_i98_4_, p_i98_5_);
    this.mcServer = p_i98_1_;
  }
  
  public void tick() {
    super.tick();
    if (!Config.isTimeDefault())
      fixWorldTime(); 
    if (Config.waterOpacityChanged) {
      Config.waterOpacityChanged = false;
      ClearWater.updateWaterOpacity(Config.getGameSettings(), (World)this);
    } 
  }
  
  protected void updateWeather() {
    if (!Config.isWeatherEnabled())
      fixWorldWeather(); 
    super.updateWeather();
  }
  
  private void fixWorldWeather() {
    if (this.worldInfo.isRaining() || this.worldInfo.isThundering()) {
      this.worldInfo.setRainTime(0);
      this.worldInfo.setRaining(false);
      setRainStrength(0.0F);
      this.worldInfo.setThunderTime(0);
      this.worldInfo.setThundering(false);
      setThunderStrength(0.0F);
      this.mcServer.getConfigurationManager().sendPacketToAllPlayers((Packet)new S2BPacketChangeGameState(2, 0.0F));
      this.mcServer.getConfigurationManager().sendPacketToAllPlayers((Packet)new S2BPacketChangeGameState(7, 0.0F));
      this.mcServer.getConfigurationManager().sendPacketToAllPlayers((Packet)new S2BPacketChangeGameState(8, 0.0F));
    } 
  }
  
  private void fixWorldTime() {
    if (this.worldInfo.getGameType().getID() == 1) {
      long i = getWorldTime();
      long j = i % 24000L;
      if (Config.isTimeDayOnly()) {
        if (j <= 1000L)
          setWorldTime(i - j + 1001L); 
        if (j >= 11000L)
          setWorldTime(i - j + 24001L); 
      } 
      if (Config.isTimeNightOnly()) {
        if (j <= 14000L)
          setWorldTime(i - j + 14001L); 
        if (j >= 22000L)
          setWorldTime(i - j + 24000L + 14001L); 
      } 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\optifine\WorldServerOF.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */