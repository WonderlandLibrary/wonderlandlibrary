package club.async.event.impl;

import club.async.event.Event;
import net.minecraft.client.gui.ScaledResolution;

public class Event2D extends Event {

    private ScaledResolution scaledResolution;
    private final float partialTicks;

    public Event2D(ScaledResolution scaledResolution, float partialTicks) {
        this.scaledResolution = scaledResolution;
        this.partialTicks = partialTicks;
    }

    public final ScaledResolution getScaledResolution() {
        return scaledResolution;
    }

    public final void setScaledResolution(ScaledResolution scaledResolution) {
        this.scaledResolution = scaledResolution;
    }

    public final float getPartialTicks() {
        return partialTicks;
    }

}
