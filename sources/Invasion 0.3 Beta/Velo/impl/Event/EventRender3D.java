package Velo.impl.Event;

import Velo.api.Event.Event;
import net.minecraft.client.gui.ScaledResolution;

public class EventRender3D extends Event<EventRender3D> {
	public static EventRender3D ins;
	
	public EventRender3D(float ticks) {
		this.partialTicks = ticks;
	}
	
	float partialTicks;
	
	public float getTicks() {
		return this.partialTicks;
	}

	public float getPartialTicks() {
	    return this.partialTicks;
	}
	
}

