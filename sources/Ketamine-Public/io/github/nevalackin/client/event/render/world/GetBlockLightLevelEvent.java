package io.github.nevalackin.client.event.render.world;

import io.github.nevalackin.client.event.Event;

public final class GetBlockLightLevelEvent implements Event {

    private int lightLevel;

    public GetBlockLightLevelEvent(int lightLevel) {
        this.lightLevel = lightLevel;
    }

    public int getLightLevel() {
        return lightLevel;
    }

    public void setLightLevel(int lightLevel) {
        this.lightLevel = lightLevel;
    }
}
