/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.movement;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;

public class NoStepDelay
extends Module {
    public NoStepDelay() {
        super("NoStepDelay", Category.Movement);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        NoStepDelay.mc.playerController.stepSoundTickCounter = 0.0f;
    }
}

