package vestige.event.type;

import lombok.Getter;
import lombok.Setter;
import vestige.event.Event;

@Getter
@Setter
public class CancellableEvent extends Event {

    private boolean cancelled;

}