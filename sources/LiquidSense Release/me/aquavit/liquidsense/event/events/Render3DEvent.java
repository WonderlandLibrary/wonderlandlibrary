package me.aquavit.liquidsense.event.events;

import me.aquavit.liquidsense.event.Event;

public class Render3DEvent extends Event {
    private float partialTicks;

    public Render3DEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }
}
