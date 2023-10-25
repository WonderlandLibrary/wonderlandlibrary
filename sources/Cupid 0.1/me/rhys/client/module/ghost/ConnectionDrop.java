package me.rhys.client.module.ghost;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;

public class ConnectionDrop extends Module {
  public long time;
  
  public ConnectionDrop(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
    this.time = 0L;
  }
  
  @EventTarget
  public void onPacket(PacketEvent event) {
    if (event.getPacket() instanceof net.minecraft.network.play.client.C03PacketPlayer) {
      this.time++;
      if (this.time == 10L)
        event.setCancelled(true); 
      if (this.time == 18L)
        this.time = 0L; 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\ghost\ConnectionDrop.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */