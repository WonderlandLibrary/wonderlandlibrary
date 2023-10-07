package best.azura.client.impl.events;

import best.azura.scripting.event.NamedEvent;

public class EventChangeSprintState implements NamedEvent {

    public boolean omni = false;

    @Override
    public String name() {
        return "changeSprintState";
    }
}