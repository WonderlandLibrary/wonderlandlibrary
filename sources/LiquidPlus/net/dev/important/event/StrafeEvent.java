/*
 * Decompiled with CFR 0.152.
 */
package net.dev.important.event;

import kotlin.Metadata;
import net.dev.important.event.CancellableEvent;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\b\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0006R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\b\u00a8\u0006\u000b"}, d2={"Lnet/dev/important/event/StrafeEvent;", "Lnet/dev/important/event/CancellableEvent;", "strafe", "", "forward", "friction", "(FFF)V", "getForward", "()F", "getFriction", "getStrafe", "LiquidBounce"})
public final class StrafeEvent
extends CancellableEvent {
    private final float strafe;
    private final float forward;
    private final float friction;

    public StrafeEvent(float strafe, float forward, float friction) {
        this.strafe = strafe;
        this.forward = forward;
        this.friction = friction;
    }

    public final float getStrafe() {
        return this.strafe;
    }

    public final float getForward() {
        return this.forward;
    }

    public final float getFriction() {
        return this.friction;
    }
}

