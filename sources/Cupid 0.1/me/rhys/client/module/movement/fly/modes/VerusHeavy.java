package me.rhys.client.module.movement.fly.modes;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.client.module.movement.fly.Fly;

public class VerusHeavy extends ModuleMode<Fly> {
  private double startY;
  
  public VerusHeavy(String name, Fly parent) {
    super(name, (Module)parent);
  }
  
  public void onEnable() {
    this.startY = this.mc.thePlayer.posY;
  }
  
  @EventTarget
  void onMotion(PlayerMotionEvent event) {
    if (this.mc.thePlayer.posY < this.startY) {
      this.mc.thePlayer.motionY = 0.41999998688697815D;
      event.setOnGround(true);
    } else {
      event.setOnGround(true);
      this.mc.thePlayer.motionY = 0.0D;
      this.mc.thePlayer.setSprinting(false);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\movement\fly\modes\VerusHeavy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */