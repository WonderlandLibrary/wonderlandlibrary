package today.flux.event;

import com.darkmagician6.eventapi.events.Event;

public class PostUpdateEvent implements Event {
	public float yaw, pitch;

	public PostUpdateEvent(float yaw, float pitch) {
		this.yaw = yaw;
		this.pitch = pitch;
	}
}
