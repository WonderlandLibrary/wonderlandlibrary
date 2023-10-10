package com.darkmagician6.eventapi.events.callables;

import com.darkmagician6.eventapi.events.Event;
import com.darkmagician6.eventapi.events.Typed;

public abstract class EventTyped implements Event, Typed {
   private final byte type;

   protected EventTyped(byte var1) {
      this.type = var1;
   }

   public byte getType() {
      return this.type;
   }
}
