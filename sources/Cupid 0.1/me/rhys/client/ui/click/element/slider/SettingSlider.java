package me.rhys.client.ui.click.element.slider;

import me.rhys.base.module.setting.impl.number.NumberSetting;
import me.rhys.base.ui.element.slider.Slider;
import me.rhys.base.util.vec.Vec2f;

public class SettingSlider<T extends NumberSetting> extends Slider {
  protected final T setting;
  
  public T getSetting() {
    return this.setting;
  }
  
  public SettingSlider(T numberSetting, Vec2f offset, int width, int height) {
    super(offset, width, height, numberSetting.getMin(), numberSetting.getMax(), 0.0D);
    this.setting = numberSetting;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\clien\\ui\click\element\slider\SettingSlider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */