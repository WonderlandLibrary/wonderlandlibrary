package koks.module.combat;

import koks.api.event.Event;
import koks.api.registry.module.Module;

/**
 * Copyright 2021, Koks Team
 * Please don't use the code
 */
@Module.Info(name = "Teams", description = "You doesn't attack your Team members", category = Module.Category.COMBAT)
public class Teams extends Module implements Module.NotBypass {

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
