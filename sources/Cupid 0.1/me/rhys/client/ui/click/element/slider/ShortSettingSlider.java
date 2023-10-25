package me.rhys.client.ui.click.element.slider;

import me.rhys.base.module.setting.impl.number.impl.ShortNumberSetting;
import me.rhys.base.util.MathUtil;
import me.rhys.base.util.vec.Vec2f;

public class ShortSettingSlider extends SettingSlider<ShortNumberSetting> {
  public ShortSettingSlider(ShortNumberSetting numberSetting, Vec2f offset, int width, int height) {
    super(numberSetting, offset, width, height);
    this.current = ((Short)numberSetting.get()).shortValue();
  }
  
  public void dragMouse(Vec2f pos, int button, long lastClickTime) {
    double offset = (pos.clone().sub(this.pos.x, this.pos.y)).x / this.width;
    this.setting.set(Short.valueOf((short)(int)Math.max(this.min, Math.min(this.max, MathUtil.round(this.max * offset, 1)))));
    this.current = ((Short)this.setting.get()).shortValue();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\clien\\ui\click\element\slider\ShortSettingSlider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */