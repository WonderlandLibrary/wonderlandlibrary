package today.flux.event;

import com.darkmagician6.eventapi.events.Event;


public class UIRenderEvent implements Event {
    private float particalTicks;

    public UIRenderEvent(float particleTicks) {
        this.particalTicks = particleTicks;
    }

    public float getParticalTicks() {
        return particalTicks;
    }
}
