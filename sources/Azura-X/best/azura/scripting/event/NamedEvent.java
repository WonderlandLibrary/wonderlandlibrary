package best.azura.scripting.event;

import best.azura.eventbus.core.Event;

public interface NamedEvent extends Event {
    String name();
}
