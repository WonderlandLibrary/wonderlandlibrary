package appu26j.events.entity;

import appu26j.events.Event;
import net.minecraft.entity.Entity;

public class EventAttack2 extends Event
{
	private final Entity entity;
	
	public EventAttack2(Entity entity)
	{
	    this.entity = entity;
	}
	
	public Entity getTarget()
	{
	    return this.entity;
	}
}
