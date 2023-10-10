package org.alphacentauri.management.events;

import org.alphacentauri.management.events.Event;

public class EventMiddleClick extends Event {
   private boolean cancelled;

   public void cancel() {
      this.cancelled = true;
   }

   public boolean isCancelled() {
      return this.cancelled;
   }
}
