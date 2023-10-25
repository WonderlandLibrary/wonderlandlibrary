/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.event.events;

public final class RenderEvent {
    private final float partialTicks;

    public RenderEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public final float getPartialTicks() {
        return this.partialTicks;
    }
}

