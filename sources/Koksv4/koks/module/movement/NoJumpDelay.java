package koks.module.movement;

import koks.api.event.Event;
import koks.api.registry.module.Module;

/**
 * @author kroko
 * @created on 20.02.2021 : 04:19
 */

@Module.Info(name = "NoJumpDelay", description = "You have no delay when you jumping.", category = Module.Category.MOVEMENT)
public class NoJumpDelay extends Module {

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
