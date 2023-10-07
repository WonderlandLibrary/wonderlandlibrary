package koks.module.movement;

import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.event.SafeWalkEvent;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "SafeWalk", description = "You cant fall in the void", category = Module.Category.MOVEMENT)
public class SafeWalk extends Module {

    @Override
    @Event.Info
    public void onEvent(Event event) {
        if(event instanceof final SafeWalkEvent safeWalkEvent) {
            safeWalkEvent.setSafe(getPlayer().onGround);
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
