package koks.module.movement;

import koks.api.event.Event;
import koks.api.manager.value.annotation.Value;
import koks.api.registry.module.Module;

/**
 * @author kroko
 * @created on 02.02.2021 : 19:38
 */

@Module.Info(name = "NoHitSlow", description = "You doesn't slow while hitting players", category = Module.Category.MOVEMENT)
public class NoHitSlow extends Module {

    @Value(name = "Slowness", minimum = 0, maximum = 1)
    double slowness = 0.6;

    @Value(name = "Sprinting")
    boolean sprinting = true;

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
