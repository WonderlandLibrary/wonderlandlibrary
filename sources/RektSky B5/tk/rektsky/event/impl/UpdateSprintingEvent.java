/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.event.impl;

import tk.rektsky.event.Event;

public class UpdateSprintingEvent
extends Event {
    private boolean sprinting;

    public UpdateSprintingEvent(boolean sprinting) {
        this.sprinting = sprinting;
    }

    public boolean isSprinting() {
        return this.sprinting;
    }

    public UpdateSprintingEvent setSprinting(boolean sprinting) {
        this.sprinting = sprinting;
        return this;
    }
}

