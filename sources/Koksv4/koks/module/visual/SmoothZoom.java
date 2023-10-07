package koks.module.visual;

import koks.api.event.Event;
import koks.api.manager.value.annotation.Value;
import koks.api.registry.module.Module;

@Module.Info(name = "SmoothZoom", description = "Smooth your zoom", category = Module.Category.VISUAL)
public class SmoothZoom extends Module {

    @Value(name = "Multiplier", minimum = 0.01, maximum = 1)
    double multiplier = 0.15;

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
