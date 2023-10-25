package me.rhys.client.ui.click.element.button;

import me.rhys.base.module.Module;
import me.rhys.base.ui.element.button.Button;
import me.rhys.base.util.render.ColorUtil;
import me.rhys.base.util.render.RenderUtil;
import me.rhys.base.util.vec.Vec2f;

public class ModuleButton extends Button {
  private final Module module;
  
  public Module getModule() {
    return this.module;
  }
  
  public ModuleButton(Module module, Vec2f offset, int width, int height) {
    super(module.getData().getName(), offset, width, height);
    this.module = module;
  }
  
  public void clickMouse(Vec2f pos, int button) {
    if (button == 0)
      this.module.toggle(); 
  }
  
  public void draw(Vec2f mouse, float partialTicks) {
    super.draw(mouse, partialTicks);
    RenderUtil.drawRect(this.pos, 1, this.height, this.module.getData().isEnabled() ? ColorUtil.rgba(0, 255, 0, 1.0F) : ColorUtil.rgba(255, 0, 0, 1.0F));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\clien\\ui\click\element\button\ModuleButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */