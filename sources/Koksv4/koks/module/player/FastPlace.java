package koks.module.player;

import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.event.UpdateEvent;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "FastPlace", description = "You can place blocks fast", category = Module.Category.PLAYER)
public class FastPlace extends Module {

    @Override
    @Event.Info
    public void onEvent(Event event) {
        if(event instanceof UpdateEvent) {
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
