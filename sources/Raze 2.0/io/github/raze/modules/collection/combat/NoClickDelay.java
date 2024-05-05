package io.github.raze.modules.collection.combat;

import io.github.raze.events.collection.motion.EventMotion;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;

public class NoClickDelay extends AbstractModule {

    public NoClickDelay() {
        super("NoClickDelay", "Removes hit delay.", ModuleCategory.COMBAT);
    }

    @Listen
    public void onMotion(EventMotion eventMotion) { mc.leftClickCounter = 0; }

}
