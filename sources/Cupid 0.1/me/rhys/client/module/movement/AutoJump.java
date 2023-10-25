package me.rhys.client.module.movement;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;

public class AutoJump extends Module {
  public AutoJump(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
  }
  
  @EventTarget
  public void onMove(PlayerMotionEvent event) {
    if (this.mc.thePlayer.onGround)
      this.mc.thePlayer.jump(); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\movement\AutoJump.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */