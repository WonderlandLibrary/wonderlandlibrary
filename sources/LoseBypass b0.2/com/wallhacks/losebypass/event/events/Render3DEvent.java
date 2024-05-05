/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.event.events;

import com.wallhacks.losebypass.event.eventbus.Event;

public class Render3DEvent
extends Event {
    public float partialTicks;

    public Render3DEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }
}

