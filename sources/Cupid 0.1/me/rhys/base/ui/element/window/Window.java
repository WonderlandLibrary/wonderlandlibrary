package me.rhys.base.ui.element.window;

import me.rhys.base.ui.element.Element;
import me.rhys.base.ui.element.panel.Panel;
import me.rhys.base.util.render.RenderUtil;
import me.rhys.base.util.vec.Vec2f;

public class Window extends Element {
  private final Panel panel;
  
  public Panel getPanel() {
    return this.panel;
  }
  
  public Window(Vec2f offset, int width, int height) {
    super(offset, width, height);
    this.panel = new Panel(new Vec2f(0.0F, 0.0F), width, height);
    this.panel.setParent(this);
    this.movable = true;
  }
  
  public void clickMouse(Vec2f pos, int button) {
    this.panel.clickMouse(pos, button);
  }
  
  public void dragMouse(Vec2f pos, int button, long lastClickTime) {
    this.panel.dragMouse(pos, button, lastClickTime);
  }
  
  public void releaseMouse(Vec2f pos, int button) {
    this.panel.releaseMouse(pos, button);
  }
  
  public void typeKey(char keyChar, int keyCode) {
    this.panel.typeKey(keyChar, keyCode);
  }
  
  public void draw(Vec2f mouse, float partialTicks) {
    RenderUtil.drawRect(this.pos, this.width, this.height, (getScreen()).theme.windowColors.border);
    RenderUtil.drawRect(this.pos.clone().add(1, 1), this.width - 2, this.height - 2, (getScreen()).theme.windowColors.background);
    this.panel._draw(mouse, partialTicks);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\bas\\ui\element\window\Window.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */