/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.event;

import net.ccbluex.liquidbounce.event.Event;

public final class BlurEvent
extends Event {
    private final boolean bloom;

    public final boolean getBloom() {
        return this.bloom;
    }

    public BlurEvent(boolean bloom) {
        this.bloom = bloom;
    }
}

