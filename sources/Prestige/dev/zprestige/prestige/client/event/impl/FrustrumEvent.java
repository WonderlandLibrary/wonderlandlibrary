package dev.zprestige.prestige.client.event.impl;

import dev.zprestige.prestige.client.event.Event;
import net.minecraft.client.render.Frustum;

public class FrustrumEvent extends Event {
    public Frustum frustrum;

    public FrustrumEvent(Frustum frustum) {
        this.frustrum = frustum;
    }

    public Frustum getFrustrum() {
        return this.frustrum;
    }
}
