package koks.manager.module.impl.player;

import koks.manager.event.Event;
import koks.manager.event.impl.EventUpdate;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;

/**
 * @author kroko
 * @created on 07.10.2020 : 16:27
 */

@ModuleInfo(name = "FastPlace", category = Module.Category.PLAYER, description = "You can place fast")
public class FastPlace extends Module {

    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if(event instanceof EventUpdate) {
            mc.rightClickDelayTimer = 0;
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
