package me.rhys.client.module.movement.noslow.modes;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerNoSlowEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.client.module.movement.noslow.NoSlow;

public class Vanilla extends ModuleMode<NoSlow> {
  public Vanilla(String name, NoSlow parent) {
    super(name, (Module)parent);
  }
  
  @EventTarget
  public void onNoSlow(PlayerNoSlowEvent event) {
    event.setCancelled(true);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\movement\noslow\modes\Vanilla.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */