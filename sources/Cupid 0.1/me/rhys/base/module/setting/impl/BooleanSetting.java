package me.rhys.base.module.setting.impl;

import java.lang.reflect.Field;
import me.rhys.base.module.setting.Setting;

public class BooleanSetting extends Setting {
  public BooleanSetting(Object object, Field field) {
    super(object, field);
  }
  
  public void toggle() {
    set(!get());
  }
  
  public void set(boolean value) {
    try {
      this.field.set(this.object, Boolean.valueOf(value));
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } 
  }
  
  public boolean get() {
    try {
      return this.field.getBoolean(this.object);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
      return false;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\module\setting\impl\BooleanSetting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */