package me.rhys.client.ui.click.element.slider;

import me.rhys.base.module.setting.impl.number.impl.DoubleNumberSetting;
import me.rhys.base.util.MathUtil;
import me.rhys.base.util.vec.Vec2f;

public class DoubleSettingSlider extends SettingSlider<DoubleNumberSetting> {
  public DoubleSettingSlider(DoubleNumberSetting numberSetting, Vec2f offset, int width, int height) {
    super(numberSetting, offset, width, height);
    this.current = ((Double)numberSetting.get()).doubleValue();
  }
  
  public void dragMouse(Vec2f pos, int button, long lastClickTime) {
    double offset = (pos.clone().sub(this.pos.x, this.pos.y)).x / this.width;
    this.setting.set(Double.valueOf(Math.max(this.min, Math.min(this.max, MathUtil.round(this.max * offset, 1)))));
    this.current = ((Double)this.setting.get()).doubleValue();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\clien\\ui\click\element\slider\DoubleSettingSlider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */