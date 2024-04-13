package com.canon.majik.impl.modules.impl.movement;

import com.canon.majik.api.event.eventBus.EventListener;
import com.canon.majik.api.event.events.MoveEvent;
import com.canon.majik.api.utils.player.PlayerUtils;
import com.canon.majik.impl.modules.api.Category;
import com.canon.majik.impl.modules.api.Module;
import com.canon.majik.impl.setting.settings.BooleanSetting;
import com.canon.majik.impl.setting.settings.ModeSetting;
import com.canon.majik.impl.setting.settings.NumberSetting;

public class Speed extends Module {

    ModeSetting mode = setting("Mode", "Strafe", "Strafe", "Ground");
    NumberSetting speedSet = setting("Speed", 1.394, 1, 2);
    BooleanSetting strict = setting("Strict", false);

    public Speed(String name, Category category) {
        super(name, category);
    }

    //SPENT AN HOUR ON THIS JUST FOR IT TO GO BACKWARDS :angry:
    @EventListener
    public void onMove(MoveEvent event){
        if(nullCheck()) return;
        float speed = speedSet.getValue().floatValue();
        float[] speedDirection = PlayerUtils.strafe(PlayerUtils.getBaseMoveSpeed(speed));
        if(mode.getValue().equalsIgnoreCase("Strafe")) {
            if (PlayerUtils.isMoving()) {
                if (mc.player.onGround) {
                    if (strict.getValue()) {
                        mc.player.motionY = 0.5;
                        event.setY(0.5);
                    } else {
                        mc.player.motionY = 0.3999999463558197;
                        event.setY(0.3999999463558197);
                    }
                }
                event.setX(speedDirection[0]);
                event.setZ(speedDirection[1]);
            }else {
                event.setX(0);
                event.setZ(0);
            }
        }else{
            event.setX(speedDirection[0]);
            event.setZ(speedDirection[1]);
        }
    }
}
