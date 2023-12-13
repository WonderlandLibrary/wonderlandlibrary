package vestige.event.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vestige.event.Event;

@Getter
@Setter
@AllArgsConstructor
public class JumpEvent extends Event {

    private double motionY;
    private float yaw;
    private boolean boosting;

}