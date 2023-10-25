package me.rhys.base.event.impl.render;

import me.rhys.base.event.Event;

public class Render2DEvent extends Event {
  private int width;
  
  private int height;
  
  private float partialTicks;
  
  public Render2DEvent(int width, int height, float partialTicks) {
    this.width = width;
    this.height = height;
    this.partialTicks = partialTicks;
  }
  
  public int getWidth() {
    return this.width;
  }
  
  public int getHeight() {
    return this.height;
  }
  
  public float getPartialTicks() {
    return this.partialTicks;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\event\impl\render\Render2DEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */