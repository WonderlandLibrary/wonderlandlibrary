package io.github.raze.modules.collection.player;

import io.github.raze.Raze;
import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import io.github.raze.settings.collection.NumberSetting;

public class Timer extends AbstractModule {

    private final NumberSetting timer;

    public Timer() {
        super("Timer", "Speeds up or slows down minecraft.", ModuleCategory.PLAYER);

        Raze.INSTANCE.managerRegistry.settingRegistry.add(

                timer = new NumberSetting(this, "Timer Speed", 0.1, 10, 1)

        );
    }

    @Listen
    public void onMotion(EventMotion eventMotion) {
        if (eventMotion.getState() == Event.State.PRE) {
                mc.timer.timerSpeed = (float) timer.get().doubleValue();
        }
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1;
    }

}
