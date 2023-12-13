package today.flux.event;

import com.darkmagician6.eventapi.events.Event;


public class UIRenderPostEvent implements Event {
    private float particalTicks;

    public UIRenderPostEvent(float particleTicks) {
        this.particalTicks = particleTicks;
    }

    public float getParticalTicks() {
        return particalTicks;
    }
}
