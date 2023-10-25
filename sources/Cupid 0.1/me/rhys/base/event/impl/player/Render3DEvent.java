package me.rhys.base.event.impl.player;

import me.rhys.base.event.Event;

public class Render3DEvent extends Event {
  private float partialTicks;
  
  public Render3DEvent(float partialTicks) {
    this.partialTicks = partialTicks;
  }
  
  public float getPartialTicks() {
    return this.partialTicks;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\event\impl\player\Render3DEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */