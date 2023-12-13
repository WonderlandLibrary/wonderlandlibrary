package today.flux.event;

import com.darkmagician6.eventapi.events.Event;
import com.darkmagician6.eventapi.types.EventType;
import lombok.Getter;

public class PlayerAbilitiesEvent implements Event {
    @Getter
    EventType type;

    public PlayerAbilitiesEvent(EventType type) {
        this.type = type;
    }
}
