package xyz.cucumber.base.events.ext;

import net.minecraft.entity.Entity;
import xyz.cucumber.base.events.Event;

public class EventEntityRenderer extends Event{

	public Entity entity;

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public EventEntityRenderer(Entity entity) {
		this.entity = entity;
	}
	
}
