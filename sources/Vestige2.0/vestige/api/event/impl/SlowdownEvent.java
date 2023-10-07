package vestige.api.event.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import vestige.api.event.types.CancellableEvent;

@Data
@AllArgsConstructor
public class SlowdownEvent extends CancellableEvent {
	
	private float forward, strafe;

}
