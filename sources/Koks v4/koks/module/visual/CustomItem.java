package koks.module.visual;

import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.api.manager.value.annotation.Value;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "CustomItem", description = "Customize your Item", category = Module.Category.VISUAL)
public class CustomItem extends Module {

    @Value(name = "X", minimum = -0.4, maximum = 0.4)
    double x = 0;

    @Value(name = "Y", minimum = -0.4, maximum = 0.4)
    double y = 0;

    @Value(name = "Z", minimum = -0.4, maximum = 0.4)
    double z = 0;

    @Value(name = "Size", minimum = 0.1, maximum = 0.8)
    double size = 0.4;

    @Value(name = "OnlyBlock")
    boolean onlyBlock = false;

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
