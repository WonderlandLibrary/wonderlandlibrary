package io.github.nevalackin.client.event.render.game;

import io.github.nevalackin.client.event.Event;
import net.minecraft.client.gui.ScaledResolution;

public final class DrawScreenEvent implements Event {

    private final ScaledResolution scaledResolution;

    public DrawScreenEvent(ScaledResolution scaledResolution) {
        this.scaledResolution = scaledResolution;
    }

    public ScaledResolution getScaledResolution() {
        return scaledResolution;
    }
}
