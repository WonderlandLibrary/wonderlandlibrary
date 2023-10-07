package koks.module.player;

import koks.api.event.Event;
import koks.api.registry.module.Module;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "SendPublic", description = "You send messages when you toggle a module", category = Module.Category.PLAYER)
public class SendPublic extends Module {

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
