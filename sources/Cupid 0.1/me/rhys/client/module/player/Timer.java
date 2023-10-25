package me.rhys.client.module.player;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerUpdateEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;

public class Timer extends Module {
  @Name("Speed")
  @Clamp(min = 0.05D, max = 50.0D)
  public double timerSpeed = 1.5D;
  
  public Timer(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
  }
  
  public void onDisable() {
    this.mc.timer.timerSpeed = 1.0F;
  }
  
  @EventTarget
  void playerUpdate(PlayerUpdateEvent event) {
    this.mc.timer.timerSpeed = (float)this.timerSpeed;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\Timer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */