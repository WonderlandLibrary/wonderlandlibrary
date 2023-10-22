package epsilon.events.listeners.render;

import epsilon.events.Event;

public class EventRender3d extends Event<EventRender3d>{

	
	public float partialTicks;
    
    public EventRender3d(float partialTicks){
        this.partialTicks = partialTicks;
    }
    
    public float getPartialTicks(){
        return partialTicks;
    }
	
}
