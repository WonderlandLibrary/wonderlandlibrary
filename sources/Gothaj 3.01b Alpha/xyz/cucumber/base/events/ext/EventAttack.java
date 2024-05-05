package xyz.cucumber.base.events.ext;

import net.minecraft.entity.Entity;
import xyz.cucumber.base.events.Event;

public class EventAttack extends Event{
	
	private Entity entity;

	public EventAttack(Entity entity) {
		super();
		this.entity = entity;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	
	

}
