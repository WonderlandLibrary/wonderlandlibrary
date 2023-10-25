package me.rhys.client.module.player.disabler.modes;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.client.module.player.disabler.Disabler;

public class KeepAliveCancel extends ModuleMode<Disabler> {
  public KeepAliveCancel(String name, Disabler parent) {
    super(name, (Module)parent);
  }
  
  @EventTarget
  void onPacket(PacketEvent event) {
    if (event.getPacket() instanceof net.minecraft.network.play.client.C00PacketKeepAlive)
      event.setCancelled(true); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\disabler\modes\KeepAliveCancel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */