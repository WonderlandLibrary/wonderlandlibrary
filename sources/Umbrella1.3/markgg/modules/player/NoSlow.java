/*
 * Decompiled with CFR 0.150.
 */
package markgg.modules.player;

import markgg.events.Event;
import markgg.events.listeners.EventUpdate;
import markgg.modules.Module;
import markgg.utilities.movement.MovementUtil;

public class NoSlow
extends Module {
    public NoSlow() {
        super("NoSlow", 0, Module.Category.PLAYER);
    }

    @Override
    public void onEvent(Event e) {
        if (e instanceof EventUpdate && this.mc.thePlayer.isBlocking()) {
            MovementUtil.setSpeed(0.12f);
        }
    }
}

