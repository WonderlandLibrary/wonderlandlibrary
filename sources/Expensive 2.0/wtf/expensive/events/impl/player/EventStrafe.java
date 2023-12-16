package wtf.expensive.events.impl.player;

import wtf.expensive.events.Event;

public class EventStrafe extends Event {

    public float yaw;

    public EventStrafe(float yaw) {
        this.yaw = yaw;
    }

}
