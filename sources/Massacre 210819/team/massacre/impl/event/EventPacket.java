package team.massacre.impl.event;

import net.minecraft.network.Packet;
import team.massacre.api.event.Cancellable;
import team.massacre.api.event.Event;

public class EventPacket extends Cancellable implements Event {
   public Packet packet;

   public EventPacket(Packet packet) {
      this.packet = packet;
   }

   public <T extends Packet> T getPacket() {
      return this.packet;
   }

   public void setPacket(Packet packet) {
      this.packet = packet;
   }

   public void cancelEvent() {
      this.setCancelled(true);
   }
}
