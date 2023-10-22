package cn.hanabi.events;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.entity.Entity;

public class EventAttack implements Event {
   public Entity entity;

   public EventAttack(Entity entity) {
      this.entity = entity;
   }

   public Entity getEntity() {
      return this.entity;
   }
}
