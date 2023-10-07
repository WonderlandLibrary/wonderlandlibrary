package best.azura.client.impl.events;

import best.azura.scripting.event.NamedEvent;

public class EventFastTick implements NamedEvent {
    @Override
    public String name() {
        return "fastTick";
    }
}