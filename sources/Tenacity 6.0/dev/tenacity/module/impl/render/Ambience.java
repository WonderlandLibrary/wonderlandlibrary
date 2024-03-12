// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render;

import net.minecraft.network.play.server.S03PacketTimeUpdate;
import dev.tenacity.event.impl.network.PacketReceiveEvent;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.server.MinecraftServer;
import dev.tenacity.event.impl.game.TickEvent;
import dev.tenacity.module.settings.Setting;
import java.util.Random;
import dev.tenacity.module.Category;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.Module;

public class Ambience extends Module
{
    private final NumberSetting time;
    private final ModeSetting weather;
    public static boolean overrideSnow;
    private final int randomValue;
    String mode;
    
    public Ambience() {
        super("Ambience", "Ambience", Category.RENDER, "world time");
        this.time = new NumberSetting("Time", 12000.0, 24000.0, 0.0, 1000.0);
        this.weather = new ModeSetting("Weather", "Clear", new String[] { "Rain", "Thunder", "Clear", "Snow", "Don't Change" });
        this.randomValue = (300 + new Random().nextInt(600)) * 20;
        this.mode = "";
        this.addSettings(this.time, this.weather);
    }
    
    @Override
    public void onTickEvent(final TickEvent event) {
        if (Ambience.mc.theWorld != null) {
            WorldInfo worldinfo = Ambience.mc.theWorld.getWorldInfo();
            if (Ambience.mc.isSingleplayer()) {
                final World world = MinecraftServer.getServer().worldServers[0];
                worldinfo = world.getWorldInfo();
            }
            Ambience.mc.theWorld.setWorldTime(this.time.getValue().longValue());
            final String mode = this.weather.getMode();
            switch (mode) {
                case "Clear": {
                    worldinfo.setCleanWeatherTime(this.randomValue);
                    worldinfo.setRainTime(0);
                    worldinfo.setThunderTime(0);
                    worldinfo.setRaining(false);
                    worldinfo.setThundering(false);
                    break;
                }
                case "Rain": {
                    worldinfo.setRainTime(Integer.MAX_VALUE);
                    worldinfo.setThunderTime(Integer.MAX_VALUE);
                    worldinfo.setRaining(true);
                    worldinfo.setThundering(false);
                    break;
                }
                case "Thunder": {
                    worldinfo.setCleanWeatherTime(0);
                    worldinfo.setRainTime(Integer.MAX_VALUE);
                    worldinfo.setThunderTime(Integer.MAX_VALUE);
                    worldinfo.setRaining(true);
                    worldinfo.setThundering(true);
                    break;
                }
                case "Don't Change": {
                    this.mode = "Don't Change";
                    break;
                }
                case "Snow": {
                    worldinfo.setCleanWeatherTime(0);
                    worldinfo.setRainTime(Integer.MAX_VALUE);
                    worldinfo.setThunderTime(Integer.MAX_VALUE);
                    worldinfo.setRaining(true);
                    worldinfo.setThundering(false);
                    break;
                }
            }
            Ambience.overrideSnow = this.weather.is("Snow");
        }
    }
    
    @Override
    public void onPacketReceiveEvent(final PacketReceiveEvent e) {
        if (e.getPacket() instanceof S03PacketTimeUpdate) {
            e.cancel();
        }
    }
    
    @Override
    public void onDisable() {
        Ambience.overrideSnow = false;
        super.onDisable();
    }
    
    static {
        Ambience.overrideSnow = false;
    }
}
