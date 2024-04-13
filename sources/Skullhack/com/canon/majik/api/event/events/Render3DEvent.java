package com.canon.majik.api.event.events;

import com.canon.majik.api.event.eventBus.Event;

public class Render3DEvent extends Event {

    public float particleTicks;

    public Render3DEvent(float partial){
        this.particleTicks = particleTicks;
    }
}
