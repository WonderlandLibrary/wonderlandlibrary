package best.azura.client.impl.events;

import best.azura.scripting.event.NamedEvent;

public class EventNameChange implements NamedEvent {
    public String lastName, newName;
    public EventNameChange(String lastName, String newName) {
        this.lastName = lastName;
        this.newName = newName;
    }

    @Override
    public String name() {
        return "nameChange";
    }
}