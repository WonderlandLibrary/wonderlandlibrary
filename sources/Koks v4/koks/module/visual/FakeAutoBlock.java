package koks.module.visual;

import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.api.manager.value.annotation.Value;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "FakeAutoBlock", description = "You block always", category = Module.Category.VISUAL)
public class FakeAutoBlock extends Module {

    @Value(name = "OnlySword")
    boolean onlySword = true;

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
