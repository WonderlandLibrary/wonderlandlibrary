package com.sun.webkit.event;

public final class WCChangeEvent {
   private final Object source;

   public WCChangeEvent(Object var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("null source");
      } else {
         this.source = var1;
      }
   }

   public Object getSource() {
      return this.source;
   }

   public String toString() {
      return this.getClass().getName() + "[source=" + this.source + "]";
   }
}
