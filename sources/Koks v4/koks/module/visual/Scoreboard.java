package koks.module.visual;

import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.api.manager.value.annotation.Value;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "Scoreboard", description = "Its hide the Scoreboard", category = Module.Category.VISUAL)
public class Scoreboard extends Module {

    @Value(name = "Numbers")
    boolean numbers = false;

    @Override
    @Event.Info
    public void onEvent(Event event) {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
