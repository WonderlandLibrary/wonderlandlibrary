package koks.module.movement;

import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.event.NoClipEvent;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "NoClip", description = "You can clip trough walls", category = Module.Category.MOVEMENT)
public class NoClip extends Module {

    @Override
    @Event.Info
    public void onEvent(Event event) {
        if(event instanceof NoClipEvent noClipEvent) {
            noClipEvent.setNoClip(true);
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
