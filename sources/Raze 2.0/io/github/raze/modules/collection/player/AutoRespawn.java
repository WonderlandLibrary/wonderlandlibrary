package io.github.raze.modules.collection.player;

import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;

public class AutoRespawn extends AbstractModule {

    public AutoRespawn() {
        super("AutoRespawn", "Respawns automatically when you die.", ModuleCategory.PLAYER);
    }

    @Listen
    public void onMotion(EventMotion eventMotion) {
        if (eventMotion.getState() == Event.State.PRE) {
            if (mc.thePlayer.isDead)
                mc.thePlayer.respawnPlayer();
        }
    }
}
