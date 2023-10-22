package cn.hanabi.events;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.events.callables.EventCancellable;
import com.darkmagician6.eventapi.types.EventType;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class EventPacket extends EventCancellable {
   private final EventType eventType;
   public Packet packet;

   public EventPacket(EventType eventType, Packet packet) {
      this.eventType = eventType;
      this.packet = packet;
      if (this.packet instanceof S08PacketPlayerPosLook) {
         EventManager.call(new EventPullback());
      }

   }

   public EventType getEventType() {
      return this.eventType;
   }

   public Packet getPacket() {
      return this.packet;
   }

   public void setPacket(Packet packet) {
      this.packet = packet;
   }
}
