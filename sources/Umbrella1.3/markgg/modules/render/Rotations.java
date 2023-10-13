/*
 * Decompiled with CFR 0.150.
 */
package markgg.modules.render;

import markgg.events.Event;
import markgg.events.listeners.EventMotion;
import markgg.modules.Module;

public class Rotations
extends Module {
    public Rotations() {
        super("Rotations", 0, Module.Category.RENDER);
    }

    @Override
    public void onEvent(Event e) {
        if (e instanceof EventMotion) {
            EventMotion event = (EventMotion)e;
            this.mc.thePlayer.rotationYawHead = event.getYaw();
            this.mc.thePlayer.renderYawOffset = event.getYaw();
        }
    }
}

