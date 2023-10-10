package today.flux.event;

import com.darkmagician6.eventapi.events.Event;


public class WorldRenderEvent implements Event {
	private float partialTicks;
	public WorldRenderEvent(float partialTicks) {
		this.partialTicks = partialTicks;
	}

	public float getPartialTicks() {
		return partialTicks;
	}
}
