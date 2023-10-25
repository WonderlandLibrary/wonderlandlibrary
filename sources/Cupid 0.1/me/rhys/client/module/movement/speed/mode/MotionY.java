package me.rhys.client.module.movement.speed.mode;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMoveEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.client.module.movement.speed.Speed;

public class MotionY extends ModuleMode<Speed> {
  @Name("Timer")
  @Clamp(min = 0.0D, max = 10.0D)
  public float timer;
  
  @Name("MotionY")
  @Clamp(min = 0.1D, max = 3.0D)
  public float my;
  
  @Name("Speed")
  @Clamp(min = 0.1D, max = 5.0D)
  public float speed;
  
  public MotionY(String name, Speed parent) {
    super(name, (Module)parent);
    this.timer = 1.0F;
    this.my = 0.8F;
    this.speed = 0.65F;
  }
  
  public void onDisable() {
    this.mc.timer.timerSpeed = 1.0F;
  }
  
  @EventTarget
  void onMove(PlayerMoveEvent event) {
    if (this.mc.thePlayer.isPlayerMoving()) {
      this.mc.timer.timerSpeed = this.timer;
      if (this.mc.thePlayer.onGround) {
        event.motionY = this.mc.thePlayer.motionY = this.my;
        event.setMovementSpeed(this.speed);
      } 
    } else {
      ((Speed)this.parent).pausePlayerSpeed(event);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\movement\speed\mode\MotionY.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */