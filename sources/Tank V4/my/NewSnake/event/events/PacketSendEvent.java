package my.NewSnake.event.events;

import my.NewSnake.event.Event;
import net.minecraft.network.Packet;

public class PacketSendEvent extends Event {
   private Packet packet;
   private Event.State state;

   public Event.State getState() {
      return this.state;
   }

   public Packet getPacket() {
      return this.packet;
   }

   public PacketSendEvent(Event.State var1, Packet var2) {
      this.state = var1;
      this.packet = var2;
   }

   public void setState(Event.State var1) {
      this.state = var1;
   }

   public void setPacket(Packet var1) {
      this.packet = var1;
   }
}
