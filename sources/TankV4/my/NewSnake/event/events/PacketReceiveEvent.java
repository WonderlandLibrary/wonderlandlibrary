package my.NewSnake.event.events;

import my.NewSnake.event.Event;
import net.minecraft.network.Packet;

public class PacketReceiveEvent extends Event {
   private Event.State state;
   private Packet packet;

   public Packet getPacket() {
      return this.packet;
   }

   public PacketReceiveEvent(Packet var1) {
      this.packet = var1;
   }

   public void setPacket(Packet var1) {
      this.packet = var1;
   }
}
