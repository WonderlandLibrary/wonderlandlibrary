package summer.cheat.eventsystem.events.render;

import net.minecraft.client.gui.ScaledResolution;
import summer.cheat.eventsystem.Event;

public class EventRender3D extends Event {
	
	
	public final ScaledResolution scaledResolution;
	
	public EventRender3D(ScaledResolution scaledResolution, float ticks) {
		this.partialTicks = ticks;
		this.scaledResolution = scaledResolution;
	}
	
	float partialTicks;
	
	public float getTicks() {
		return this.partialTicks;
	}

	public float getPartialTicks() {
	    return this.partialTicks;
	  }

}
