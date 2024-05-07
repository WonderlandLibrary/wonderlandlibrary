package com.masterof13fps.features.modules.impl.movement;

import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.utils.entity.EntityUtils;
import com.masterof13fps.utils.entity.PlayerUtil;
import com.masterof13fps.manager.settingsmanager.Setting;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.manager.eventmanager.impl.EventMotion;
import com.masterof13fps.features.modules.Category;

@ModuleInfo(name = "Strafe", category = Category.MOVEMENT, description = "Fixes your strafings when you're in air")
public class Strafe extends Module {

    public Setting mode = new Setting("Mode", this, "NCP", new String[] {"NCP", "AAC"});

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
        if (event instanceof EventMotion) {
            setDisplayName("Strafe [" + mode + "]");

            switch (mode.getCurrentMode()) {
                case "NCP": {
                    doNCP();
                    break;
                }
                case "AAC": {
                    doAAC();
                    break;
                }
            }
        }
    }

    private void doAAC() {
        if (isMoving()) {
            if (mc.gameSettings.keyBindJump.pressed) {
                PlayerUtil.setSpeed(0.23D);
            } else {
                PlayerUtil.setSpeed(0.135D);
            }
        }
    }

    private void doNCP() {
        if (isMoving()) {
            if (mc.gameSettings.keyBindJump.pressed) {
                PlayerUtil.setSpeed(0.26D);
            } else {
                PlayerUtil.setSpeed(0.17D);
            }
        }
    }
}