package com.masterof13fps.features.modules.impl.player;

import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.manager.eventmanager.impl.EventUpdate;
import com.masterof13fps.features.modules.Category;

@ModuleInfo(name = "AutoWalk", category = Category.PLAYER, description = "Automatically walks for you")
public class AutoWalk extends Module {
    @Override
    public void onToggle() {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        mc.gameSettings.keyBindForward.pressed = false;
    }

    @Override
    public void onEvent(Event event) {
        if(event instanceof EventUpdate) {
            mc.gameSettings.keyBindForward.pressed = true;
        }
    }
}
