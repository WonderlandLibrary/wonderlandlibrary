package koks.manager.module.impl.movement;

import koks.api.settings.Setting;
import koks.manager.event.Event;
import koks.manager.event.impl.EventUpdate;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;

/**
 * @author kroko
 * @created on 01.11.2020 : 00:49
 */

@ModuleInfo(name = "WallSpeed", description = "You are fast on walls", category = Module.Category.MOVEMENT)
public class WallSpeed extends Module {

    public Setting mode = new Setting("Mode", new String[]{"Intave"}, "Intave", this);


    float intaveSpeed;

    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        switch (mode.getCurrentMode()) {
            case "Intave":
                if (event instanceof EventUpdate) {
                    if (getPlayer().isCollidedHorizontally) {
                        intaveSpeed+= randomUtil.getRandomFloat(20,23);
                        getPlayer().setSprinting(true);
                        intaveSpeed /= 69;
                        movementUtil.setSpeed(intaveSpeed, true);
                    }else{
                    }
                }
                break;
        }
    }

    @Override
    public void onEnable() {
        intaveSpeed = 0F;
    }

    @Override
    public void onDisable() {
        intaveSpeed = 0F;
    }
}
