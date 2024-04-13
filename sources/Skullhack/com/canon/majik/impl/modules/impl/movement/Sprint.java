package com.canon.majik.impl.modules.impl.movement;

import com.canon.majik.api.event.eventBus.EventListener;
import com.canon.majik.api.event.events.TickEvent;
import com.canon.majik.api.utils.player.PlayerUtils;
import com.canon.majik.impl.modules.api.Category;
import com.canon.majik.impl.modules.api.Module;
import com.canon.majik.impl.setting.settings.ModeSetting;

public class Sprint extends Module {

    ModeSetting mode = setting("Mode", "Rage", "Rage", "Normal");

    public Sprint(String name, Category category) {
        super(name, category);
    }

    @EventListener
    public void onTick(TickEvent event){
        if(nullCheck()) return;
        if(mode.getValue().equalsIgnoreCase("Rage")){
            mc.player.setSprinting(true);
        }else{
            if(PlayerUtils.isMoving()){
                mc.player.setSprinting(true);
            }
        }
    }
}
