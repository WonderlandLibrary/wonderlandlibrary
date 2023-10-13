/*
 * Decompiled with CFR 0.150.
 */
package markgg.modules.player;

import markgg.events.Event;
import markgg.events.listeners.EventMotion;
import markgg.modules.Module;

public class FastPlace
extends Module {
    public FastPlace() {
        super("FastPlace", 0, Module.Category.PLAYER);
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
        this.mc.rightClickDelayTimer = 6;
    }

    @Override
    public void onEvent(Event e) {
        if (e instanceof EventMotion) {
            this.mc.rightClickDelayTimer = 0;
        }
    }
}

