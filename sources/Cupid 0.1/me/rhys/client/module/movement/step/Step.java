package me.rhys.client.module.movement.step;

import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.client.module.movement.step.modes.Instant;
import net.minecraft.entity.Entity;

public class Step extends Module {
  @Name("Height")
  @Clamp(min = 1.0D, max = 2.0D)
  public double height = 1.5D;
  
  public long lastStep;
  
  public Step(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
    add((ModuleMode)new Instant("Instant", this));
  }
  
  public float getNeededStepHeight() {
    if (this.mc.theWorld.getCollidingBoundingBoxes((Entity)this.mc.thePlayer, this.mc.thePlayer.boundingBox.offset(this.mc.thePlayer.motionX, 1.1D, this.mc.thePlayer.motionZ))
      .size() == 0)
      return 1.0F; 
    if (this.mc.theWorld.getCollidingBoundingBoxes((Entity)this.mc.thePlayer, this.mc.thePlayer.boundingBox.offset(this.mc.thePlayer.motionX, 1.6D, this.mc.thePlayer.motionZ))
      .size() == 0)
      return 1.5F; 
    return 2.0F;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\movement\step\Step.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */