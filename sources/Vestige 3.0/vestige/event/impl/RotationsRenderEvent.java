package vestige.event.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vestige.event.Event;

@Getter
@AllArgsConstructor
public class RotationsRenderEvent extends Event {

	@Setter
	private float yaw, bodyYaw, pitch;

	private float partialTicks;
	
}
