/*
 * Decompiled with CFR 0.150.
 */
package markgg.modules.player;

import markgg.events.Event;
import markgg.events.listeners.EventMotion;
import markgg.modules.Module;
import markgg.settings.NumberSetting;

public class FastBreak
extends Module {
    private final NumberSetting speed = new NumberSetting("Speed", 0.5, 0.0, 0.9, 0.1);

    public FastBreak() {
        super("FastBreak", 0, Module.Category.PLAYER);
    }

    @Override
    public void onEvent(Event e) {
        if (e instanceof EventMotion) {
            this.mc.playerController.blockHitDelay = 0;
            if ((double)this.mc.playerController.curBlockDamageMP > 1.0 - this.speed.getValue()) {
                this.mc.playerController.curBlockDamageMP = 1.0f;
            }
        }
    }
}

