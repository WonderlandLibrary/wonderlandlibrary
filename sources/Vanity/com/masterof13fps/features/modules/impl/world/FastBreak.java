package com.masterof13fps.features.modules.impl.world;

import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.features.modules.Category;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.manager.eventmanager.impl.EventTick;

@ModuleInfo(name = "FastBreak", category = Category.WORLD, description = "Breaks blocks faster than usual")
public class FastBreak extends Module {

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
        if(event instanceof EventTick){
            if (mc.playerController.curBlockDamageMP > 0.8F) {
                mc.playerController.curBlockDamageMP = 1.0F;
            }
            mc.playerController.blockHitDelay = 0;
        }
    }
}
