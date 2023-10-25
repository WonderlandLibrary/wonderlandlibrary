package me.rhys.client.module.player.nofall.modes;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.client.module.player.nofall.NoFall;

public class NoGround extends ModuleMode<NoFall> {
  public NoGround(String name, NoFall parent) {
    super(name, (Module)parent);
  }
  
  @EventTarget
  void onMotion(PlayerMotionEvent event) {
    event.setOnGround(false);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\nofall\modes\NoGround.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */