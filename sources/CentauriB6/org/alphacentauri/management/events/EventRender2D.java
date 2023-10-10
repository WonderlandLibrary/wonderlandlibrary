package org.alphacentauri.management.events;

import net.minecraft.client.gui.ScaledResolution;
import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;

public class EventRender2D extends Event {
   private ScaledResolution resolution;

   public EventRender2D(ScaledResolution resolution) {
      this.resolution = resolution;
   }

   public Event fire() {
      return (Event)(AC.isGhost()?this:super.fire());
   }

   public ScaledResolution getResolution() {
      return this.resolution;
   }
}
