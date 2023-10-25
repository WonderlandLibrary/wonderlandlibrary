package me.rhys.base.event.impl.player;

import me.rhys.base.event.Event;
import net.minecraft.entity.Entity;

public class StepEvent extends Event {
  private float stepHeight;
  
  private Entity entity;
  
  private boolean shouldStep;
  
  public void setStepHeight(float stepHeight) {
    this.stepHeight = stepHeight;
  }
  
  public void setEntity(Entity entity) {
    this.entity = entity;
  }
  
  public void setShouldStep(boolean shouldStep) {
    this.shouldStep = shouldStep;
  }
  
  public float getStepHeight() {
    return this.stepHeight;
  }
  
  public Entity getEntity() {
    return this.entity;
  }
  
  public boolean isShouldStep() {
    return this.shouldStep;
  }
  
  public StepEvent(Entity entity, float stepHeight) {
    this.entity = entity;
    this.stepHeight = stepHeight;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\event\impl\player\StepEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */