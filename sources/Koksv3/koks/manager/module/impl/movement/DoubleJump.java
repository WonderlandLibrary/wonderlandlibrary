package koks.manager.module.impl.movement;

import koks.manager.event.Event;
import koks.manager.event.impl.EventUpdate;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;

/**
 * @author kroko
 * @created on 30.11.2020 : 03:11
 */

@ModuleInfo(name = "DoubleJump", category = Module.Category.MOVEMENT, description = "You can jump in the air")
public class DoubleJump extends Module {

    @Override
    public void onEvent(Event event) {
        if(!this.isToggled())
            return;
        if(event instanceof EventUpdate) {
            getPlayer().onGround = true;
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
