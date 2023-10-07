package vestige.api.event;

import vestige.Vestige;

public class Event {

    public Event() {

    }

    public void call() {
        Vestige.getInstance().getEventManager().handle(this);
    }

}
