package org.alphacentauri.management.events;

import org.alphacentauri.management.events.Event;

public class EventServerJoin extends Event {
   private final String addr;
   private final int port;
   private boolean cancel;

   public EventServerJoin(String addr, int port) {
      this.addr = addr;
      this.port = port;
   }

   public int getPort() {
      return this.port;
   }

   public void cancel() {
      this.cancel = true;
   }

   public boolean isCanceled() {
      return this.cancel;
   }

   public String getAddr() {
      return this.addr;
   }
}
