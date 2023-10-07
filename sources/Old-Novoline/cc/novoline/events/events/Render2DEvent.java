package cc.novoline.events.events;

import net.minecraft.client.gui.ScaledResolution;

public class Render2DEvent implements Event {

    private final ScaledResolution resolution;
    private final float partialTicks;

    public Render2DEvent(ScaledResolution resolution, float partialTicks) {
        this.resolution = resolution;
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }

    public ScaledResolution getResolution() {
        return this.resolution;
    }

}
