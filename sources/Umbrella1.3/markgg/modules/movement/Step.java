/*
 * Decompiled with CFR 0.150.
 */
package markgg.modules.movement;

import markgg.events.Event;
import markgg.events.listeners.EventMotion;
import markgg.events.listeners.EventUpdate;
import markgg.modules.Module;
import markgg.settings.ModeSetting;

public class Step
extends Module {
    public ModeSetting mode = new ModeSetting("Mode", "Normal", "Normal", "Motion");

    public Step() {
        super("Step", 0, Module.Category.MOVEMENT);
        this.addSettings(this.mode);
    }

    @Override
    public void onDisable() {
        this.mc.thePlayer.stepHeight = 0.5f;
    }

    @Override
    public void onEvent(Event e) {
        if (e instanceof EventUpdate && this.mode.is("Normal")) {
            this.mc.thePlayer.stepHeight = 1.0f;
        }
        if (e instanceof EventMotion && e.isPre() && this.mode.is("Motion") && this.mc.thePlayer.isCollidedHorizontally) {
            this.mc.thePlayer.motionY = 0.2f;
        }
    }
}

