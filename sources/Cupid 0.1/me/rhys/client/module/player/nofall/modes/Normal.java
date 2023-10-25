package me.rhys.client.module.player.nofall.modes;

import me.rhys.base.event.Event;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.client.module.player.nofall.NoFall;

public class Normal extends ModuleMode<NoFall> {
  public Normal(String name, NoFall parent) {
    super(name, (Module)parent);
  }
  
  @EventTarget
  void onMotion(PlayerMotionEvent event) {
    if (event.getType() == Event.Type.PRE && this.mc.thePlayer.fallDistance > 3.0F)
      event.setOnGround(true); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\nofall\modes\Normal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */