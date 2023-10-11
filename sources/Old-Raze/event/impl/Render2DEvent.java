package markgg.event.impl;

import markgg.event.Event;
import net.minecraft.client.gui.ScaledResolution;

public class Render2DEvent extends Event {

    public ScaledResolution scaledResolution;

    public Render2DEvent(ScaledResolution scaledResolution) {
        this.scaledResolution = scaledResolution;
    }

    public ScaledResolution getScaledResolution() {
        return scaledResolution;
    }
}
