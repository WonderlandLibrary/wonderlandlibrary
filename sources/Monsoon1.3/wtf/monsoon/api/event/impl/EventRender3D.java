package wtf.monsoon.api.event.impl;

import wtf.monsoon.api.event.Event;

public class EventRender3D extends Event {
	
	private float partialTicks;
	    
    public EventRender3D(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public void setPartialTicks(float partialTicks) {
        this.partialTicks = partialTicks;
    }

}
