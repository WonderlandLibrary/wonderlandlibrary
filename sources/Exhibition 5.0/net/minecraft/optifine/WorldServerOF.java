// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.optifine;

import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.world.World;
import net.minecraft.profiler.Profiler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class WorldServerOF extends WorldServer
{
    private MinecraftServer mcServer;
    
    public WorldServerOF(final MinecraftServer par1MinecraftServer, final ISaveHandler par2iSaveHandler, final WorldInfo worldInfo, final int par4, final Profiler par6Profiler) {
        super(par1MinecraftServer, par2iSaveHandler, worldInfo, par4, par6Profiler);
        this.mcServer = par1MinecraftServer;
    }
    
    @Override
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
    
    @Override
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
            this.setRainStrength(0.0f);
            this.worldInfo.setThunderTime(0);
            this.worldInfo.setThundering(false);
            this.setThunderStrength(0.0f);
            this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(2, 0.0f));
            this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(7, 0.0f));
            this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(8, 0.0f));
        }
    }
    
    private void fixWorldTime() {
        if (this.worldInfo.getGameType().getID() == 1) {
            final long time = this.getWorldTime();
            final long timeOfDay = time % 24000L;
            if (Config.isTimeDayOnly()) {
                if (timeOfDay <= 1000L) {
                    this.setWorldTime(time - timeOfDay + 1001L);
                }
                if (timeOfDay >= 11000L) {
                    this.setWorldTime(time - timeOfDay + 24001L);
                }
            }
            if (Config.isTimeNightOnly()) {
                if (timeOfDay <= 14000L) {
                    this.setWorldTime(time - timeOfDay + 14001L);
                }
                if (timeOfDay >= 22000L) {
                    this.setWorldTime(time - timeOfDay + 24000L + 14001L);
                }
            }
        }
    }
}
