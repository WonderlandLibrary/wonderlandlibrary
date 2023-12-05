package koks.manager.module.impl.movement;

import koks.manager.event.Event;
import koks.manager.event.impl.EventUpdate;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 20:10
 */

@ModuleInfo(name = "Sprint", description = "Sprints automatically", category = Module.Category.MOVEMENT)
public class Sprint extends Module {

    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if (event instanceof EventUpdate) {
            mc.gameSettings.keyBindSprint.pressed = true;
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        mc.thePlayer.setSprinting(false);
        mc.gameSettings.keyBindSprint.pressed = false;
    }

}