package dev.tenacity.event.impl.render;

import dev.tenacity.event.Event;

public class ShaderEvent extends Event {

    private final boolean bloom;

    public ShaderEvent(boolean bloom) {
        this.bloom = bloom;
    }

    public boolean isBloom() {
        return bloom;
    }

    public boolean isBlur() {
        return !bloom;
    }
}

