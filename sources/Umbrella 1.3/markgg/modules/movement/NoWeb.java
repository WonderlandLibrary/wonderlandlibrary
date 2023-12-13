/*
 * Decompiled with CFR 0.150.
 */
package markgg.modules.movement;

import markgg.events.Event;
import markgg.events.listeners.EventUpdate;
import markgg.modules.Module;

public class NoWeb
extends Module {
    public NoWeb() {
        super("NoWeb", 0, Module.Category.MOVEMENT);
    }

    @Override
    public void onEvent(Event e) {
        if (e instanceof EventUpdate && this.isEnabled()) {
            this.mc.thePlayer.isInWeb = false;
        }
    }
}

