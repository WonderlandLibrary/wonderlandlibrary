package me.rhys.client.module.combat.velocity.modes;

import me.rhys.base.event.Event;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.client.module.combat.velocity.Velocity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public class Hypixel extends ModuleMode<Velocity> {
  public Hypixel(String name, Velocity parent) {
    super(name, (Module)parent);
  }
  
  @EventTarget
  public void packetReceive(PacketEvent event) {
    if (event.getDirection().equals(Event.Direction.IN)) {
      Packet<?> packet = event.getPacket();
      if (packet instanceof S12PacketEntityVelocity) {
        S12PacketEntityVelocity velocity = (S12PacketEntityVelocity)packet;
        if (velocity.getEntityID() == this.mc.thePlayer.getEntityId()) {
          if (event.getPacket() instanceof net.minecraft.network.play.client.C0FPacketConfirmTransaction)
            event.setCancelled(true); 
          if (event.getPacket() instanceof net.minecraft.network.play.client.C0FPacketConfirmTransaction)
            event.setCancelled(true); 
          velocity.setMotionX((int)(velocity.getMotionX() * 0.0D));
          velocity.setMotionY((int)(velocity.getMotionY() * 0.0D));
          velocity.setMotionZ((int)(velocity.getMotionZ() * 0.0D));
        } 
      } 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\combat\velocity\modes\Hypixel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */