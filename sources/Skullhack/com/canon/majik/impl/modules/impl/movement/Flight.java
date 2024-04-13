package com.canon.majik.impl.modules.impl.movement;

import com.canon.majik.api.event.eventBus.EventListener;
import com.canon.majik.api.event.events.TickEvent;
import com.canon.majik.impl.modules.api.Category;
import com.canon.majik.impl.modules.api.Module;
import com.canon.majik.impl.setting.settings.NumberSetting;

import java.util.concurrent.ThreadLocalRandom;

public class Flight extends Module {

    NumberSetting speed = setting("Speed", 1, 1, 10);

    public Flight(String name, Category category) {
        super(name, category);
    }

    @EventListener
    public void onTick(TickEvent event){
        if(nullCheck()) return;
        mc.player.capabilities.isFlying = true;
        mc.player.capabilities.allowFlying = ThreadLocalRandom.current().nextBoolean();
        mc.player.capabilities.setFlySpeed(speed.getValue().floatValue());
    }

    @Override
    public void onDisable() {
        if(nullCheck()) return;
        mc.player.capabilities.isFlying = false;
        mc.player.capabilities.allowFlying = false;
    }
}
