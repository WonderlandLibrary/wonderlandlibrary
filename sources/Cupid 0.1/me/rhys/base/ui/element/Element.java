package me.rhys.base.ui.element;

import me.rhys.base.ui.FlagCallback;
import me.rhys.base.ui.UIElement;
import me.rhys.base.ui.UIScreen;
import me.rhys.base.util.render.ColorUtil;
import me.rhys.base.util.render.RenderUtil;
import me.rhys.base.util.vec.Vec2f;

public class Element implements UIElement {
  private UIScreen screen;
  
  private Element parent;
  
  public Vec2f offset;
  
  public Vec2f pos;
  
  public int background;
  
  public FlagCallback callback;
  
  public boolean movable;
  
  protected int width;
  
  protected int height;
  
  public void setScreen(UIScreen screen) {
    this.screen = screen;
  }
  
  public UIScreen getScreen() {
    return this.screen;
  }
  
  public void setParent(Element parent) {
    this.parent = parent;
  }
  
  public Element getParent() {
    return this.parent;
  }
  
  public Vec2f getPos() {
    return this.pos;
  }
  
  public void setWidth(int width) {
    this.width = width;
  }
  
  public int getWidth() {
    return this.width;
  }
  
  public void setHeight(int height) {
    this.height = height;
  }
  
  public int getHeight() {
    return this.height;
  }
  
  public Element(Vec2f offset, int width, int height) {
    this.offset = offset;
    this.pos = offset;
    this.width = width;
    this.height = height;
    this.movable = false;
    this.background = ColorUtil.Colors.TRANSPARENT.getColor();
    this.callback = null;
  }
  
  public void _draw(Vec2f mouse, float partialTicks) {
    Vec2f parent = (this.parent == null) ? new Vec2f(0.0F, 0.0F) : this.parent.pos;
    this.pos = parent.clone().add(this.offset.x, this.offset.y);
    if (!ColorUtil.isTransparent(this.background))
      RenderUtil.drawRect(this.pos, this.width, this.height, this.background); 
    draw(mouse, partialTicks);
  }
  
  public boolean isHovered(Vec2f mouse) {
    return (mouse.x >= this.pos.x && mouse.y >= this.pos.y && mouse.x <= this.pos.x + this.width && mouse.y <= this.pos.y + this.height);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\bas\\ui\element\Element.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */