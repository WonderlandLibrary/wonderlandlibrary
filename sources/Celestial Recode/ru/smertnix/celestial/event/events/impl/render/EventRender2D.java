package ru.smertnix.celestial.event.events.impl.render;

import net.minecraft.client.gui.ScaledResolution;
import ru.smertnix.celestial.event.events.Event;

public class EventRender2D implements Event {

    private final ScaledResolution resolution;

    public EventRender2D(ScaledResolution resolution) {
        this.resolution = resolution;
    }

    public ScaledResolution getResolution() {
        return resolution;
    }

}
