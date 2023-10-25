package me.rhys.client.ui.click.element.dropdown;

import java.util.Arrays;
import java.util.Comparator;
import me.rhys.base.module.setting.impl.EnumSetting;
import me.rhys.base.ui.element.button.DropDownButton;
import me.rhys.base.util.vec.Vec2f;

public class EnumDropDown extends DropDownButton {
  private final EnumSetting setting;
  
  public EnumDropDown(EnumSetting setting, Vec2f offset, int width, int height) {
    super(offset, width, height, new String[] { setting.get() });
    this.setting = setting;
    Arrays.<String>stream(setting.values()).filter(value -> !value.equalsIgnoreCase(setting.get())).sorted(Comparator.comparingInt(value -> value.charAt(0))).forEachOrdered(this.items::add);
  }
  
  public void setCurrent(String current) {
    if (this.items.contains(current)) {
      this.label = current;
      this.current = this.items.indexOf(current);
      this.setting.set(this.label);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\clien\\ui\click\element\dropdown\EnumDropDown.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */