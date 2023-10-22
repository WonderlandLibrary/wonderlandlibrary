package cn.hanabi.events;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.entity.Entity;

public class EventSoundPlay implements Event {
   public Entity entity;
   public String name;
   public boolean cancel;

   public EventSoundPlay(Entity entity, String name) {
      this.entity = entity;
      this.name = name;
   }

   public Entity getEntity() {
      return this.entity;
   }

   public String getName() {
      return this.name;
   }

   public void setCancelled(boolean b) {
      this.cancel = b;
   }
}
