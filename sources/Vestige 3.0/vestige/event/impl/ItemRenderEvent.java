package vestige.event.impl;

import lombok.AllArgsConstructor;
import lombok.Setter;
import vestige.event.Event;

@Setter
@AllArgsConstructor
public class ItemRenderEvent extends Event {

    private boolean renderBlocking;

    public boolean shouldRenderBlocking() {
        return renderBlocking;
    }

}