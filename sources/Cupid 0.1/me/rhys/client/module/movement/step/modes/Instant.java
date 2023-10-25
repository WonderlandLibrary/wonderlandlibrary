package me.rhys.client.module.movement.step.modes;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.StepEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.client.module.movement.step.Step;

public class Instant extends ModuleMode<Step> {
  public Instant(String name, Step parent) {
    super(name, (Module)parent);
  }
  
  @EventTarget
  public void onStep(StepEvent event) {
    if (this.mc.thePlayer == null)
      return; 
    if (this.mc.thePlayer.isCollidedHorizontally && this.mc.thePlayer.isCollidedVertically && 
      !this.mc.thePlayer.checkBlockAbove(1.0F)) {
      float stepHeight;
      if ((stepHeight = ((Step)this.parent).getNeededStepHeight()) > ((Step)this.parent).height)
        return; 
      ((Step)this.parent).lastStep = System.currentTimeMillis();
      event.setStepHeight(stepHeight);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\movement\step\modes\Instant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */