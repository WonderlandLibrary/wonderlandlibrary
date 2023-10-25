package me.rhys.base.module.setting.impl;

import java.lang.reflect.Field;
import me.rhys.base.module.setting.Setting;
import me.rhys.base.util.accessor.impl.MethodAccessor;

public class EnumSetting extends Setting {
  public EnumSetting(Object object, Field field) {
    super(object, field);
  }
  
  public void toggle() {
    set(get());
  }
  
  public boolean set(String choice) {
    try {
      Object value = Enum.valueOf(this.field.getType(), choice);
      this.field.set(this.object, value);
      return true;
    } catch (Exception e) {
      return false;
    } 
  }
  
  public String[] values() {
    try {
      Enum<?> value = (Enum)this.field.get(this.object);
      MethodAccessor methodAccessor = new MethodAccessor(value.getClass(), "values", new Class[0]);
      Object[] objs = (Object[])methodAccessor.invoke(value.getClass(), new Object[0]);
      String[] strings = new String[objs.length];
      for (int i = 0; i < objs.length; i++)
        strings[i] = objs[i].toString(); 
      return strings;
    } catch (IllegalAccessException e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  public int ordinal() {
    try {
      Enum<?> value = (Enum)this.field.get(this.object);
      return value.ordinal();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
      return -1;
    } 
  }
  
  public String get() {
    try {
      return this.field.get(this.object).toString();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
      return "";
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\module\setting\impl\EnumSetting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */