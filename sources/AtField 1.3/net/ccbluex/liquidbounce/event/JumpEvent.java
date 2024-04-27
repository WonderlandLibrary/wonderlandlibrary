/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.event;

import net.ccbluex.liquidbounce.event.CancellableEvent;

public final class JumpEvent
extends CancellableEvent {
    private float motion;

    public final float getMotion() {
        return this.motion;
    }

    public JumpEvent(float f) {
        this.motion = f;
    }

    public final void setMotion(float f) {
        this.motion = f;
    }
}

