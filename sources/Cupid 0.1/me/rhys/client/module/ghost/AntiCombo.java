package me.rhys.client.module.ghost;

import me.rhys.base.event.Event;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public class AntiCombo extends Module {
  public double vertical = 100.0D;
  
  public double horizontal = 60.0D;
  
  public AntiCombo(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
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


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\ghost\AntiCombo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */