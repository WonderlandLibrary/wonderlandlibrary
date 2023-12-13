/*
 * Decompiled with CFR 0.150.
 */
package markgg.modules.player;

import markgg.events.Event;
import markgg.events.listeners.EventMotion;
import markgg.modules.Module;
import markgg.settings.NumberSetting;

public class Timer
extends Module {
    public NumberSetting Speed = new NumberSetting("Speed", 1.0, 0.05, 4.0, 0.05);

    public Timer() {
        super("Timer", 0, Module.Category.PLAYER);
        this.addSettings(this.Speed);
    }

    @Override
    public void onEvent(Event e) {
        if (e instanceof EventMotion && e.isPre()) {
            this.mc.timer.timerSpeed = (float)this.Speed.getValue();
        }
    }

    @Override
    public void onDisable() {
        this.mc.timer.timerSpeed = 1.0f;
    }
}

