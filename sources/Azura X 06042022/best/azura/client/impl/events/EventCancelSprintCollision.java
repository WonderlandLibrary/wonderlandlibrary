package best.azura.client.impl.events;

import best.azura.scripting.event.NamedEvent;

public class EventCancelSprintCollision implements NamedEvent {
    public boolean cancelSprint = true;

    @Override
    public String name() {
        return "cancelSprintCollision";
    }
}
