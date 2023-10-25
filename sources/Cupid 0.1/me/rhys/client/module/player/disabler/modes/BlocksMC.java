package me.rhys.client.module.player.disabler.modes;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.client.module.player.disabler.Disabler;

public class BlocksMC extends ModuleMode<Disabler> {
  public BlocksMC(String name, Disabler parent) {
    super(name, (Module)parent);
  }
  
  @EventTarget
  private void onUpdate(PacketEvent event) {}
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\disabler\modes\BlocksMC.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */