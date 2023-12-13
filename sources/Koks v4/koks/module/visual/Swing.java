package koks.module.visual;

import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.api.manager.value.annotation.Value;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "Swing", description = "You have a custom swing animation", category = Module.Category.VISUAL)
public class Swing extends Module {

    @Value(name = "Mode", modes = {"Animation1", "Animation2", "Animation3", "Animation4", "Animation5", "Push", "Spin", "Astolfo", "Fly", "1.7 Animation"})
    String mode = "1.7 Animation";

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
