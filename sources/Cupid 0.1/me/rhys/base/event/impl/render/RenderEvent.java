package me.rhys.base.event.impl.render;

import me.rhys.base.event.Event;

public class RenderEvent extends Event {
  private final float partialTicks;
  
  public RenderEvent(float partialTicks) {
    this.partialTicks = partialTicks;
  }
  
  public float getPartialTicks() {
    return this.partialTicks;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\event\impl\render\RenderEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */