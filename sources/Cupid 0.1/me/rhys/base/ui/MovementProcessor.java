package me.rhys.base.ui;

import me.rhys.base.ui.element.Element;
import me.rhys.base.ui.element.panel.Panel;
import me.rhys.base.util.container.MapContainer;
import me.rhys.base.util.vec.Vec2f;

public class MovementProcessor extends MapContainer<Element, MovementProcessor.Entry> {
  private Panel parent;
  
  public void setParent(Panel parent) {
    this.parent = parent;
  }
  
  public void clickMouse(Vec2f pos, int button) {
    if (button == 0)
      this.parent.getContainer().filter(element -> (element.movable && element.isHovered(pos) && element.callback != null && element.callback.handle(pos))).forEach(element -> {
            getMap().putIfAbsent(element, new Entry());
            Entry entry = (Entry)get(element);
            entry.mouseDown = true;
            entry.lockPos = pos.clone().sub(element.offset.x, element.offset.y);
          }); 
  }
  
  public void releaseMouse(Vec2f pos, int button) {
    if (button == 0)
      this.parent.getContainer().filter(element -> element.movable).forEach(element -> {
            getMap().putIfAbsent(element, new Entry());
            ((Entry)get(element)).mouseDown = false;
          }); 
  }
  
  public void updatePositions(Vec2f mouse) {
    this.parent.getContainer().filter(element -> element.movable).forEach(element -> {
          getMap().putIfAbsent(element, new Entry());
          Entry entry = (Entry)get(element);
          if (entry.mouseDown)
            element.offset = mouse.clone().sub(entry.lockPos.x, entry.lockPos.y); 
        });
  }
  
  public static class Entry {
    boolean mouseDown = false;
    
    Vec2f lockPos = new Vec2f();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\bas\\ui\MovementProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */