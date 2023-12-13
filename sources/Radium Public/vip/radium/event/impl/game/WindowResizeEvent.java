package vip.radium.event.impl.game;

import vip.radium.event.Event;
import net.minecraft.client.gui.ScaledResolution;

public final class WindowResizeEvent implements Event {

    private final ScaledResolution scaledResolution;

    public WindowResizeEvent(ScaledResolution scaledResolution) {
        this.scaledResolution = scaledResolution;
    }

    public ScaledResolution getScaledResolution() {
        return scaledResolution;
    }

}
