package com.masterof13fps.features.modules.impl.movement;

import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.manager.settingsmanager.Setting;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.manager.eventmanager.impl.EventUpdate;
import com.masterof13fps.features.modules.Category;

@ModuleInfo(name = "HighJump", category = Category.MOVEMENT, description = "Lets you jump higher than usual")
public class HighJump extends Module {

    public Setting boost = new Setting("Boost", this,0.5, 0.1, 50, false);

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
            if (mc.gameSettings.keyBindJump.pressed && mc.gameSettings.keyBindForward.pressed) {
                mc.thePlayer.motionY = boost.getCurrentValue();
            }
        }
    }
}
