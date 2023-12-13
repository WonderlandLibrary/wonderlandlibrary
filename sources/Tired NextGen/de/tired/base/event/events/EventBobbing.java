package de.tired.base.event.events;

import de.tired.base.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@AllArgsConstructor
public class EventBobbing extends Event {
    float bobbing;
    boolean changeBobbing;
}
