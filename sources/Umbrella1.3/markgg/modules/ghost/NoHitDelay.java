/*
 * Decompiled with CFR 0.150.
 */
package markgg.modules.ghost;

import markgg.events.Event;
import markgg.events.listeners.EventUpdate;
import markgg.modules.Module;

public class NoHitDelay
extends Module {
    public NoHitDelay() {
        super("NoHitDelay", 0, Module.Category.GHOST);
    }

    @Override
    public void onEvent(Event e) {
        if (e instanceof EventUpdate && this.mc.theWorld != null && this.mc.thePlayer != null) {
            if (!this.mc.inGameHasFocus) {
                return;
            }
            this.mc.leftClickCounter = 0;
        }
    }
}

