package me.rhys.client.module.player.disabler.modes;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.client.module.player.disabler.Disabler;

public class Ground extends ModuleMode<Disabler> {
  public Ground(String name, Disabler parent) {
    super(name, (Module)parent);
  }
  
  @EventTarget
  public void onMotion(PlayerMotionEvent event) {
    event.setOnGround(true);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\disabler\modes\Ground.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */