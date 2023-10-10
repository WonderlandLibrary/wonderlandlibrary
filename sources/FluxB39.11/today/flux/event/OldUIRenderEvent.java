package today.flux.event;

import com.darkmagician6.eventapi.events.Event;


public class OldUIRenderEvent implements Event {
    private float particalTicks;

    public OldUIRenderEvent(float particleTicks) {
        this.particalTicks = particleTicks;
    }

    public float getParticalTicks() {
        return particalTicks;
    }
}
