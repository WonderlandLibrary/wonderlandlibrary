package com.canon.majik.impl.modules.impl.render;

import com.canon.majik.api.event.eventBus.EventListener;
import com.canon.majik.api.event.events.TickEvent;
import com.canon.majik.impl.modules.api.Category;
import com.canon.majik.impl.modules.api.Module;
import com.canon.majik.impl.setting.settings.NumberSetting;

public class TimeChanger extends Module {

    NumberSetting time = setting("Timer", 1000, -24000, 24000);

    public TimeChanger(String name, Category category) {
        super(name, category);
    }

    @Override
    public void onEnable() {
        if(nullCheck()) return;
        mc.world.setWorldTime(time.getValue().intValue());
        super.onEnable();
    }

    @EventListener
    public void onTick(TickEvent event){
        if(nullCheck()) return;
        mc.world.setWorldTime(time.getValue().intValue());
    }
}
