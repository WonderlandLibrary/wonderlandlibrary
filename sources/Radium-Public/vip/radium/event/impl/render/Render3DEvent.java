package vip.radium.event.impl.render;

import vip.radium.event.Event;
import net.minecraft.client.gui.ScaledResolution;

public final class Render3DEvent implements Event {

    private final ScaledResolution scaledResolution;
    private final float partialTicks;
    public Render3DEvent(ScaledResolution scaledResolution, float partialTicks) {
        this.scaledResolution = scaledResolution;
        this.partialTicks = partialTicks;
    }

    public ScaledResolution getScaledResolution() {
        return scaledResolution;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

}
