package me.rhys.client.module.combat.velocity.modes;

import me.rhys.base.event.Event;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.client.module.combat.velocity.Velocity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public class Vulcan extends ModuleMode<Velocity> {
  @Name("Vertical")
  @Clamp(min = 0.0D, max = 100.0D)
  public double vertical = 0.0D;
  
  @Name("Horizontal")
  @Clamp(min = 0.0D, max = 100.0D)
  public double horizontal = 0.0D;
  
  public Vulcan(String name, Velocity parent) {
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
          if (this.vertical == 0.0D && this.horizontal == 0.0D)
            event.setCancelled(true); 
          velocity.setMotionX((int)(velocity.getMotionX() * this.horizontal / 100.0D));
          velocity.setMotionY((int)(velocity.getMotionY() * this.vertical / 100.0D));
          velocity.setMotionZ((int)(velocity.getMotionZ() * this.horizontal / 100.0D));
        } 
      } 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\combat\velocity\modes\Vulcan.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */