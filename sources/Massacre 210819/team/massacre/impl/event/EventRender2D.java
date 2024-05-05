package team.massacre.impl.event;

import net.minecraft.client.gui.ScaledResolution;
import team.massacre.api.event.Cancellable;
import team.massacre.api.event.Event;

public class EventRender2D extends Cancellable implements Event {
   public ScaledResolution scaledResolution;
   public float partialTicks;

   public EventRender2D(ScaledResolution scaledResolution, float partialTicks) {
      this.scaledResolution = scaledResolution;
      this.partialTicks = partialTicks;
   }

   public ScaledResolution getScaledResolution() {
      return this.scaledResolution;
   }

   public float getPartialTicks() {
      return this.partialTicks;
   }
}
