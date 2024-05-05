package tech.atani.client.listener.event.minecraft.render;

import tech.atani.client.listener.event.Event;

public class Render3DEvent extends Event {
    private float partialTicks;

    public Render3DEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}
