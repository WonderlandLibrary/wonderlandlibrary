package tech.atani.client.feature.module.impl.render;

import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import tech.atani.client.listener.event.minecraft.network.PacketEvent;
import tech.atani.client.listener.event.minecraft.game.RunTickEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.utility.interfaces.Methods;
import tech.atani.client.feature.value.impl.SliderValue;
import tech.atani.client.feature.value.impl.StringBoxValue;

import java.util.Random;

@ModuleData(name = "Ambience", description = "Allows you to change time and weather", category = Category.RENDER)
public class Ambience extends Module {

    private final SliderValue<Integer> time = new SliderValue<>("Time", "What time will it be?", this, 12000, 1000, 24000, 0);
    private final StringBoxValue weather = new StringBoxValue("Weather", "What weather will it be?", this, new String[] {"Clear", "Rain", "Thunder", "Snow", "Don't Change"});

    private final int randomValue = (300 + (new Random()).nextInt(600)) * 20;
    public static boolean snow = false;

    @Listen
    public void onTick(RunTickEvent runTickEvent) {
        snow = weather.is("Snow");
        if (Methods.mc.theWorld != null) {

            WorldInfo worldinfo = Methods.mc.theWorld.getWorldInfo();
            if (Methods.mc.isSingleplayer()) {
                World world = MinecraftServer.getServer().worldServers[0];
                worldinfo = world.getWorldInfo();
            }

            Methods.mc.theWorld.setWorldTime(time.getValue().longValue());

            switch (weather.getValue()) {
                case "Clear":
                    worldinfo.setCleanWeatherTime(randomValue);
                    worldinfo.setRainTime(0);
                    worldinfo.setThunderTime(0);
                    worldinfo.setRaining(false);
                    worldinfo.setThundering(false);

                    break;
                case "Rain":
                case "Snow":
                    worldinfo.setCleanWeatherTime(0);
                    worldinfo.setRainTime(Integer.MAX_VALUE);
                    worldinfo.setThunderTime(Integer.MAX_VALUE);
                    worldinfo.setRaining(true);
                    worldinfo.setThundering(false);
                    break;
                case "Thunder":
                    worldinfo.setCleanWeatherTime(0);
                    worldinfo.setRainTime(Integer.MAX_VALUE);
                    worldinfo.setThunderTime(Integer.MAX_VALUE);
                    worldinfo.setRaining(true);
                    worldinfo.setThundering(true);
                    break;
            }

        }
    }

    @Listen
    public void onPacket(PacketEvent packetEvent) {
        if (packetEvent.getPacket() instanceof S03PacketTimeUpdate) {
            packetEvent.setCancelled(true);
        }
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

}
