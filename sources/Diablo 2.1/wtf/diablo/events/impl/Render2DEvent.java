package wtf.diablo.events.impl;

import net.minecraft.client.gui.ScaledResolution;
import wtf.diablo.events.Event;

public final class Render2DEvent extends Event {
    private float partialTicks;
    private ScaledResolution scaledResolution;

    public float getPartialTicks() {
        return this.partialTicks;
    }

    public ScaledResolution getScaledResolution() {
        return this.scaledResolution;
    }

    public void setPartialTicks(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public void setScaledResolution(ScaledResolution scaledResolution) {
        this.scaledResolution = scaledResolution;
    }

    public Render2DEvent(float partialTicks, ScaledResolution scaledResolution) {
        this.partialTicks = partialTicks;
        this.scaledResolution = scaledResolution;
    }
}