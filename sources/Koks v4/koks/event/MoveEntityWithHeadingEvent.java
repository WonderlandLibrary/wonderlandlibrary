package koks.event;

import koks.api.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor @Getter @Setter
public class MoveEntityWithHeadingEvent extends Event {
    float aiSpeed;
}
