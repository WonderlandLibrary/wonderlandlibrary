/*
 * Decompiled with CFR 0.152.
 */
package net.dev.important.event;

import kotlin.Metadata;
import net.dev.important.event.CancellableEvent;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\u0004\u00a8\u0006\b"}, d2={"Lnet/dev/important/event/JumpEvent;", "Lnet/dev/important/event/CancellableEvent;", "motion", "", "(F)V", "getMotion", "()F", "setMotion", "LiquidBounce"})
public final class JumpEvent
extends CancellableEvent {
    private float motion;

    public JumpEvent(float motion) {
        this.motion = motion;
    }

    public final float getMotion() {
        return this.motion;
    }

    public final void setMotion(float f) {
        this.motion = f;
    }
}

