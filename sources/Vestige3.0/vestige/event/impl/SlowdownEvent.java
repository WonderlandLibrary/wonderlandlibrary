package vestige.event.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vestige.event.Event;

@Getter
@Setter
@AllArgsConstructor
public class SlowdownEvent extends Event {

    private float forward, strafe;
    private boolean allowedSprinting;

}