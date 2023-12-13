package io.github.nevalackin.client.event.render.item;

import io.github.nevalackin.client.event.Event;

public final class GetArmSwingModEvent implements Event {

    private float modifier;

    public GetArmSwingModEvent(float modifier) {
        this.modifier = modifier;
    }

    public float getModifier() {
        return modifier;
    }

    public void setModifier(float modifier) {
        this.modifier = modifier;
    }
}
