package me.rhys.client.ui.click.element.slider;

import me.rhys.base.module.setting.impl.number.impl.IntNumberSetting;
import me.rhys.base.util.MathUtil;
import me.rhys.base.util.vec.Vec2f;

public class IntSettingSlider extends SettingSlider<IntNumberSetting> {
  public IntSettingSlider(IntNumberSetting numberSetting, Vec2f offset, int width, int height) {
    super(numberSetting, offset, width, height);
    this.current = ((Integer)numberSetting.get()).intValue();
  }
  
  public void dragMouse(Vec2f pos, int button, long lastClickTime) {
    double offset = (pos.clone().sub(this.pos.x, this.pos.y)).x / this.width;
    this.setting.set(Integer.valueOf((int)Math.max(this.min, Math.min(this.max, MathUtil.round(this.max * offset, 1)))));
    this.current = ((Integer)this.setting.get()).intValue();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\clien\\ui\click\element\slider\IntSettingSlider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */