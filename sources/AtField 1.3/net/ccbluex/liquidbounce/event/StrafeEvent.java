/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.event;

import net.ccbluex.liquidbounce.event.CancellableEvent;

public final class StrafeEvent
extends CancellableEvent {
    private final float friction;
    private final float strafe;
    private final float forward;

    public final float getForward() {
        return this.forward;
    }

    public final float getStrafe() {
        return this.strafe;
    }

    public StrafeEvent(float f, float f2, float f3) {
        this.strafe = f;
        this.forward = f2;
        this.friction = f3;
    }

    public final float getFriction() {
        return this.friction;
    }
}

