package koks.module.visual;

import koks.api.event.Event;
import koks.api.registry.module.Module;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "Fullbright", description = "You can see in darkness", category = Module.Category.VISUAL)
public class Fullbright extends Module {

    float gamma;

    @Override
    @Event.Info
    public void onEvent(Event event) {

    }

    @Override
    public void onEnable() {
        gamma = getGameSettings().gammaSetting;
        getGameSettings().gammaSetting = 1000;
    }

    @Override
    public void onDisable() {
        getGameSettings().gammaSetting = gamma;
    }
}
