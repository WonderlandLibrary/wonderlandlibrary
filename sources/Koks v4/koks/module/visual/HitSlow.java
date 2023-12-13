package koks.module.visual;

import koks.api.event.Event;
import koks.api.manager.value.annotation.Value;
import koks.api.registry.module.Module;

/**
 * @author kroko
 * @created on 22.01.2021 : 17:13
 */

@Module.Info(name = "HitSlow", description = "Its slows your Hit Animation", category = Module.Category.VISUAL)
public class HitSlow extends Module {

    @Value(name = "Slowness", minimum = 1, maximum = 20)
    int slowness = 6;

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
