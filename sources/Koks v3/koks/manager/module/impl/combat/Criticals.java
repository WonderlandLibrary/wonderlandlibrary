package koks.manager.module.impl.combat;

import koks.manager.event.Event;
import koks.manager.event.impl.EventUpdate;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;

/**
 * @author kroko
 * @created on 11.10.2020 : 13:57
 */

@ModuleInfo(name = "Criticals", description = "Every hit is a critical hit", category = Module.Category.COMBAT)
public class Criticals extends Module {

    @Override
    public void onEvent(Event event) {
        if (!this.isToggled())
            return;

        if (event instanceof EventUpdate) {
            if (getPlayer().onGround)
                getPlayer().jump();
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
