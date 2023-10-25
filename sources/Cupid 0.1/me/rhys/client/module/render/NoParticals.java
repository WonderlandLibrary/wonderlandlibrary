package me.rhys.client.module.render;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;

public class NoParticals extends Module {
  public NoParticals(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
  }
  
  @EventTarget
  public void onRec(PacketEvent event) {
    if (event.getPacket() instanceof net.minecraft.network.play.server.S2APacketParticles)
      event.setCancelled(true); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\render\NoParticals.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */