package me.rhys.client.module.player;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;

public class PacketBlock extends Module {
  public PacketBlock(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
  }
  
  @EventTarget
  public void onRejoin(PacketEvent event) {
    if (event.getPacket() instanceof net.minecraft.network.play.client.C03PacketPlayer)
      event.setCancelled(true); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\PacketBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */