package club.async.event.impl;

import club.async.event.Event;

public class Event3D extends Event {

    private final float partialTicks;

    public Event3D(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public final float getPartialTicks() {
        return partialTicks;
    }

}
