package me.aquavit.liquidsense.event.events;

import me.aquavit.liquidsense.event.Event;

public class Render2DEvent extends Event {
    private float partialTicks;

    public Render2DEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }
}

