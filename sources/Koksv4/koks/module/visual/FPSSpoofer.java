package koks.module.visual;

import koks.api.event.Event;
import koks.api.manager.value.annotation.Value;
import koks.api.registry.module.Module;
import koks.event.RunGameLoopEvent;

@Module.Info(name = "FPSSpoofer", description = "You can spoof your fps", category = Module.Category.VISUAL)
public class FPSSpoofer extends Module {

    @Value(name = "Multiplier", minimum = 0.1, maximum = 1000)
    double multiplier = 1;

    @Override
    @Event.Info
    public void onEvent(Event event) {
        if(event instanceof final RunGameLoopEvent runGameLoopEvent) {
            runGameLoopEvent.setFpsMultiplier(multiplier);
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
