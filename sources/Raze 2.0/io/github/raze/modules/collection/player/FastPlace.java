package io.github.raze.modules.collection.player;

import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;

public class FastPlace extends AbstractModule {

    public FastPlace() {
        super("FastPlace", "Enables you to place blocks faster.", ModuleCategory.PLAYER);
    }

    @Listen
    public void onMotion(EventMotion eventMotion) {
        if (eventMotion.getState() == Event.State.PRE) {
            mc.rightClickDelayTimer = 0;
        }
    }

    @Override
    public void onDisable() { mc.rightClickDelayTimer = 6; }

}