package org.alphacentauri.management.events;

import org.alphacentauri.management.events.Event;

public class EventSuccessfulServerJoin extends Event {
   private String addr;
   private int port;

   public EventSuccessfulServerJoin(String addr, int port) {
      this.addr = addr;
      this.port = port;
   }

   public int getPort() {
      return this.port;
   }

   public String getAddr() {
      return this.addr;
   }
}
