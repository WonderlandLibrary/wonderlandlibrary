package koks.module.player;

import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.event.HeadLookEvent;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "NoRotate", description = "You doesn't rotate to the block after flagging", category = Module.Category.PLAYER)
public class NoRotate extends Module {

    @Override
    @Event.Info(priority = Event.Priority.EXTREME)
    public void onEvent(Event event) {
        if(event instanceof final HeadLookEvent headLookEvent) {
            headLookEvent.setYaw(getPlayer().rotationYaw);
            headLookEvent.setPitch(getPlayer().rotationPitch);
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
