package vestige.api.event.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vestige.api.event.types.CancellableEvent;

@Getter
@Setter
@AllArgsConstructor
public class MoveEvent extends CancellableEvent {
	
	private double x, y, z;
	
}