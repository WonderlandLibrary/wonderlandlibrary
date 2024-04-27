/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.event;

import net.ccbluex.liquidbounce.event.Event;

public final class SlowDownEvent
extends Event {
    private float strafe;
    private float forward;

    public final float getStrafe() {
        return this.strafe;
    }

    public final void setForward(float f) {
        this.forward = f;
    }

    public final void setStrafe(float f) {
        this.strafe = f;
    }

    public final float getForward() {
        return this.forward;
    }

    public SlowDownEvent(float f, float f2) {
        this.strafe = f;
        this.forward = f2;
    }
}

