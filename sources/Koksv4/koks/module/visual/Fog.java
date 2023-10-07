package koks.module.visual;

import koks.api.event.Event;
import koks.api.manager.value.annotation.Value;
import koks.api.registry.module.Module;

import java.awt.*;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "Fog", description = "Colored Fog", category = Module.Category.VISUAL)
public class Fog extends Module {

    @Value(name = "Color", colorPicker = true)
    int color = Color.green.getRGB();

    @Value(name = "Distance", minimum = 0, maximum = 8)
    double distance = 3;

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
