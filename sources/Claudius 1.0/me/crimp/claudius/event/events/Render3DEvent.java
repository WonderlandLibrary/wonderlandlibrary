package me.crimp.claudius.event.events;

import me.crimp.claudius.event.EventStage;

public class Render3DEvent extends EventStage {
    private final float partialTicks;

    public Render3DEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }
}

