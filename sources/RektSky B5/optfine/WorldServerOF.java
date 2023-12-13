/*
 * Decompiled with CFR 0.152.
 */
package optfine;

import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import optfine.ClearWater;
import optfine.Config;

public class WorldServerOF
extends WorldServer {
    private MinecraftServer mcServer;

    public WorldServerOF(MinecraftServer p_i64_1_, ISaveHandler p_i64_2_, WorldInfo p_i64_3_, int p_i64_4_, Profiler p_i64_5_) {
        super(p_i64_1_, p_i64_2_, p_i64_3_, p_i64_4_, p_i64_5_);
        this.mcServer = p_i64_1_;
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
            long i2 = this.getWorldTime();
            long j2 = i2 % 24000L;
            if (Config.isTimeDayOnly()) {
                if (j2 <= 1000L) {
                    this.setWorldTime(i2 - j2 + 1001L);
                }
                if (j2 >= 11000L) {
                    this.setWorldTime(i2 - j2 + 24001L);
                }
            }
            if (Config.isTimeNightOnly()) {
                if (j2 <= 14000L) {
                    this.setWorldTime(i2 - j2 + 14001L);
                }
                if (j2 >= 22000L) {
                    this.setWorldTime(i2 - j2 + 24000L + 14001L);
                }
            }
        }
    }
}

