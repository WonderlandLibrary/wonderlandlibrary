package org.alphacentauri.management.events;

import org.alphacentauri.management.events.Event;

public class EventRenderString extends Event {
   private String text;

   public EventRenderString(String text) {
      this.text = text;
   }

   public void setText(String text) {
      this.text = text;
   }

   public String getText() {
      return this.text;
   }
}
