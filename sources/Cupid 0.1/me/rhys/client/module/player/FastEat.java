package me.rhys.client.module.player;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerUpdateEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;

public class FastEat extends Module {
  public FastEat(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
  }
  
  public void onDisable() {
    this.mc.timer.timerSpeed = 1.0F;
  }
  
  @EventTarget
  public void onUpdate(PlayerUpdateEvent event) {
    if (this.mc.thePlayer.isEating()) {
      this.mc.timer.timerSpeed = 3.0F;
    } else {
      this.mc.timer.timerSpeed = 1.0F;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\FastEat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */