/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.event;

import net.ccbluex.liquidbounce.event.Event;

public final class Render2DEvent
extends Event {
    private final float partialTicks;

    public final float getPartialTicks() {
        return this.partialTicks;
    }

    public Render2DEvent(float f) {
        this.partialTicks = f;
    }
}

