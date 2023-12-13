package io.github.nevalackin.client.event.render.overlay;

import io.github.nevalackin.client.event.Event;
import net.minecraft.client.gui.ScaledResolution;

public final class DrawHUDEditorEvent implements Event {

    private final float partialTicks;
    private final ScaledResolution scaledResolution;

    public DrawHUDEditorEvent(float partialTicks, ScaledResolution scaledResolution) {
        this.partialTicks = partialTicks;
        this.scaledResolution = scaledResolution;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public ScaledResolution getScaledResolution() {
        return scaledResolution;
    }
}
