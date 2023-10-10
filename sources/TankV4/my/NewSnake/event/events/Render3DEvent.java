package my.NewSnake.event.events;

import my.NewSnake.event.Event;
import net.minecraft.client.gui.ScaledResolution;

public class Render3DEvent extends Event {
   public final ScaledResolution scaledResolution;
   float partialTicks;

   public Render3DEvent(ScaledResolution var1, float var2) {
      this.partialTicks = var2;
      this.scaledResolution = var1;
   }

   public float getPartialTicks() {
      return this.partialTicks;
   }

   public float getTicks() {
      return this.partialTicks;
   }
}
