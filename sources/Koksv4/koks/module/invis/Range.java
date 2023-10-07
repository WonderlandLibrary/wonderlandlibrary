package koks.module.invis;

import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.api.manager.value.annotation.Value;
import koks.event.BlockReachEvent;
import koks.event.MouseOverEvent;

/**
 * Copyright 2021, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "Range", description = "You have more range", category = Module.Category.INVIS)
public class Range extends Module {

    @Value(name = "Range", minimum = 0, maximum = 6)
    double range = 3;

    @Override
    @Event.Info(priority = Event.Priority.HIGH)
    public void onEvent(Event event) {
        if(event instanceof final MouseOverEvent mouseOverEvent) {
            mouseOverEvent.setRange(range);
            mouseOverEvent.setRangeCheck(false);
        }

        if(event instanceof final BlockReachEvent blockReachEvent) {
            blockReachEvent.setRange((float) range);
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
