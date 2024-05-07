package com.masterof13fps.features.modules.impl.player;

import com.masterof13fps.features.modules.Category;
import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.manager.eventmanager.impl.EventUpdate;

@ModuleInfo(name = "AirStuck", category = Category.PLAYER, description = "Makes you stuck in the air")
public class AirStuck extends Module {

    @Override
    public void onToggle() {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        getPlayer().isDead = false;
    }

    @Override
    public void onEvent(Event event) {
        if(event instanceof EventUpdate){
            getPlayer().isDead = true;
        }
    }
}
