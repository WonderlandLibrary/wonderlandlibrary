package com.masterof13fps.features.modules.impl.movement;

import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.manager.eventmanager.impl.EventSafeWalk;
import com.masterof13fps.features.modules.Category;

@ModuleInfo(name = "SafeWalk", category = Category.MOVEMENT, description = "Automatically stops at the end of a block")
public class SafeWalk extends Module {

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
        if(event instanceof EventSafeWalk)
            ((EventSafeWalk) event).setSafe(true);
    }
}
