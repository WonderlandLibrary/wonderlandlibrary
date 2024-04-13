package com.canon.majik.api.event.events;

import com.canon.majik.api.event.eventBus.Event;

public class Render2DEvent extends Event {
    public float particleTicks;

    public Render2DEvent(float partial){
        this.particleTicks = partial;
    }
}
