package me.rhys.client.module.movement.speed.mode;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.event.impl.player.PlayerMoveEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.client.module.movement.speed.Speed;

public class YPort extends ModuleMode<Speed> {
  @Name("Speed")
  @Clamp(min = 0.1D, max = 9.0D)
  public double movementSpeed;
  
  @Name("Mode")
  public Mode mode;
  
  @Name("Silent YTick")
  @Clamp(min = 1.0D, max = 20.0D)
  public int silentTick;
  
  @Name("Silent Ground Spoof")
  public boolean silentGroundSpoof;
  
  public YPort(String name, Speed parent) {
    super(name, (Module)parent);
    this.movementSpeed = 0.5D;
    this.mode = Mode.SET_MOTION;
    this.silentTick = 3;
    this.silentGroundSpoof = true;
  }
  
  @EventTarget
  void onMove(PlayerMoveEvent event) {
    switch (this.mode) {
      case SET_MOTION:
        if (this.mc.thePlayer.isPlayerMoving()) {
          event.motionY = this.mc.thePlayer.motionY = 0.41999998688697815D;
          event.motionY = this.mc.thePlayer.motionY - 0.41999998688697815D;
          event.setMovementSpeed(this.movementSpeed);
          break;
        } 
        ((Speed)this.parent).pausePlayerSpeed(event);
        if (this.mc.thePlayer.motionY > 0.0D)
          event.motionY = this.mc.thePlayer.motionY - 0.41999998688697815D; 
        break;
      case SILENT:
        if (this.mc.thePlayer.isPlayerMoving()) {
          event.setMovementSpeed(this.movementSpeed);
          break;
        } 
        event.motionX = event.motionZ = this.mc.thePlayer.motionX = this.mc.thePlayer.motionZ = 0.0D;
        break;
    } 
  }
  
  @EventTarget
  void onMotion(PlayerMotionEvent event) {
    if (this.mode == Mode.SILENT && this.mc.thePlayer.ticksExisted % this.silentTick == 0) {
      if (this.silentGroundSpoof)
        event.setOnGround(false); 
      event.getPosition().setY(event.getPosition().getY() + 0.41999998688697815D);
    } 
  }
  
  public enum Mode {
    SET_MOTION, SILENT;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\movement\speed\mode\YPort.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */