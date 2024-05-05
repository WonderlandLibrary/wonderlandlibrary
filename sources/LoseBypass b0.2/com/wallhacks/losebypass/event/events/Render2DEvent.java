/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.event.events;

import com.wallhacks.losebypass.event.eventbus.Event;

public class Render2DEvent
extends Event {
    public float partialTicks;

    public Render2DEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }
}

