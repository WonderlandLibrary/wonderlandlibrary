/*
 * Decompiled with CFR 0.152.
 */
package net.dev.important.event;

import kotlin.Metadata;
import net.dev.important.event.Event;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2={"Lnet/dev/important/event/Render2DEvent;", "Lnet/dev/important/event/Event;", "partialTicks", "", "(F)V", "getPartialTicks", "()F", "LiquidBounce"})
public final class Render2DEvent
extends Event {
    private final float partialTicks;

    public Render2DEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public final float getPartialTicks() {
        return this.partialTicks;
    }
}

