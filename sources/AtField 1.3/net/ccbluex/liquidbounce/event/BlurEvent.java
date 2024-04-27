/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.event;

import net.ccbluex.liquidbounce.event.Event;

public final class BlurEvent
extends Event {
    private final boolean bloom;

    public BlurEvent(boolean bl) {
        this.bloom = bl;
    }

    public final boolean getBloom() {
        return this.bloom;
    }
}

