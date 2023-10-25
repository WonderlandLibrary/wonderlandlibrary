package me.rhys.base.ui.element.button;

import me.rhys.base.ui.FlagCallback;
import me.rhys.base.ui.UIScreen;
import me.rhys.base.util.render.ColorUtil;
import me.rhys.base.util.render.FontUtil;
import me.rhys.base.util.render.RenderUtil;
import me.rhys.base.util.vec.Vec2f;

public class CheckBox extends Button {
  private boolean checked;
  
  private FlagCallback callback;
  
  public void setChecked(boolean checked) {
    this.checked = checked;
  }
  
  public void setCallback(FlagCallback callback) {
    this.callback = callback;
  }
  
  public boolean isChecked() {
    return this.checked;
  }
  
  public FlagCallback getCallback() {
    return this.callback;
  }
  
  public CheckBox(String label, Vec2f offset, int width, int height) {
    super(label, offset, width, height);
    this.checked = false;
    this.callback = null;
  }
  
  public void clickMouse(Vec2f pos, int button) {
    if (this.callback != null) {
      this.checked = this.callback.handle(pos);
    } else {
      this.checked = !this.checked;
    } 
  }
  
  public void draw(Vec2f mouse, float partialTicks) {
    UIScreen screen = getScreen();
    if (this.background == ColorUtil.Colors.TRANSPARENT.getColor()) {
      RenderUtil.drawRect(this.pos.clone().sub(1.0F, 1.0F), this.width + 2, this.height + 2, screen.theme.checkBoxColors.border);
      RenderUtil.drawRect(this.pos, this.width, this.height, screen.theme.checkBoxColors.background);
    } 
    if (this.checked)
      RenderUtil.drawRect(this.pos, this.width, this.height, screen.theme.checkBoxColors.active); 
    FontUtil.drawStringWithShadow(this.label, this.pos.clone().add((this.width + 5), (this.height - FontUtil.getFontHeight()) / 2.0F), screen.theme.buttonColors.text);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\bas\\ui\element\button\CheckBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */