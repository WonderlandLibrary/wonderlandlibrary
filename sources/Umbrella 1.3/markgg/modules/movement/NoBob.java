/*
 * Decompiled with CFR 0.150.
 */
package markgg.modules.movement;

import markgg.events.Event;
import markgg.events.listeners.EventMotion;
import markgg.modules.Module;

public class NoBob
extends Module {
    public NoBob() {
        super("NoBob", 0, Module.Category.MOVEMENT);
    }

    @Override
    public void onEvent(Event e) {
        if (e instanceof EventMotion) {
            this.mc.thePlayer.distanceWalkedModified = 0.0f;
            this.mc.gameSettings.viewBobbing = true;
        }
    }
}

