package me.dev.legacy.features.modules.client;

import me.dev.legacy.event.events.ClientEvent;
import me.dev.legacy.event.events.UpdateEvent;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import me.dev.legacy.util.ColorHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Colors extends Module {

    public static Setting<Object> saturation;

    public Colors () {
        super ("Colors", "Colors for sync.", Category.CLIENT, true, false,false);
    }

    Setting<Integer> speed = this.register(new Setting("Speed", 100, 1, 150));
    Setting<Integer> brightness = this.register(new Setting("Brightness", 255, 0, 255));

    Setting<Integer> red = this.register(new Setting("Red", 255, 0, 255));
    Setting<Integer> green = this.register(new Setting("Green", 255, 0, 255));
    Setting<Integer> blue = this.register(new Setting("Blue", 255, 0, 255));

    int ticks;

    @SubscribeEvent
    public void onUpdate(UpdateEvent event) {
        if (ticks++ < 10) {
            ColorHandler.setColor(red.getValue(), green.getValue(), blue.getValue());
        }
    }

    @SubscribeEvent
    public void onClientEvent(ClientEvent event) {
        if (event.getProperty() == red || event.getProperty() == green || event.getProperty() == blue) {
            ColorHandler.setColor(red.getValue(), green.getValue(), blue.getValue());
        }
    }

}
