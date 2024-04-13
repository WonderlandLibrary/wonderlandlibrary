package dev.tenacity.event.impl.render;

import dev.tenacity.event.Event;
import net.minecraft.client.gui.ScaledResolution;

public final class Render2DEvent extends Event {

    private final ScaledResolution scaledResolution;

    public Render2DEvent(final ScaledResolution scaledResolution) {
        this.scaledResolution = scaledResolution;
    }

    public ScaledResolution getScaledResolution() {
        return scaledResolution;
    }

}
