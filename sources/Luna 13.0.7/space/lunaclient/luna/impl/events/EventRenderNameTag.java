package space.lunaclient.luna.impl.events;

import net.minecraft.entity.Entity;
import space.lunaclient.luna.api.event.Event;

public class EventRenderNameTag
  extends Event
{
  public static boolean cancel;
  private Entity entity;
  
  public EventRenderNameTag(Entity entity)
  {
    this.entity = entity;
  }
  
  public Entity getEntity()
  {
    return this.entity;
  }
}
