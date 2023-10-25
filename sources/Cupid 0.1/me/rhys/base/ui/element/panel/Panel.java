package me.rhys.base.ui.element.panel;

import me.rhys.base.ui.UIScreen;
import me.rhys.base.ui.element.Element;
import me.rhys.base.util.container.Container;
import me.rhys.base.util.vec.Vec2f;

public class Panel extends Element {
  protected final Container<Element> container = new Container();
  
  public Container<Element> getContainer() {
    return this.container;
  }
  
  public Panel(Vec2f offset, int width, int height) {
    super(offset, width, height);
  }
  
  public void add(Element element) {
    element.setParent(this);
    element.setScreen(getScreen());
    this.container.add(element);
  }
  
  public void remove(Element element) {
    this.container.remove(element);
  }
  
  public void clickMouse(Vec2f pos, int button) {
    this.container.filter(element -> element.isHovered(pos)).forEach(element -> element.clickMouse(pos, button));
  }
  
  public void dragMouse(Vec2f pos, int button, long lastClickTime) {
    this.container.filter(element -> element.isHovered(pos)).forEach(element -> element.dragMouse(pos, button, lastClickTime));
  }
  
  public void releaseMouse(Vec2f pos, int button) {
    this.container.filter(element -> element.isHovered(pos)).forEach(element -> element.releaseMouse(pos, button));
  }
  
  public void typeKey(char keyChar, int keyCode) {
    this.container.forEach(element -> element.typeKey(keyChar, keyCode));
  }
  
  public void draw(Vec2f mouse, float partialTicks) {
    this.container.forEach(element -> element._draw(mouse, partialTicks));
  }
  
  public void setScreen(UIScreen screen) {
    this.container.forEach(element -> element.setScreen(screen));
    super.setScreen(screen);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\bas\\ui\element\panel\Panel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */