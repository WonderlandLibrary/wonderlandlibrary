package xyz.northclient.features.events;

import xyz.northclient.features.Event;
import lombok.Getter;

public class Render2DEvent extends Event {
    @Getter
    public float partialTicks;

    public Render2DEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }
}
