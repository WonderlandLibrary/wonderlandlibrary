package vestige.api.event.impl;

import lombok.Getter;
import vestige.api.event.Event;

@Getter
public class RenderEvent extends Event {

    private float partialTicks;

    public RenderEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

}
