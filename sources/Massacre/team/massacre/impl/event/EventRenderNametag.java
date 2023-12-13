package team.massacre.impl.event;

import net.minecraft.entity.EntityLivingBase;
import team.massacre.api.event.Cancellable;
import team.massacre.api.event.Event;

public final class EventRenderNametag extends Cancellable implements Event {
   private final EntityLivingBase entity;
   private String renderedName;

   public EventRenderNametag(EntityLivingBase entity, String renderedName) {
      this.entity = entity;
      this.renderedName = renderedName;
   }

   public EntityLivingBase getEntity() {
      return this.entity;
   }

   public String getRenderedName() {
      return this.renderedName;
   }

   public void setRenderedName(String renderedName) {
      this.renderedName = renderedName;
   }
}
