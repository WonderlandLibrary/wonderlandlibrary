package me.dev.legacy.event.events;

import me.dev.legacy.event.EventStage;

public class Render3DEvent extends EventStage
{
    private float partialTicks;

    public Render3DEvent(final float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }
}

