package vestige.api.event.types;

import lombok.Getter;
import lombok.Setter;
import vestige.api.event.Event;

@Getter
@Setter
public class CancellableEvent extends Event {

    private boolean cancelled;

    public CancellableEvent() {

    }

}
