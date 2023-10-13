/*
 * Decompiled with CFR 0.150.
 */
package markgg.modules.player;

import markgg.events.Event;
import markgg.events.listeners.EventUpdate;
import markgg.modules.Module;

public class AutoRespawn
extends Module {
    public AutoRespawn() {
        super("AutoRespawn", 0, Module.Category.PLAYER);
    }

    @Override
    public void onEvent(Event e) {
        if (e instanceof EventUpdate && this.mc.thePlayer.isDead) {
            this.mc.thePlayer.respawnPlayer();
        }
    }
}

