package events;

import events.Event;

public class MoveFlyingEvent
extends Event<MoveFlyingEvent> {
    public float yaw;

    public MoveFlyingEvent(float yaw) {
        this.yaw = yaw;
    }
}
