package io.github.nevalackin.client.event.render.game;

import io.github.nevalackin.client.event.Event;
import net.minecraft.client.renderer.culling.Frustum;

public final class FrustumUpdateEvent implements Event {

    private final Frustum frustum;

    public FrustumUpdateEvent(Frustum frustum) {
        this.frustum = frustum;
    }

    public Frustum getFrustum() {
        return frustum;
    }
}
