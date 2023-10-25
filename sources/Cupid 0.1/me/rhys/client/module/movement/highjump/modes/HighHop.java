package me.rhys.client.module.movement.highjump.modes;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMoveEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.client.module.movement.highjump.HighJump;

public class HighHop extends ModuleMode<HighJump> {
  @Name("Mode")
  public Mode moduleMode;
  
  @Name("Speed")
  @Clamp(min = 1.0D, max = 15.0D)
  public double movementSpeed;
  
  public HighHop(String name, HighJump parent) {
    super(name, (Module)parent);
    this.moduleMode = Mode.REPEAT;
    this.movementSpeed = 3.0D;
  }
  
  private enum Mode {
    REPEAT, ONCE;
  }
  
  @EventTarget
  void onMove(PlayerMoveEvent event) {
    switch (this.moduleMode) {
      case REPEAT:
        if (this.mc.thePlayer.onGround)
          event.motionY = this.mc.thePlayer.motionY = this.movementSpeed; 
      case ONCE:
        if (this.mc.thePlayer.onGround)
          event.motionY = this.mc.thePlayer.motionY = this.movementSpeed; 
        break;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\movement\highjump\modes\HighHop.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */