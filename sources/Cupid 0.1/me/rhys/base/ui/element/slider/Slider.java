package me.rhys.base.ui.element.slider;

import me.rhys.base.ui.UIScreen;
import me.rhys.base.ui.element.Element;
import me.rhys.base.util.MathUtil;
import me.rhys.base.util.render.ColorUtil;
import me.rhys.base.util.render.FontUtil;
import me.rhys.base.util.render.RenderUtil;
import me.rhys.base.util.vec.Vec2f;

public class Slider extends Element {
  protected double min;
  
  protected double max;
  
  protected double current;
  
  public double getMin() {
    return this.min;
  }
  
  public double getMax() {
    return this.max;
  }
  
  public double getCurrent() {
    return this.current;
  }
  
  public Slider(Vec2f offset, int width, int height, double min, double max, double current) {
    super(offset, width, height);
    this.min = min;
    this.max = max;
    this.current = current;
  }
  
  public void dragMouse(Vec2f pos, int button, long lastClickTime) {
    double offset = (pos.clone().sub(this.pos.x, this.pos.y)).x / this.width;
    this.current = Math.max(this.min, Math.min(this.max, MathUtil.round(this.max * offset, 1)));
  }
  
  public void draw(Vec2f mouse, float partialTicks) {
    UIScreen screen = getScreen();
    RenderUtil.drawRect(this.pos.clone().sub(1.0F, 1.0F), this.width + 2, this.height + 2, ColorUtil.darken(screen.theme.sliderColors.background, 15).getRGB());
    RenderUtil.drawRect(this.pos, this.width, this.height, screen.theme.sliderColors.background);
    double fill = this.current / this.max;
    RenderUtil.drawRect(this.pos, (int)(this.width * fill), this.height, screen.theme.sliderColors.fill);
    FontUtil.drawCenteredStringWithShadow(String.valueOf(this.current), this.pos.clone().add((float)(this.width * fill), this.height / 2.0F), -1);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\bas\\ui\element\slider\Slider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */