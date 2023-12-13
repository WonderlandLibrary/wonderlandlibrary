package vestige.api.event.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import vestige.api.event.Event;

@Data
@AllArgsConstructor
public class PreStepEvent extends Event {
	
	private float height;
	
}