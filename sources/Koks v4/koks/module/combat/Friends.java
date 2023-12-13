package koks.module.combat;

import koks.api.event.Event;
import koks.api.registry.module.Module;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "Friends", description = "You doesn't attack your friends", category = Module.Category.COMBAT)
public class Friends extends Module implements Module.NotBypass {

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
