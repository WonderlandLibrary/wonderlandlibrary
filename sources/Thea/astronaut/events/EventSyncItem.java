package astronaut.events;

import eventapi.events.Event;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class EventSyncItem implements Event {
    private int slot;
}
