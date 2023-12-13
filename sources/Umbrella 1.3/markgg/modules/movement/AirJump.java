/*
 * Decompiled with CFR 0.150.
 */
package markgg.modules.movement;

import markgg.events.Event;
import markgg.events.listeners.EventUpdate;
import markgg.modules.Module;

public class AirJump
extends Module {
    public AirJump() {
        super("AirJump", 0, Module.Category.MOVEMENT);
    }

    @Override
    public void onEvent(Event e) {
        if (e instanceof EventUpdate) {
            this.mc.thePlayer.onGround = true;
        }
    }
}

