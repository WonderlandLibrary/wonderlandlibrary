package client.event.impl.other;

import client.event.CancellableEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public final class MotionEvent extends CancellableEvent {
    private double x, y, z;
    private float yaw, pitch;
    private boolean onGround;
}
