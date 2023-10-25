package me.rhys.base.event.impl.render;

public class RenderGameOverlayEvent extends RenderEvent {
  private int width;
  
  private int height;
  
  public int getWidth() {
    return this.width;
  }
  
  public int getHeight() {
    return this.height;
  }
  
  public RenderGameOverlayEvent(float partialTicks, int width, int height) {
    super(partialTicks);
    this.width = width;
    this.height = height;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\event\impl\render\RenderGameOverlayEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */