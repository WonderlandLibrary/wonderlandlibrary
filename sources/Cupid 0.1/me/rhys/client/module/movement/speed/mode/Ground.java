package me.rhys.client.module.movement.speed.mode;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMoveEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.client.module.movement.speed.Speed;

public class Ground extends ModuleMode<Speed> {
  @Name("Mode")
  public Mode moduleMode;
  
  @Name("Speed")
  @Clamp(min = 1.0D, max = 50.0D)
  public double movementSpeed;
  
  @Name("Timer")
  @Clamp(min = 0.0D, max = 10.0D)
  public float timer;
  
  public Ground(String name, Speed parent) {
    super(name, (Module)parent);
    this.moduleMode = Mode.TICK;
    this.movementSpeed = 2.0D;
    this.timer = 1.0F;
  }
  
  public void onDisable() {
    this.mc.timer.timerSpeed = 1.0F;
  }
  
  private enum Mode {
    TICK, DEV, GROUNDTIMER;
  }
  
  @EventTarget
  void onMove(PlayerMoveEvent event) {
    switch (this.moduleMode) {
      case TICK:
        if (this.mc.thePlayer.onGround)
          event.motionY = this.mc.thePlayer.motionY = this.movementSpeed; 
        if (this.mc.thePlayer.isAirBorne)
          event.motionY = this.mc.thePlayer.motionY = -this.movementSpeed; 
      case DEV:
        if (this.mc.thePlayer.onGround)
          event.motionZ = event.motionX = this.mc.thePlayer.motionX = this.mc.thePlayer.motionZ = this.movementSpeed; 
      case GROUNDTIMER:
        if (this.mc.thePlayer.onGround)
          this.mc.timer.timerSpeed = this.timer; 
        break;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\movement\speed\mode\Ground.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */