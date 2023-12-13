package io.github.nevalackin.client.event.render.world;

import io.github.nevalackin.client.event.Event;
import net.minecraft.client.gui.ScaledResolution;

public final class Render3DEvent implements Event {

    private final float partialTicks;
    private final ScaledResolution scaledResolution;

    public Render3DEvent(float partialTicks, ScaledResolution scaledResolution) {
        this.partialTicks = partialTicks;
        this.scaledResolution = scaledResolution;
    }

    public ScaledResolution getScaledResolution() {
        return scaledResolution;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}
