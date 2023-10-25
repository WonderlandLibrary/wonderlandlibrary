package me.rhys.client.module.movement.speed.mode;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMoveEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.client.module.movement.speed.Speed;

public class Teleport extends ModuleMode<Speed> {
  @Name("Speed")
  @Clamp(min = 0.5D, max = 20.0D)
  public float speed;
  
  public Teleport(String name, Speed parent) {
    super(name, (Module)parent);
    this.speed = 1.0F;
  }
  
  @EventTarget
  void onMove(PlayerMoveEvent event) {
    if (this.mc.thePlayer.isPlayerMoving()) {
      if (this.mc.thePlayer.onGround) {
        event.setMovementSpeed(this.speed);
        event.motionY = this.mc.thePlayer.motionY = 0.0D;
      } 
    } else {
      ((Speed)this.parent).pausePlayerSpeed(event);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\movement\speed\mode\Teleport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */