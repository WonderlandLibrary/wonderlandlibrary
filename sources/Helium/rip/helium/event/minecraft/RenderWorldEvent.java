package rip.helium.event.minecraft;

import rip.helium.event.CancellableEvent;

public class RenderWorldEvent extends CancellableEvent {

    private float partialTicks;

    public RenderWorldEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public void setPartialTicks(float partialTicks) {
        this.partialTicks = partialTicks;
    }
}
