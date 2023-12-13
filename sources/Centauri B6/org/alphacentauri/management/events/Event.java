package org.alphacentauri.management.events;

import org.alphacentauri.AC;

public class Event {
   public Event fire() {
      AC.getEventManager().fireEvent(this);
      return this;
   }
}
