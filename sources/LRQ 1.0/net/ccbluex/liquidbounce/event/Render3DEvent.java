/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.event;

import net.ccbluex.liquidbounce.event.Event;

public final class Render3DEvent
extends Event {
    private final float partialTicks;

    public final float getPartialTicks() {
        return this.partialTicks;
    }

    public Render3DEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }
}

