/*
 * Decompiled with CFR 0.150.
 */
package markgg.modules.movement;

import markgg.events.Event;
import markgg.events.listeners.EventUpdate;
import markgg.modules.Module;
import markgg.settings.BooleanSetting;

public class Sprint
extends Module {
    public BooleanSetting omni = new BooleanSetting("Omni", false);

    public Sprint() {
        super("Sprint", 0, Module.Category.MOVEMENT);
        this.addSettings(this.omni);
    }

    @Override
    public void onDisable() {
        this.mc.thePlayer.setSprinting(this.mc.gameSettings.keyBindSprint.getIsKeyPressed());
    }

    @Override
    public void onEvent(Event e) {
        if (e.isPre() && e instanceof EventUpdate) {
            if (this.omni.isEnabled()) {
                this.mc.thePlayer.setSprinting(true);
            } else if (!(!(this.mc.thePlayer.moveForward > 0.0f) || this.mc.thePlayer.isBlocking() || this.mc.thePlayer.isUsingItem() || this.mc.thePlayer.isOnLadder() || this.mc.thePlayer.isEating() || this.mc.thePlayer.isSneaking())) {
                this.mc.thePlayer.setSprinting(true);
            }
        }
    }
}

