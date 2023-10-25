package me.rhys.base.event.impl.network;

import me.rhys.base.event.Event;
import net.minecraft.network.Packet;

public class PacketEvent extends Event {
  private Packet<?> packet;
  
  public PacketEvent(Packet<?> packet) {
    this.packet = packet;
  }
  
  public Packet<?> getPacket() {
    return this.packet;
  }
  
  public void setPacket(Packet<?> packet) {
    this.packet = packet;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\event\impl\network\PacketEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */