package me.nyan.flush.event.impl;

import me.nyan.flush.event.Event;
import net.minecraft.client.gui.ScaledResolution;

public class Event2D extends Event {
    private final ScaledResolution scaledResolution;

    public Event2D(ScaledResolution scaledResolution) {
        this.scaledResolution = scaledResolution;
    }

    public int getWidth() {
        return scaledResolution.getScaledWidth();
    }

    public int getHeight() {
        return scaledResolution.getScaledHeight();
    }

    public int getScaleFactor() {
        return scaledResolution.getScaleFactor();
    }

    public ScaledResolution getScaledResolution() {
        return scaledResolution;
    }
}
