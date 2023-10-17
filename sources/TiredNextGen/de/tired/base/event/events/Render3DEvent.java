package de.tired.base.event.events;

import de.tired.base.event.Event;

public class Render3DEvent extends Event {

    public float partialTicks;

    public Render3DEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

}
