package com.sun.javafx.event;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class RedirectedEvent extends Event {
   private static final long serialVersionUID = 20121107L;
   public static final EventType REDIRECTED;
   private final Event originalEvent;

   public RedirectedEvent(Event var1) {
      this(var1, (Object)null, (EventTarget)null);
   }

   public RedirectedEvent(Event var1, Object var2, EventTarget var3) {
      super(var2, var3, REDIRECTED);
      this.originalEvent = var1;
   }

   public Event getOriginalEvent() {
      return this.originalEvent;
   }

   static {
      REDIRECTED = new EventType(Event.ANY, "REDIRECTED");
   }
}
