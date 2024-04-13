package com.canon.majik.impl.modules.impl.movement;

import com.canon.majik.api.event.eventBus.EventListener;
import com.canon.majik.api.event.events.TickEvent;
import com.canon.majik.impl.modules.api.Category;
import com.canon.majik.impl.modules.api.Module;
import com.canon.majik.impl.setting.settings.ModeSetting;
import com.canon.majik.impl.setting.settings.NumberSetting;

public class Step extends Module {

    NumberSetting height = setting("Height", 2, 1, 5);
    ModeSetting mode = setting("Mode", "Vanilla", "Vanilla", "NCP");

    public Step(String name, Category category) {
        super(name, category);
    }

    @EventListener
    public void onTick(TickEvent event){
        if(nullCheck()) return;
        if(mode.getValue().equalsIgnoreCase("Vanilla")){
            mc.player.stepHeight = height.getValue().floatValue();
        }else{
            //ToDo
        }
    }
}
