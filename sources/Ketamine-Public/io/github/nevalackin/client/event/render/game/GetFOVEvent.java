package io.github.nevalackin.client.event.render.game;

import io.github.nevalackin.client.event.Event;

public final class GetFOVEvent implements Event {

    private float fov;
    private boolean useModifier = true;

    public GetFOVEvent(float fov) {
        this.fov = fov;
    }

    public float getFov() {
        return fov;
    }

    public void setFov(float fov) {
        this.fov = fov;
    }

    public boolean isUseModifier() {
        return useModifier;
    }

    public void setUseModifier(boolean useModifier) {
        this.useModifier = useModifier;
    }
}
