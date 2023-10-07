package vestige.api.event.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vestige.api.event.Event;

@Getter
@Setter
@AllArgsConstructor
public class JumpEvent extends Event {
	
	private double jumpHeight;
	private float yaw;
	
}