package club.dortware.client.event.impl;

import club.dortware.client.event.Event;

public class Render3DEvent extends Event {
    private final float ticks;

    public Render3DEvent(float ticks) {
        this.ticks = ticks;
    }

    public float getPartialTicks() {
        return ticks;
    }
}
