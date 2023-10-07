package koks.module.visual;

import koks.api.event.Event;
import koks.api.registry.module.Module;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "NameProtect", description = "Your name is protected", category = Module.Category.VISUAL)
public class NameProtect extends Module {

    public NameProtect() {
        setToggled(true);
    }

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
