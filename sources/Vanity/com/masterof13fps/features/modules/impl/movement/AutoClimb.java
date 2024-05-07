package com.masterof13fps.features.modules.impl.movement;

import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.manager.eventmanager.impl.EventUpdate;
import com.masterof13fps.features.modules.Category;

@ModuleInfo(name = "AutoClimb", category = Category.MOVEMENT, description = "Automatically climbs ladders")
public class AutoClimb extends Module {
    @Override
    public void onToggle() {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onEvent(Event event) {
     if(event instanceof EventUpdate) {
         if (mc.thePlayer.isOnLadder()) {
             mc.thePlayer.motionY += 0.1D;
         }
     }
    }
}
