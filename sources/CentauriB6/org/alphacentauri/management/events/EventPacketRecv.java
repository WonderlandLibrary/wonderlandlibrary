package org.alphacentauri.management.events;

import net.minecraft.network.Packet;
import org.alphacentauri.management.events.Event;

public class EventPacketRecv extends Event {
   private final Packet packet;
   private boolean cancel = false;

   public EventPacketRecv(Packet packet) {
      this.packet = packet;
   }

   public void cancel() {
      this.cancel = true;
   }

   public boolean isCanceled() {
      return this.cancel;
   }

   public Packet getPacket() {
      return this.packet;
   }
}
