package client.event.impl.other;

import client.event.CancellableEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public final class SlowDownEvent extends CancellableEvent {
    private float strafeMultiplier, forwardMultiplier;
}
