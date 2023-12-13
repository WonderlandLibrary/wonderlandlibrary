package koks.module.visual;

import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.event.BobbingEvent;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "NoBob", description = "Your hand doesn't bobbing", category = Module.Category.VISUAL)
public class NoBob extends Module{

    @Override
    @Event.Info
    public void onEvent(Event event) {
        if(event instanceof final BobbingEvent bobbingEvent) {
            bobbingEvent.setBobbing(0);
            bobbingEvent.setChangeBobbing(true);
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
