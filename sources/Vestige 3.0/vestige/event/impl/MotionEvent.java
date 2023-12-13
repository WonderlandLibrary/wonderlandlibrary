package vestige.event.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vestige.event.Event;

@Getter
@Setter
@AllArgsConstructor
public class MotionEvent extends Event {

    private double x, y, z;
    private float yaw, pitch;
    private boolean onGround;

}