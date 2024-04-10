package info.sigmaclient.sigma.modules.world;

import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.ModeValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.net.PacketEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import net.minecraft.network.play.server.SUpdateTimePacket;
import top.fl0wowp4rty.phantomshield.annotations.Native;

public class Weather extends Module {
    BooleanValue customTime = new BooleanValue("CustomTime", true);
    NumberValue time = new NumberValue("Time", 60000, 0, 130000, NumberValue.NUMBER_TYPE.INT);
    ModeValue weather = new ModeValue("Weather", "Clear", new String[]{"Clear", "Rain", "Thunder"});
    public Weather() {
        super("Weather", Category.World, "Removes rain and changes the world's time.");
     registerValue(customTime);
     registerValue(time);
     registerValue(weather);
    }
    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPost()){
            if(customTime.isEnable()) {
                mc.world.setDayTime(time.getValue().longValue());
            }
            if(weather.is("Clear")) {
                mc.world.setRainStrength(0);
                mc.world.setThunderStrength(0);
            }
            if(weather.is("Thunder")) {
                mc.world.setRainStrength(0);
                mc.world.setThunderStrength(0.5f);
            }
            if(weather.is("Rain")) {
                mc.world.setRainStrength(0.5f);
                mc.world.setThunderStrength(0);
            }
        }
        super.onUpdateEvent(event);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onPacketEvent(PacketEvent event) {
        if(event.packet instanceof SUpdateTimePacket){
            event.cancelable = true;
            if(!customTime.isEnable()){
                mc.world.setDayTime(((SUpdateTimePacket) event.packet).getWorldTime());
            }
        }
        super.onPacketEvent(event);
    }
}
