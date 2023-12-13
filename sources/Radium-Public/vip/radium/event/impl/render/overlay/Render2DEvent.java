package vip.radium.event.impl.render.overlay;

import vip.radium.event.Event;
import vip.radium.utils.render.LockedResolution;

public final class Render2DEvent implements Event {

    private final LockedResolution resolution;
    private final float partialTicks;

    public Render2DEvent(LockedResolution resolution, float partialTicks) {
        this.resolution = resolution;
        this.partialTicks = partialTicks;
    }

    public LockedResolution getResolution() {
        return resolution;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

}
