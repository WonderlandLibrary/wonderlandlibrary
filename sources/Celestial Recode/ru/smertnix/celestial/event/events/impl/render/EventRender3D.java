package ru.smertnix.celestial.event.events.impl.render;

import ru.smertnix.celestial.event.events.Event;

public class EventRender3D implements Event {
    private final float partialTicks;

    public EventRender3D(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }
}
