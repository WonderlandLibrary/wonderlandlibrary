package client.event.impl.input;

import client.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MoveInputEvent implements Event {
    private float forward, strafe;
    private boolean jumping, sneaking;
    private double strafeSneakMultiplier, forwardSneakMultiplier;
}
