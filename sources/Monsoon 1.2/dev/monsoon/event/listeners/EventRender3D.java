package dev.monsoon.event.listeners;

import dev.monsoon.event.Event;

public class EventRender3D extends Event<EventRender3D>{
    
    private float partialTicks;
    
    public EventRender3D(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public void setPartialTicks(float partialTicks) {
        this.partialTicks = partialTicks;
    }
}