package org.dreamcore.client.event.events.impl.render;

import org.dreamcore.client.event.events.Event;

public class EventRender3D implements Event {
    private final float partialTicks;

    public EventRender3D(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }
}
