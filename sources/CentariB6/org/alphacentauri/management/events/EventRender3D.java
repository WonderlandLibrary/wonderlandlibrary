package org.alphacentauri.management.events;

import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;

public class EventRender3D extends Event {
   private final float partialTicks;

   public EventRender3D(float partialTicks) {
      this.partialTicks = partialTicks;
   }

   public Event fire() {
      return (Event)(AC.isGhost()?this:super.fire());
   }

   public float getPartialTicks() {
      return this.partialTicks;
   }
}
