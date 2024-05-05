package best.azura.client.impl.events;

import best.azura.scripting.event.NamedEvent;

public class EventRotate implements NamedEvent {
    public float yaw, pitch;
    public EventRotate(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public String name() {
        return "rotate";
    }
}