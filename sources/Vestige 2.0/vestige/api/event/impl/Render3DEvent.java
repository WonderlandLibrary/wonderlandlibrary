package vestige.api.event.impl;

import lombok.Getter;
import vestige.api.event.Event;

@Getter
public class Render3DEvent extends Event {

    private float partialTicks;

    public Render3DEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

}
