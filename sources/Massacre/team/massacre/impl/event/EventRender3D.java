package team.massacre.impl.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import team.massacre.api.event.Cancellable;
import team.massacre.api.event.Event;

public class EventRender3D extends Cancellable implements Event {
   public float partialTicks;
   public ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
   public Entity entity;

   public EventRender3D(float partialTicks, Entity entity) {
      this.partialTicks = partialTicks;
      this.entity = entity;
   }

   public Entity getEntity() {
      return this.entity;
   }

   public float getPartialTicks() {
      return this.partialTicks;
   }

   public void setPartialTicks(float partialTicks) {
      this.partialTicks = partialTicks;
   }

   public ScaledResolution getScaledResolution() {
      return this.sr;
   }
}
