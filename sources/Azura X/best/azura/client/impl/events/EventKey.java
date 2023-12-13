package best.azura.client.impl.events;

import best.azura.scripting.event.NamedEvent;

public class EventKey implements NamedEvent {
    public int key;
    public EventKey(int key) {
        this.key = key;
    }

    @Override
    public String name() {
        return "key";
    }
}