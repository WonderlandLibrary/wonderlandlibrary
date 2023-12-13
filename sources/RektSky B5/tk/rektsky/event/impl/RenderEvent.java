/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.event.impl;

import tk.rektsky.event.Event;

public class RenderEvent
extends Event {
    private float partialTick;

    public float getPartialTick() {
        return this.partialTick;
    }

    public void setPartialTick(float partialTick) {
        this.partialTick = partialTick;
    }

    public RenderEvent(float partialTick) {
        this.partialTick = partialTick;
    }
}

