package me.rhys.client.module.movement.speed.mode;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMoveEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.client.module.movement.speed.Speed;

public class BHop extends ModuleMode<Speed> {
  @Name("Speed")
  @Clamp(min = 0.1D, max = 9.0D)
  public double movementSpeed;
  
  @Name("Jump")
  public boolean jump;
  
  public BHop(String name, Speed parent) {
    super(name, (Module)parent);
    this.movementSpeed = 0.5D;
    this.jump = true;
  }
  
  @EventTarget
  void onMove(PlayerMoveEvent event) {
    if (this.mc.thePlayer.isPlayerMoving()) {
      if (this.mc.thePlayer.onGround && this.jump)
        event.motionY = this.mc.thePlayer.motionY = 0.41999998688697815D; 
      event.setMovementSpeed(this.movementSpeed);
    } else {
      ((Speed)this.parent).pausePlayerSpeed(event);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\movement\speed\mode\BHop.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */