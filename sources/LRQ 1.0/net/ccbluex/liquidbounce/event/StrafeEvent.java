/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.event;

import net.ccbluex.liquidbounce.event.CancellableEvent;

public final class StrafeEvent
extends CancellableEvent {
    private final float strafe;
    private final float forward;
    private final float friction;

    public final float getStrafe() {
        return this.strafe;
    }

    public final float getForward() {
        return this.forward;
    }

    public final float getFriction() {
        return this.friction;
    }

    public StrafeEvent(float strafe, float forward, float friction) {
        this.strafe = strafe;
        this.forward = forward;
        this.friction = friction;
    }
}

